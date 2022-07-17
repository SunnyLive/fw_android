package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.GiftVpAdapter;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeGiftEvent;
import com.fengwo.module_live_vedio.utils.WishCacheMr;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import io.reactivex.disposables.Disposable;
import razerdp.basepopup.BasePopupWindow;

/**
 * 选择礼物弹窗
 */
public class SelectGiftPopWindow extends BasePopupWindow implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener {

    private List<List<GiftDto>> allGifts;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout llPoint;
    private SparseArray<PagerAdapter> vpAdapters;
    private Context mContext;
    private int currentPage = 0;
    private int vpCheckPosition = 0;
    private int wishType;


    @SuppressLint("CheckResult")
    public SelectGiftPopWindow(Context context, List<List<GiftDto>> data) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        mContext = context;
        allGifts = data;
        vpAdapters = new SparseArray<>();
        tabLayout = findViewById(R.id.tabview);
        viewPager = findViewById(R.id.vp);
        llPoint = findViewById(R.id.ll_point);
        findViewById(R.id.iv_select_gif_back).setOnClickListener(this);
        findViewById(R.id.iv_select_gif_sure).setOnClickListener(this);
//        设置礼物类型
        for (int i = 0; i < allGifts.size(); i++) {
            String title = allGifts.get(i).get(0).giftTypeText;
            TabLayout.Tab t = tabLayout.newTab();
            t.setText(title);
            tabLayout.addTab(t);
        }
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.getTabAt(0).select();
        if (null == vpAdapters.get(0)) {
            GiftVpAdapter adapter = new GiftVpAdapter(context, allGifts.get(0));
            vpAdapters.put(0, adapter);
        }
        viewPager.setAdapter(vpAdapters.get(0));
        viewPager.setOffscreenPageLimit(3);
        setPoint(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPoint(position);
                setCheckZero(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setWishType( int wishType){
        this.wishType = wishType;
    }
    private void setPoint(int position) {
        int count = vpAdapters.get(position).getCount();
        llPoint.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DensityUtils.dp2px(mContext, 10);
            params.leftMargin = DensityUtils.dp2px(mContext, 10);
            point.setLayoutParams(params);
            point.setBackgroundResource(com.fengwo.module_comment.R.drawable.selector_gift_point_bg);
            if (0 == i) {
                currentPage = i;
                point.setEnabled(false);
            }
            llPoint.addView(point);
        }
    }

    private void setCurrentPoint(int position) {
        llPoint.getChildAt(position).setEnabled(false);
        llPoint.getChildAt(currentPage).setEnabled(true);
        currentPage = position;

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_select_pop_gift);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpCheckPosition = tab.getPosition();
        if (null == vpAdapters.get(vpCheckPosition)) {
            GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(vpCheckPosition));
            vpAdapters.put(vpCheckPosition, adapter);
        }
        viewPager.setAdapter(vpAdapters.get(vpCheckPosition));
        setPoint(vpCheckPosition);
        setCheckZero(0);
    }


    private void setCheckZero(int position) {
        if (null == vpAdapters || vpAdapters.size() == 0) {
            return;
        }
        GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
        adapter.setCheck(position, 0);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    AddAnchorWishPop addAnchorWishPop;
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (addAnchorWishPop ==null){
            addAnchorWishPop  = new AddAnchorWishPop(getContext());
        }
        addAnchorWishPop.setWishType(wishType);
        if (id == R.id.iv_select_gif_back) {
            dismiss();
        }else if (id == R.id.iv_select_gif_sure) {//确定选择礼物
            GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
            GiftDto dto = adapter.getGift(currentPage);
            if (WishCacheMr.getInstance().isExitGiftId(dto.id)){
                ToastUtils.showShort(getContext(),"心愿礼物不能相同");
            }else{
                dismiss();
                addAnchorWishPop.setGifName(dto.giftName,dto.id,dto.giftIcon);
//                addAnchorWishPop.showPopupWindow();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (addAnchorWishPop ==null){
            addAnchorWishPop  = new AddAnchorWishPop(getContext());
            addAnchorWishPop.showPopupWindow();
        }else{
            addAnchorWishPop.showPopupWindow();
        }
    }
}
