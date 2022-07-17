package com.fengwo.module_flirt.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.iservice.SysConfigService;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.LocationUtils;
import com.fengwo.module_comment.utils.SPUtils1;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.BannerView.BannerView;
import com.fengwo.module_comment.widget.BannerView.OnBannerItemCliakListenr;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_flirt.Interfaces.IFlirtView;
import com.fengwo.module_flirt.Interfaces.NearByListener;
import com.fengwo.module_flirt.Interfaces.RecommendListener;
import com.fengwo.module_flirt.P.FlirtPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.RankActivity;
import com.fengwo.module_flirt.UI.activity.SearchWenBoActivity;
import com.fengwo.module_flirt.UI.certification.FlirtCertificationActivity;
import com.fengwo.module_flirt.UI.certification.PreStartWbActivity;
import com.fengwo.module_flirt.adapter.FlirtViewPagerAdapter;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.dialog.CertificationDialog;
import com.fengwo.module_flirt.dialog.FilterTablePopwindow;
import com.fengwo.module_flirt.widget.AnchorListButtonView;
import com.fengwo.module_flirt.widget.FlirtIndicatorView;
import com.fengwo.module_login.mvp.ui.activity.RealNameActivity;
import com.fengwo.module_login.utils.UserManager;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * I撩
 *
 * @Author BLCS
 * @Time 2020/3/26 10:37  i撩2.0 暂时放弃
 */

@Route(path = ArouterApi.LOGIN_FRAGMENT_FLIRT)
public class FlirtFragment extends BaseMvpFragment<IFlirtView, FlirtPresenter> implements IFlirtView {
    @BindView(R2.id.banner_flirt)
    BannerView mBanner;
    @BindView(R2.id.flv_indicator)
    FlirtIndicatorView flvIndicator;
    @BindView(R2.id.vp_flirt)
    ViewPager vpFlirt;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.ll_positioning_unopen_tip)
    LinearLayout llLocationTip;
    @BindView(R2.id.tool_bar)
    LinearLayout tool_bar;
    @BindView(R2.id.cl_flirt)
    ConstraintLayout clFlirt;
    @BindView(R2.id.iv_flirt_home_bg)
    ImageView ivFlirtHomeBg;
    @BindView(R2.id.iv_flirt_search)
    ImageView iv_flirt_search;
    @BindView(R2.id.iv_flirt_live)
    ImageView iv_flirt_live;

    @BindView(R2.id.cl_scrool)
    CoordinatorLayout clScrool;
    @BindView(R2.id.basetitle)
    FrameLayout basetitle;
    @BindView(R2.id.tv_flirt_title)
    TextView tv_flirt_title;
    @BindView(R2.id.appBar)
    AppBarLayout appBar;

    @BindView(R2.id.view_line)
    View viewLine;
    @Autowired
    SysConfigService sysConfigService;
    //轮播图数据
    private int checkPos = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private RecommendListener onlineListener, recommendListener;
    private NearByListener nearByListener;
    public String alocation = "全部";
    public String findAlocation = "全部";
    public String time;

    private AnchorListButtonView anchorListButtonView;
