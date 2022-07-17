//
//
//package com.fengwo.module_vedio.mvp.ui.fragment;
//
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.fengwo.module_comment.ArouterApi;
//import com.fengwo.module_comment.base.BaseMvpFragment;
//import com.fengwo.module_comment.event.ToTopEvent;
//import com.fengwo.module_comment.event.UndateUnReadMessageEvent;
//import com.fengwo.module_comment.iservice.UserProviderService;
//import com.fengwo.module_comment.utils.DensityUtils;
//import com.fengwo.module_comment.utils.DoubleClickListener;
//import com.fengwo.module_comment.utils.RxBus;
//import com.fengwo.module_comment.utils.ScreenUtils;
//import com.fengwo.module_comment.widget.BannerView.BannerView;
//import com.fengwo.module_vedio.R;
//import com.fengwo.module_vedio.R2;
//import com.fengwo.module_vedio.mvp.dto.AdvertiseBean;
//import com.fengwo.module_vedio.mvp.dto.VideoHomeCategoryDto;
//import com.fengwo.module_vedio.mvp.presenter.VideoHomePresenter;
//import com.fengwo.module_vedio.mvp.ui.activity.SearchActivity;
//import com.fengwo.module_vedio.mvp.ui.iview.IVideoHomeView;
//import com.google.android.material.appbar.AppBarLayout;
//import com.shizhefei.view.indicator.IndicatorViewPager;
//import com.shizhefei.view.indicator.ScrollIndicatorView;
//import com.shizhefei.view.indicator.slidebar.DrawableBar;
//import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//
///**
// * F
// *
// * @author GuiLianL
// * @intro
// * @date 2019/11/29
// */
//@Route(path = ArouterApi.VEDIO_FRAGMENT_HOME)
//public class VideoHomeFragment extends BaseMvpFragment<IVideoHomeView, VideoHomePresenter> implements IVideoHomeView {
//    //    @BindView(R2.id.titleBar)
////    AppTitleBar titleBar;
//    @BindView(R2.id.basetitle)
//    View topView;
//    @BindView(R2.id.iv_bg_video_home)
//    ImageView iv_bg_video_home;
//    //    @BindView(R2.id.iv_bg_video_home_cover)
////    ImageView iv_bg_video_home_cover;
//    @BindView(R2.id.appbarlayout)
//    AppBarLayout appbarlayout;
//    @BindView(R2.id.iv_search)
//    ImageView ivSearch;
//    @BindView(R2.id.banner)
//    BannerView bannerView;
//    @BindView(R2.id.tabLayout)
//    ScrollIndicatorView indicatorView;
//    @BindView(R2.id.vp_content)
//    ViewPager viewPager;
//    @BindView(R2.id.coordinator)
//    CoordinatorLayout coordinatorLayout;
//    @BindView(R2.id.tv_badge)
//    TextView tvBadge;
//    @BindView(R2.id.fl_chat_home_message)
//    View msgView;
//
//    private ArrayList<VideoHomeCategoryDto> titles;
//    private ArrayList<Fragment> fragmentList = new ArrayList<>();
//    private int MAX_SCROLL;
//
//    @Autowired
//    UserProviderService userProviderService;
//
//    @Override
//    protected VideoHomePresenter initPresenter() {
//        return new VideoHomePresenter();
//    }
//
//    @Override
//    protected int setContentView() {
//        return R.layout.fragment_video_home;
//    }
//
//    @Override
//    public void initUI(Bundle savedInstanceState) {
//        MAX_SCROLL = DensityUtils.dp2px(getActivity(), 100);
//        ivSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SearchActivity.class);
//            }
//        });
////        ViewGroup.LayoutParams lp = bannerView.getLayoutParams();
////        lp.width = ScreenUtils.getScreenWidth(getContext());
////        lp.height = (int) (lp.width / 2.23);
////        bannerView.setLayoutParams(lp);
//
//        p.getCategory();
//        p.getBannerAd();
//
//        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                int dy = Math.abs(i);
//                topView.setSelected(dy > 10);
//                float alpha = Math.min(MAX_SCROLL, dy) / (float) MAX_SCROLL;
//                int backgroundAlpha = (int) (alpha * 255);
//                int backgroundColor = Color.argb(backgroundAlpha, 255, 255, 255);
//                topView.setBackgroundColor(backgroundColor);
//                coordinatorLayout.setBackgroundColor(backgroundColor);
//            }
//        });
//
//        // 未读消息数
//        RxBus.get().toObservable(UndateUnReadMessageEvent.class)
//                .compose(bindToLifecycle()).subscribe(object -> {
//                    showUnReadMessage(object.num);
//                },
//                Throwable::printStackTrace);
//        showUnReadMessage(UndateUnReadMessageEvent.unReadMessageCount);
//        msgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int userId = userProviderService.getUserInfo().id;
//                ARouter.getInstance().build(ArouterApi.MESSAGE_LIST).withString("uid", String.valueOf(userId)).navigation();
//            }
//        });
//
//        topView.setOnClickListener(new DoubleClickListener() {
//            @Override
//            public void onDoubleClick(View v) {
//                RxBus.get().post(new ToTopEvent());
//            }
//        });
//
//    }
//
//    /**
//     * 显示未读消息数
//     */
//    private void showUnReadMessage(int count) {
//        if (tvBadge == null) return;
//        tvBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
//        tvBadge.setText(count > 99 ? "99+" : String.valueOf(count));
//    }
//
//    @Override
//    public void setCategory(ArrayList<VideoHomeCategoryDto> records) {
//        fragmentList.clear();
//        fragmentList.add(VideoHomeRecommendFragment.newInstance(0));
//        fragmentList.add(new VideoHomeShortFragment());
//        fragmentList.add(new VideoHomeSpecialFragment());
//        for (VideoHomeCategoryDto model : records) {
//            fragmentList.add(VideoHomeRecommendFragment.newInstance(model.id));
//        }
//        records.add(0, new VideoHomeCategoryDto(-1, "推荐"));
//        records.add(1, new VideoHomeCategoryDto(-1, "短片"));
//        records.add(2, new VideoHomeCategoryDto(-1, "专题"));
//        titles = records;
//        initIndicatorView();
//    }
//
//    @Override
//    public void setBannerData(ArrayList<AdvertiseBean> records) {
//        bannerView.setBanner(records);
//        bannerView.startAutoScroll();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        bannerView.stopAutoScroll();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        bannerView.startAutoScroll();
//
//    }
//
//    private void initIndicatorView() {
//        int selectColor = getResources().getColor(R.color.text_33);
//        int unSelectColor = getResources().getColor(R.color.text_99);
//        indicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(16, 14));
//        indicatorView.setSplitAuto(false);
//        IndicatorViewPager pager = new IndicatorViewPager(indicatorView, viewPager);
//        pager.setClickIndicatorAnim(false);
//        pager.setIndicatorScrollBar(new DrawableBar(getContext(), R.drawable.indicator_video_home));
//        pager.setPageOffscreenLimit(titles.size());
//        pager.setOnIndicatorPageChangeListener((preItem, currentItem) -> {
//            if (preItem >= 0) {
//                TextView unselectedTextView = (TextView) indicatorView.getItemView(preItem);
//                if (unselectedTextView != null)
//                    unselectedTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//            }
//            TextView currentTextView = (TextView) indicatorView.getItemView(currentItem);
//            if (currentTextView != null)
//                currentTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//        });
//        pager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public int getCount() {
//                return fragmentList.size();
//            }
//
//            @Override
//            public View getViewForTab(int position, View convertView, ViewGroup container) {
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_item_home_tab, container, false);
//                }
//                TextView textView = (TextView) convertView;
//                textView.setText(titles.get(position).name);
//                if (position == 0) {
//                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                }
//                int width = getTextWidth(textView);
//                int padding = DensityUtils.dp2px(getContext(), 20);
//                //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
//                textView.setWidth((int) (width * 1.2f) + padding);
//                return convertView;
//            }
//
//            @Override
//            public Fragment getFragmentForPage(int position) {
//                return fragmentList.get(position);
//            }
//
//            private int getTextWidth(TextView textView) {
//                if (textView == null) {
//                    return 0;
//                }
//                Rect bounds = new Rect();
//                String text = textView.getText().toString();
//                Paint paint = textView.getPaint();
//                paint.getTextBounds(text, 0, text.length(), bounds);
//                return bounds.left + bounds.width();
//            }
//        });
//    }
//
//}