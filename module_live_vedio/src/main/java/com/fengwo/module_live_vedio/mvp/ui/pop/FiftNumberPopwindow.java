package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MakeExpansionDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.GiftNumberMoreAdapters;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @anchor Administrator
 * @date 2020/9/2
 */
public class FiftNumberPopwindow extends BasePopupWindow {

    private RecyclerView recyclerView;
    public Context context;
    private List<MakeExpansionDto> img = new ArrayList();//{R.drawable.ic_beauty, R.drawable.ic_share,R.drawable.live_ic_close_animate};
    private GiftNumberMoreAdapters livePushMoreAdapter;
    private List<MakeExpansionDto> listData;
    LinearLayout ll_view;
    private boolean isAnimate = true;
    private boolean isMirror = false;
    private String sendNum;

    public FiftNumberPopwindow(Context context, String num, int my_number) {
        super(context);
        this.context = context;
        this.sendNum = num;
        setBackground(R.color.transparent);
        setPopupGravity(Gravity.BOTTOM);
        this.isMirror = isMirror;
        recyclerView = findViewById(R.id.recyclerview);
        ll_view = findViewById(R.id.ll_view);
        OnDismissListener mDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(onItemClickListener!=null)
                    onItemClickListener.isDismiss();
            }
        };
        setOnDismissListener(mDismissListener);
        ll_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return false;
            }
        });
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ll_view.getLayoutParams();
//        layoutParams.leftMargin = le-((int)getContext().getResources().getDimension(R.dimen.dp_25));
//        ll_view.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(sendNum)) {
            String[] split = sendNum.split(",");
            if (split.length > 3) {
                img.add(new MakeExpansionDto(0, split[3]));
            }
            if (split.length > 2) {
                img.add(new MakeExpansionDto(0, split[2]));
            }
            if (split.length > 1) {
                img.add(new MakeExpansionDto(0, split[1]));
            }
            if (split.length > 0) {
                img.add(new MakeExpansionDto(0, split[0]));
            }
        } else {
            img.add(new MakeExpansionDto(0, "1"));
            img.add(new MakeExpansionDto(0, "10"));
            img.add(new MakeExpansionDto(0, "66"));
            img.add(new MakeExpansionDto(0, "520"));
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

//        img.add(R.drawable.ic_effect);
//        img.add(R.drawable.ic_gesture);
//        img.add(R.drawable.ic_share);
//        img.add(isMirror?R.drawable.ic_mirror_close:R.drawable.ic_mirror_open);
//        img.add(R.drawable.live_ic_close_animate);
        initData(my_number);
    }

    private void initData(int my_number) {
        listData = new ArrayList<>();
        for (int i = 0; i < img.size(); i++) {
            listData.add(img.get(i));
        }
        livePushMoreAdapter = new GiftNumberMoreAdapters(context, R.layout.live_item_girt_number, listData, my_number + "");
        recyclerView.setAdapter(livePushMoreAdapter);
        livePushMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClickListener.onItemClick(adapter, view, livePushMoreAdapter.getItem(position).getTitle());
                dismiss();
            }
        });
    }
//
//    @Override
//    protected Animation onCreateShowAnimation() {
//        return getTranslateVerticalAnimation(1f, 0, 300);
//    }
//
//    @Override
//    protected Animation onCreateDismissAnimation() {
//        return getTranslateVerticalAnimation(0, 1f, 300);
//    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_gift_number);
    }

    public interface OnItemClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, String position);
        void isDismiss();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
