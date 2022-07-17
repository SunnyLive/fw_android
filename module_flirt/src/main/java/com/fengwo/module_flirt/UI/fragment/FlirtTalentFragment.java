package com.fengwo.module_flirt.UI.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.DrawableUtil;
import com.fengwo.module_comment.utils.DrawableUtilHtml;
import com.fengwo.module_flirt.Interfaces.SelectListenet;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_flirt.Interfaces.IFlirtTalentView;
import com.fengwo.module_flirt.P.FlirtTalentPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.UI.activity.SearchWenBoActivity;
import com.fengwo.module_flirt.adapter.FindListAdapter;
import com.fengwo.module_flirt.adapter.OnLineTalentAdapter;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.dialog.FlirtGdPopwindow;
import com.fengwo.module_flirt.utlis.CommonUtils;
import com.fengwo.module_flirt.widget.AnchorListButtonView;
import com.fengwo.module_flirt.widget.OptionPopwindow;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 达人列表
 */
public class FlirtTalentFragment extends BaseMvpFragment<IFlirtTalentView, FlirtTalentPresenter> implements IFlirtTalentView {

    /**
     * 发布请求码
     */
    private final int REQUEST_POST_TREND = 100;

    @BindView(R2.id.tabLayout)
    ScrollIndicatorView tabLayout;
    @BindView(R2.id.vp_talent)
    ViewPager vpTalent;
    @BindView(R2.id.cl_flirt)
    ConstraintLayout clFlirt;

    @BindView(R2.id.tv_become)
    TextView tvBecome;
    @BindView(R2.id.im_vip)
    ImageView imVip;

    @BindView(R2.id.iv_flirt_search)
    ImageView ivFlirtSearch;
    @BindView(R2.id.tv_add)
    View tv_add;
    @BindView(R2.id.iv_flirt_gengduo)
    View iv_flirt_gengduo;

    @BindView(R2.id.btn_filter)
    View btn_filter;
    @BindView(R2.id.btn_label)
    View btnLabel;


    private String[] titles = new String[]{"在线的TA", "发现TA"};
    private int page = 1;
    private String tagName = "";
    private OnLineTalentAdapter onLineTalentAdapter;
    private FindListAdapter findListAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    public String value1Age = "";
    public String value2Age = "";
    public int sexA = 0;
    private SelectListenet listener;
    private AnchorListButtonView anchorListButtonView;
    private FindListFragment findListFragment;
    private OnLineFragment onLineFragment;
    /*当前tab*/
    private int currentItem;

    public void setIOnclickListener(SelectListenet listener) {
        this.listener = listener;
    }