//    public int value1Age = -1;
//    public int value2Age = -1;
//    public int sexA = -1;
//    private int MAX_SCROLL;

    public static  final String IMPRISSON_TO_SECOND = "IMPRISSON_TO_SECOND";
    @Override
    protected FlirtPresenter initPresenter() {
        return new FlirtPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_flirt;
    }
    private FilterTablePopwindow filterFlirtPopwindow;
    @Override
    public void initUI(Bundle savedInstanceState) {
//        MAX_SCROLL = DensityUtils.dp2px(getActivity(), 100);
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(getActivity(), refreshLayout);
        if(filterFlirtPopwindow==null){//筛选标签Pop
            filterFlirtPopwindow = new FilterTablePopwindow(getContext());
            filterFlirtPopwindow.addOnClickListener((tabNames,parentId) -> {
                flvIndicator.setCheckStatus(parentId);
                if (checkPos == 0) {//推荐
                    recommendListener.getVideoDating(alocation, 1, checkPos, tabNames);
                } else {//  在线
                    onlineListener.getVideoDating(alocation, 1, checkPos,tabNames);
                }
            });
        }
        refreshLayout.setOnRefreshListener(refreshLayout -> {//刷新
            if (recommendListener != null && checkPos == 0) {//推荐
                recommendListener.onRefrsh();
                p.getLabel(checkPos, alocation);
            }
            if (onlineListener != null && checkPos == 1) {//在线
                onlineListener.onRefrsh();
                p.getLabel(checkPos, alocation);
            }
            if (nearByListener != null && checkPos == 2) nearByListener.onRefrsh();
            refreshLayout.finishRefresh();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {//加载
            if (recommendListener != null && checkPos == 0) recommendListener.onLoadMore();
            if (onlineListener != null && checkPos == 1) onlineListener.onLoadMore();
            if (nearByListener != null && checkPos == 2) nearByListener.onLoadMore();
            refreshLayout.finishLoadMore();
        });
        /*导航栏 及列表*/
        initViewPager();
        //动态添加I撩 浮窗按钮
        anchorListButtonView = new AnchorListButtonView(getContext(), getFragmentManager());
        clFlirt.addView(anchorListButtonView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        anchorListButtonView.addClickViewListener(new AnchorListButtonView.ClickViewListener() {
            @Override
            public void clickView() {
                //解决及时刷新问题 防止在列表页面停留时间过久
                p.getMyOrder();
            }
        });
        /*上滑 title变色*/
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                float alpha = Math.abs(i) / (float) totalScrollRange;
                int backgroundAlpha = (int) (alpha * 255);
                int backgroundColor = Color.argb(backgroundAlpha, 255, 255, 255);
                int bgColorBlack = Color.argb(backgroundAlpha, 27, 20, 41);
                int lineBgColor = Color.argb(backgroundAlpha, 205, 205, 205);
//                iv_flirt_search.setImageResource(alpha > 0.5 ? R.drawable.chat_ic_home_search_selected : R.drawable.chat_ic_home_search);//chat_ic_home_search_selected
                iv_flirt_live.setImageResource(alpha > 0.5 ? R.drawable.ic_flirt_wenbo_white : R.drawable.ic_flirt_wenbo);
                tv_flirt_title.setTextColor(alpha > 0.5 ? ContextCompat.getColor(getContext(), com.fengwo.module_chat.R.color.text_33) : ContextCompat.getColor(getContext(), com.fengwo.module_chat.R.color.text_white));
                viewLine.setBackgroundColor(DarkUtil.isDarkTheme(getActivity()) ? bgColorBlack : lineBgColor);

                tool_bar.setBackgroundColor(DarkUtil.isDarkTheme(getActivity()) ? bgColorBlack : backgroundColor);
            }
        });
        /*接口请求*/
        initData();

    }

    private void initData() {
        /*获取标签*/
        p.getLabel(0, alocation);
        /*获取广告*/
        p.getBanner("1,20", 1);
        /* 获取印象值刷新时间   印象值： 聊天时间与印象值的的比例 有后台系统配置 需要动态获取比例*/
        sysConfigService.getSysConfig(IMPRISSON_TO_SECOND, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()){
                    try {
                        JSONObject jsonObject = new JSONObject(data.data.toString());
                        time = jsonObject.getString(IMPRISSON_TO_SECOND);
                        SPUtils1.put(getContext(),"Flirt_Time",time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg) ) toastTip(msg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        p.getMyOrder();//及时刷新
        if (DarkUtil.isDarkTheme(getActivity())){//暗黑模式
            ivFlirtHomeBg.setVisibility(View.INVISIBLE);
        }
    }

    private void initViewPager() {
        flvIndicator.bindViewpage(vpFlirt);
        //指示器 点击事件
        flvIndicator.addClickListener(new FlirtIndicatorView.OnClickListener() {

            @Override
            public void clickVideoSelect() { // 推荐
                checkPos = 0;
                recommendListener.getVideoDating(alocation, 1, checkPos, "");
                flvIndicator.setLabelIndex(0);
                p.getLabel(checkPos, alocation);
            }

            @Override
            public void clickAppointment() {// 在线交友
                checkPos = 1;
                flvIndicator.setLabelIndex(0);
                onlineListener.getVideoDating(alocation, 1, checkPos, "");
                p.getLabel(checkPos, alocation);
            }

            @Override
            public void clickFind() {
                checkPos = 2;
                MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
                    @Override
                    public void onLocationSuccess(AMapLocation location) {
                        llLocationTip.setVisibility(View.GONE);
                        p.upLoading(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), location.getCity());
                        if (nearByListener != null)
                            nearByListener.getPeopleNearby(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), "", "", 1, "0",findAlocation);
                    }

                    @Override
                    public void onLocationFailure(String msg) {
                        llLocationTip.setVisibility(View.VISIBLE);
                        p.upLoading("0", "0", "");
                    }
                });
            }

            @Override
            public void clickSelect() {//TODO 筛选
                filterFlirtPopwindow.showPopupWindow();
            }

            @Override
            public void selectAddress() {
                LocationUtils.showLocationCity(getContext(), new LocationUtils.OnSelectedListener() {
                    @Override
                    public void onSelected(String location) {
                        String city = location;
                        if (location.length() > 3) {
                            city = location.substring(0, 3) + "...";
                        } else if (location.endsWith("市") || location.endsWith("区")) {
                            city = location.substring(0, location.length() - 1);
                        }
                        //显示给用户看的城市名称
                        flvIndicator.setLocation(city);
                        //传给后台的城市名称
                        if (location.equals("全部")){
                            findAlocation = location;
                        }else {
                            String newLocation = location.substring(0, location.length() - 1);
                            findAlocation = newLocation;
                        }
//                        if (onlineListener != null)
//                            onlineListener.getVideoDating(alocation, 1, checkPos, "");
//                        if (recommendListener != null){
//                            recommendListener.getVideoDating(alocation, 1, checkPos, "");
//                        }
//                        p.getLabel(checkPos, alocation);
                        if (nearByListener != null)
                            nearByListener.getPeopleNearby(MapLocationUtil.getInstance().getLongitude(), MapLocationUtil.getInstance().getLatitude(), "", "", 1, "0",findAlocation);
                    }
                });
            }

            @Override
            public void clickLabel(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                //TODO 点击标签
                CerTagBean bean = (CerTagBean) baseQuickAdapter.getData().get(i);
                if (checkPos == 0) {//推荐
                    recommendListener.getVideoDating(alocation, 1, checkPos, bean.getTagName());
                } else {//  在线
                    onlineListener.getVideoDating(alocation, 1, checkPos, bean.getTagName());
                }
            }
        });
        initFragment();
        //包含推荐与 在线交友
        FlirtViewPagerAdapter mAdapter = new FlirtViewPagerAdapter(getActivity().getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments);
        vpFlirt.setAdapter(mAdapter);