    @Override
    protected FlirtTalentPresenter initPresenter() {
        return new FlirtTalentPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_flirt_talent;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        onLineFragment = new OnLineFragment();
        fragments.add(onLineFragment);
        findListFragment = new FindListFragment();
        fragments.add(findListFragment);
        setScrollIndicator();
//
        anchorListButtonView = new AnchorListButtonView(getContext(), getFragmentManager());
        anchorListButtonView.bringToFront();
        clFlirt.addView(anchorListButtonView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        anchorListButtonView.addClickViewListener(new AnchorListButtonView.ClickViewListener() {
            @Override
            public void clickView() {
                //解决及时刷新问题 防止在列表页面停留时间过久
                p.getMyOrder();
            }
        });
        anchorListButtonView.findFocus();
        p.getMyOrder();

        if (UserManager.getInstance().getUser().isWenboRole()) {
            tvBecome.setText("我是达人");
            imVip.setBackgroundResource(R.drawable.ic_talent_yx);
        } else {
            tvBecome.setText("成为达人");
            imVip.setBackgroundResource(R.drawable.ic_talent_back);
        }

        new DrawableUtil(tvBecome, new DrawableUtil.OnDrawableListener() {
            @Override
            public void onLeft(View v, Drawable left) {

            }

            @Override
            public void onRight(View v, Drawable right) {
                CommonUtils.showOperation(getContext());
            }
        });
    }

    public void resume() {
        if (null != tvBecome) {


            if (UserManager.getInstance().getUser().wenboAnchorStatus == 1) {
                tvBecome.setText("我是达人");
                imVip.setBackgroundResource(R.drawable.ic_talent_yx);
            } else {
                tvBecome.setText("成为达人");
                imVip.setBackgroundResource(R.drawable.ic_talent_back);
            }
        }
//        ImmersionBar.with(this)
//                .statusBarDarkFont(true, 0.2f)
//                .navigationBarDarkIcon(true, 0.2f)
//                .navigationBarColor(com.fengwo.module_comment.R.color.black)
//                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//                .init();
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarDarkIcon(true, 0.2f)
                .navigationBarColor(com.fengwo.module_comment.R.color.white)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                .init();
    }

    @Override
    public void onResume() {
        super.onResume();
        p.getMyOrder();
    }


    @Override
    public boolean getUserVisibleHint() {
        //如果是达人  就关闭 运营提示
        if (UserManager.getInstance().getUser().wenboAnchorStatus == 1) {
            tvBecome.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        return super.getUserVisibleHint();
    }

    private void setScrollIndicator() {
        vpTalent.setOffscreenPageLimit(fragments.size());
        int selectColor = getResources().getColor(R.color.homt_tab_selsct);
        int unSelectColor;
        if (DarkUtil.isDarkTheme(getActivity())) {
            unSelectColor = getResources().getColor(R.color.text_33);
        } else {
            unSelectColor = getResources().getColor(R.color.text_33);
        }
        tabLayout.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(18, 16));
       tabLayout.setSplitAuto(true);
        ColorBar colorBar = new ColorBar(getActivity(), selectColor, 3);
        colorBar.setWidth(48);
        tabLayout.setScrollBar(colorBar);
        //    tabLayout.setScrollBar(new LayoutBar(getActivity(), R.layout.layout_contact_home_slidebar, ScrollBar.Gravity.CENTENT_BACKGROUND));
        IndicatorViewPager pager = new IndicatorViewPager(tabLayout, vpTalent);
        pager.setClickIndicatorAnim(false);
        pager.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                if (position == 0) {
                    onLineFragment.scrollToTop();
                } else {
                    findListFragment.scrollToTop();
                }
                return false;
            }
        });
        pager.setOnIndicatorPageChangeListener((preItem, currentItem) -> {
            if (preItem >= 0) {
                TextView unselectedTextView = (TextView) tabLayout.getItemView(preItem);
                if (unselectedTextView != null) {
                    unselectedTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
            TextView currentTextView = (TextView) tabLayout.getItemView(currentItem);
            if (currentTextView != null)
                currentTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            this.currentItem = currentItem;
            if (currentItem == 0) {
                tv_add.setVisibility(View.GONE);
                btn_filter.setVisibility(View.VISIBLE);
                btnLabel.setVisibility(View.VISIBLE);
                iv_flirt_gengduo.setVisibility(View.GONE);
                ivFlirtSearch.setVisibility(View.VISIBLE);
            } else {
                tv_add.setVisibility(View.VISIBLE);
                btn_filter.setVisibility(View.GONE);
                btnLabel.setVisibility(View.GONE);
                iv_flirt_gengduo.setVisibility(View.VISIBLE);
                ivFlirtSearch.setVisibility(View.GONE);
            }
        });
        pager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public View getViewForTab(int position, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_tab, container, false);
                }
                TextView textView = (TextView) convertView;
                textView.setText(titles[position]);
                if (position == 0) {
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                int width = getTextWidth(textView);
                int padding = DensityUtils.dp2px(getContext(), 20);
                //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
                //textView.setWidth((int) (width * 1.2f) + padding);
                textView.setWidth((int) width + padding);
                return convertView;
            }

            @Override
            public Fragment getFragmentForPage(int position) {
                return fragments.get(position);
            }

            private int getTextWidth(TextView textView) {
                if (textView == null) {
                    return 0;
                }
                Rect bounds = new Rect();
                String text = textView.getText().toString();
                Paint paint = textView.getPaint();
                paint.getTextBounds(text, 0, text.length(), bounds);
                return bounds.left + bounds.width();
            }
        });
        // 设置第一个Tab高亮
        TextView unselectedTextView = (TextView) tabLayout.getItemView(0);
        if (unselectedTextView != null)
            unselectedTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    OptionPopwindow filterFlirtPopwindow;
    /**
     * 筛选
     */
    private void filter() {
        if(null==filterFlirtPopwindow){
             filterFlirtPopwindow = new OptionPopwindow(getContext(), sexA);
        }

        filterFlirtPopwindow.addOnClickListener((sex, minAge, maxAge, city) -> {
            if (currentItem == 0) {
                onLineFragment.filter(city, String.valueOf(maxAge), String.valueOf(minAge), String.valueOf(sex));
            } else {
                findListFragment.filter(city, String.valueOf(maxAge), String.valueOf(minAge), String.valueOf(sex));
            }
            filterFlirtPopwindow.dismiss();
        });
        filterFlirtPopwindow.showPopupWindow();
    }

    @OnClick({R2.id.tv_become, R2.id.btn_filter, R2.id.btn_label, R2.id.view_line, R2.id.iv_flirt_search, R2.id.tv_add, R2.id.iv_flirt_gengduo})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_become) {/*在线达人 i 撩 开播流程*/
            CommonUtils.playing(getContext(), 2);
        } else if (id == R.id.btn_filter) {/*搜索*/
            filter();
        } else if (id == R.id.btn_label) {/*todo 标签*/
            if (null != listener) {
                listener.select(0);
            }
        } else if (id == R.id.view_line) {

        } else if (id == R.id.iv_flirt_search) {
            startActivity(SearchWenBoActivity.class);
        } else if (id == R.id.tv_add) {//添加动态
            //这里forResult只能在Activity中接收到onActivityResult
//            ARouter.getInstance().build(ArouterApi.CHAT_POST_TREND)
//                    .navigation(getActivity(), 100);

            Postcard postcard = ARouter.getInstance()
                    .build(ArouterApi.CHAT_POST_TREND);
            LogisticsCenter.completion(postcard);
            Intent intent = new Intent(getActivity(), postcard.getDestination());
            intent.putExtras(postcard.getExtras());
            startActivityForResult(intent, REQUEST_POST_TREND);
        } else if (id == R.id.iv_flirt_gengduo) {
            FlirtGdPopwindow flirtGdPopwindow = new FlirtGdPopwindow(getActivity());
            flirtGdPopwindow.setOnItemClickListener(new FlirtGdPopwindow.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0) {//筛选
                        filter();
                    } else {//搜索
                        startActivity(SearchWenBoActivity.class);
                    }
                    flirtGdPopwindow.dismiss();
                }

                @Override
                public void isDismiss() {

                }
            });
            flirtGdPopwindow.showPopupWindow();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POST_TREND && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String cardDetail = data.getStringExtra("CardDetail");
                if (!TextUtils.isEmpty(cardDetail)) {
                    FindListDto findListDot = new Gson().fromJson(cardDetail, FindListDto.class);
                    if (findListDot != null) {
                        UserInfo u = UserManager.getInstance().getUser();
                        findListFragment.insertSelfFind(findListDot);
                    }
                }
            }
        }
    }

    @Override
    public void setOnLineList(ArrayList<CityHost> records) {

    }

    @Override
    public void getRequestFindListSuccess(BaseListDto<FindListDto> data) {

    }

    @Override
    public void cardLikeSuccess(int id, int position) {

    }

    @Override
    public void getShareUrlSuccess(String url, int type, String imgUrl, String content) {

    }

    @Override
    public void onReceiveFlirtData(boolean isRefresh, List<CityHost> onlineFlirtData) {

    }

    @Override
    public void setMyOrderList(List<MyOrderDto> data) {
        anchorListButtonView.setVisibility(data != null && data.size() > 0 ? View.VISIBLE : View.GONE);
        anchorListButtonView.setHeader(data != null && data.size() > 0 ? data.get(0).getHeadImg() : null);
        anchorListButtonView.setOrderList(data, "0");
    }

    @Override
    public void onGetFindDetail(int id, FindDetailBean detail, int position) {

    }
}