//        //来撩无数据 则切换到约聊列表
//        RxBus.get().toObservable(VideoDatingEvent.class).compose(bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(event -> {
//                    vpFlirt.setCurrentItem(1);
//                });

    }

    /**
     * 推荐 在线 发现 列表UI
     */
    private void initFragment() {
        mFragments.clear();
        RecommendFragment recommendFragment = RecommendFragment.getInstance(0);//推荐
        RecommendFragment onlineFragment = RecommendFragment.getInstance(1);//在线
        NearbyFragment nearbyFragment = new NearbyFragment();
        onlineListener = onlineFragment.getListener();
        recommendListener = recommendFragment.getListener();
        nearByListener = nearbyFragment.getListener();
        mFragments.add(recommendFragment);
        mFragments.add(onlineFragment);
        mFragments.add(nearbyFragment);
    }

    @OnClick({R2.id.tool_bar, R2.id.iv_flirt_search, R2.id.iv_flirt_live, R2.id.tv_location_sure})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.tool_bar) {//双击刷新
            refreshLayout.autoRefresh();
        } else if (id == R.id.iv_flirt_search) {//搜索
            startActivity(SearchWenBoActivity.class);
        } else if (id == R.id.iv_flirt_live) {
            if (UserManager.getInstance().getUser().isWenboRole()) {//是I撩主播
                //判断资料是否完善
                if (UserManager.getInstance().getUser().isWeboSet || UserManager.getInstance().getWenboCer()) {
                    Intent intent = new Intent(getContext(), PreStartWbActivity.class);
                    getContext().startActivity(intent);
                } else {
                    showIsFlirtDialog();
                }
            } else if (UserManager.getInstance().getUser().isWenboUser()) {//是I撩用户
                showIsRealNameDialog();
            } else {
                toastTip("暂无权限");
            }
        } else if (id == R.id.tv_location_sure) {//知道了
            llLocationTip.setVisibility(View.GONE);
        }
    }

    /**
     * 开播判断逻辑
     */
    private void showIsRealNameDialog() {
        String msg = "";
        switch (UserManager.getInstance().getUser().getMyIsCardStatus()) {
            case UserInfo.IDCARD_STATUS_NULL:
                msg = "该账户未进行实名认证";
                break;
            case UserInfo.IDCARD_STATUS_ING:
                msg = "该账户实名认证中";
                break;
            case UserInfo.IDCARD_STATUS_NO:
                msg = "该账户实名认证不通过";
                break;
        }
        CustomerDialog dialog = new CustomerDialog.Builder(getActivity()).setTitle("提示").setMsg(msg)
                .setNegativeButton("取消", () -> {

                })
                .setPositiveButton("确定", () -> {
                    if (UserManager.getInstance().getUser().getMyIsCardStatus() == UserInfo.IDCARD_STATUS_NULL)
                        RealNameActivity.start(getActivity(), UserManager.getInstance().getUser().getMyIsCardStatus());
                }).create();
        dialog.show();
        if (UserManager.getInstance().getUser().getMyIsCardStatus() != UserInfo.IDCARD_STATUS_NULL) {
            dialog.hideCancle();
        }
    }

    /**
     * 个人资料不完善 弹窗
     */
    private void showIsFlirtDialog() {
        CertificationDialog certificationDialog = new CertificationDialog(getActivity(), "个人资料不完善，请先完善信息", "完善资料",
                new CertificationDialog.onPositiveListener() {
                    @Override
                    public void onPositive() {
                        Intent intent = new Intent(getActivity(), FlirtCertificationActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
        certificationDialog.show();
    }

    @Override
    public void setBannerData(ArrayList<AdvertiseBean> records) {//接口返回  广告数据
        if (records == null || records.size() == 0) return;
        mBanner.setBanner(records);
        mBanner.setOnBannerItemCliakListenr(new OnBannerItemCliakListenr() {
            @Override
            public void jump(String action, String id, int pos) {
                String advertName = records.get(pos).getAdvertName();
                if (advertName.equals("同城之星")) {
                    startActivity(new Intent(getContext(), RankActivity.class));
                    return;
                }
            }
        });
        mBanner.startAutoScroll();
    }

    @Override
    public void getLabelSuccess(List<CerTagBean> data) {//接口返回  标签数据
        filterFlirtPopwindow.setLabel(data);
        ArrayList<CerTagBean> cerTagBeans = new ArrayList<>(data);
        CerTagBean allBean = new CerTagBean();
        allBean.setTagName("全部");
        cerTagBeans.add(0, allBean);
        flvIndicator.setLabelData(cerTagBeans);
    }

    @Override
    public void setMyOrderList(List<MyOrderDto> data) {//刷新点单浮窗数据
        anchorListButtonView.setVisibility(data != null && data.size() > 0 ? View.VISIBLE : View.GONE);
        anchorListButtonView.setHeader(data != null && data.size() > 0 ? data.get(0).getHeadImg() : null);
        anchorListButtonView.setOrderList(data, "0");
    }

    private boolean isFirstGif = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstGif && isVisibleToUser) {
            isFirstGif = false;
            ImageLoader.loadGif1OneTime(iv_flirt_live, R.drawable.ic_flirt_wenbo_gif);
        }
    }
}
