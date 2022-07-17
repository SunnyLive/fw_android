package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.event.ToTopEvent;
import com.fengwo.module_comment.helper.GlobalMsgHelper;
import com.fengwo.module_comment.iservice.GetUserInfoByIdService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.DoubleClickListener;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.H5addressBean;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.fengwo.module_live_vedio.mvp.ui.activity.SearchActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.TeenagerModeMainActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.TeenagerModeSettingActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.StartLiveActivity;
import com.fengwo.module_live_vedio.mvp.ui.dialog.TeenagerModeTipsPopup;
import com.fengwo.module_live_vedio.mvp.ui.pop.ActTipsPop;
import com.fengwo.module_live_vedio.mvp.ui.pop.ActTipsPopnewyear;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.BasePopupWindow;

@Route(path = ArouterApi.LIVE_FRAGMENT_HOME)
public class LiveHomeFragment extends BaseMvpFragment {

    private List<String> titles;

    @Autowired
    UserProviderService userProviderService;
    @Autowired
    GetUserInfoByIdService getUserInfoByIdService;

    @BindView(R2.id.tabLayout)
    ScrollIndicatorView tabLayout;
    @BindView(R2.id.vp)
    ViewPager vp;

    @BindView(R2.id.basetitle)
    View basetitle;
    @BindView(R2.id.iv_live_home_bg)
    ImageView ivLiveHomeBg;
    @BindView(R2.id.btn_live)
    ImageView btnLive;

    @BindView(R2.id.ll_shuaxin)
    LinearLayout ll_shuaxin;
    @BindView(R2.id.tv_sx)
    TextView tv_sx;

    private List<Fragment> fragmentList;
    private boolean isFirst = true;
    private Handler handler;

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_home;
    }

    private static final String TAG = "LiveHomeFragment";
    private static final String SP_KEY_TEENAGER_TIME = "sp_key_teenager_time_";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        handler = new Handler();
        getMenu();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (DarkUtil.isDarkTheme(getActivity())) {
            ivLiveHomeBg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        basetitle.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                RxBus.get().post(new ToTopEvent());
            }
        });

        try {
            getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getActivity().getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    getAddress();
                    //提示是否需要开启青少年模式
                    showTeenagerModeTips();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_sx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMenu();
            }
        });
        if (!CommentUtils.isNetworkConnected(getActivity())) {
            ll_shuaxin.setVisibility(View.VISIBLE);
            vp.setVisibility(View.GONE);

        }
    }

    /**
     * 提示是否需要开启青少年模式
     */
    private void showTeenagerModeTips() {
        if (isFastClick()) {
            return;
        }
        UserInfo userInfo = userProviderService.getUserInfo();
        if (TextUtils.equals(userInfo.teenagerMode, Constants.TEENAGER_MODE_ENABLE)) {
            startActivity(TeenagerModeMainActivity.class);
            return;
        }
        Long updateTime = (Long) SPUtils1.get(requireContext(), SP_KEY_TEENAGER_TIME + userInfo.id, 0L);
        //如果超过一小时
        if (updateTime == null || System.currentTimeMillis() - updateTime > 1000 * 60 * 60 * 24) {
            new TeenagerModeTipsPopup(requireContext(), () -> {
                if (isFastClick()) {
                    return;
                }
                startActivity(TeenagerModeSettingActivity.class);
            }).showPopupWindow();
            SPUtils1.put(requireContext(), SP_KEY_TEENAGER_TIME + userInfo.id, System.currentTimeMillis());
        }
    }

    private void getAddress() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getH5Address()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<H5addressBean>>() {
                    @Override
                    public void _onNext(HttpResult<H5addressBean> data) {
                        if (data.isSuccess())
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (data.data.getStatus().equals("1")) {
                                        long lastTime = (long) SPUtils1.get(getActivity(), "home_noshowtoday", 0L);
                                        if ( !TimeUtils.isToday(new Date(lastTime))) {
                                            ActTipsPop actTipsPop = new ActTipsPop(getActivity(), data.data);
                                            actTipsPop.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                                                @Override
                                                public void onDismiss() {
                                                    SPUtils1.put(getActivity(), "home_noshowtoday", System.currentTimeMillis());
                                                    //元旦活动 只用一次
                                                    if (null != data.data.getExchangeActivityPropInfo()) {
                                                        ActTipsPopnewyear actTipsPop = new ActTipsPopnewyear(getActivity(), data.data);
                                                        actTipsPop.showPopupWindow();
                                                    }
                                                }
                                            });
                                            actTipsPop.showPopupWindow();
                                        }

//                                    long lastTime = (long0) SPUtils1.get(getActivity(), "home_noshowtoday", 0L);
//                                    if (data.data.status.equals("1") &&  !TimeUtils.isToday(new Date(lastTime))) {
//                                    }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private boolean isFirstGif = true;

    @SuppressLint("CheckResult")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isFirstGif && isVisibleToUser) {
//            isFirstGif = false;
//            Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(aLong -> {
//                        ImageLoader.loadGif1OneTime(btnLive, R.drawable.ic_home_living_gif);
//                    }, Throwable::printStackTrace);
//        }
    }

    private void getMenu() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getZhuboMenu(1).compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<ZhuboMenuDto>>(this) {
                    @Override
                    public void _onNext(BaseListDto<ZhuboMenuDto> data) {
                        if (null == data) {
                            ll_shuaxin.setVisibility(View.VISIBLE);
                            vp.setVisibility(View.GONE);
                            return;
                        }
                        ll_shuaxin.setVisibility(View.GONE);
                        vp.setVisibility(View.VISIBLE);
                        List<ZhuboMenuDto> list = data.records;
                        ZhuboMenuDto hot = new ZhuboMenuDto();
                        hot.id = -1;
                        hot.name = "热门";
                        titles.add(hot.name);
                        fragmentList.add(LiveHomeRecommentFragment1.newInstance(hot.id));
                        for (int i = 0; i < list.size(); i++) {
                            ZhuboMenuDto menu = list.get(i);
                            titles.add(menu.name);
                            fragmentList.add(LiveHomeTabFragment.newInstance(menu.id, menu.name));
                        }
                        setScrollIndicator();
                    }

                    @Override
                    public void _onError(String msg) {
                        ll_shuaxin.setVisibility(View.VISIBLE);
                        vp.setVisibility(View.GONE);
                    }
                });
    }

    private void setScrollIndicator() {
        vp.setOffscreenPageLimit(fragmentList.size());
        int selectColor = getResources().getColor(R.color.text_white_arr);
        int unSelectColor;
        if (DarkUtil.isDarkTheme(getActivity())) {
            unSelectColor = getResources().getColor(R.color.alpha_40_white);
        } else {
            unSelectColor = getResources().getColor(R.color.alpha_40_white);
        }
        tabLayout.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(17, 14));
        tabLayout.setSplitAuto(false);
        ColorBar colorBar = new ColorBar(getActivity(), Color.WHITE, 5);
        colorBar.setWidth(48);
        tabLayout.setScrollBar(colorBar);
        IndicatorViewPager pager = new IndicatorViewPager(tabLayout, vp);
        pager.setClickIndicatorAnim(false);
        pager.setOnIndicatorPageChangeListener((preItem, currentItem) -> {
            if (preItem >= 0) {
                TextView unselectedTextView = (TextView) tabLayout.getItemView(preItem);
                if (unselectedTextView != null)
                    unselectedTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            TextView currentTextView = (TextView) tabLayout.getItemView(currentItem);
            if (currentTextView != null)
                currentTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        });
        pager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public View getViewForTab(int position, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_tab, container, false);
                }
                TextView textView = (TextView) convertView;
                textView.setText(titles.get(position));
                if (position == 0) {
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                int width = getTextWidth(textView);
                int padding = DensityUtils.dp2px(getContext(), 20);
                //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
                textView.setWidth((int) (width * 1.2f) + padding);
                return convertView;
            }

            @Override
            public Fragment getFragmentForPage(int position) {
                return fragmentList.get(position);
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

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @SuppressLint("CheckResult")
    @OnClick({R2.id.btn_tosearch, R2.id.btn_live})
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.btn_tosearch) {
            startActivity(SearchActivity.class);
        } else if (id == R.id.btn_live) {  // 开启直播
            if (userProviderService.getUserInfo().isReanName()) {
                if (!userProviderService.getUserInfo().isFace()) {
                    showFaceCheckDialog(userProviderService.getUserInfo());
                } else {
                    Intent intent = new Intent(getActivity(), StartLiveActivity.class);
                    getActivity().startActivity(intent);
                }
            } else {   //当未实名认证再次请求服务器查看，防止本地缓存失效
//                getUserInfoByIdService.getUserInfoById(userProviderService.getUserInfo().getId() + "", new LoadingObserver<HttpResult<UserInfo>>() {
//                    @Override
//                    public void _onNext(HttpResult<UserInfo> data) {
//                        if(data!=null && data.data!=null){
//                            if(data.data.isReanName()){
//                                Intent intent = new Intent(getActivity(), StartLiveActivity.class);
//                                getActivity().startActivity(intent);
//                            }else{
//                                showIsRealNameDialog(userProviderService.getUserInfo());
//                            }
//                        }
//                    }
//                    @Override
//                    public void _onError(String msg) {
//                    }
//                });
                new RetrofitUtils().createApi(LiveApiService.class).getUserCenter()
                        .compose(RxUtils.applySchedulers())
                        .subscribe(new Consumer<HttpResult<UserInfo>>() {
                            @Override
                            public void accept(HttpResult<UserInfo> data) throws Exception {
                                if (data != null && data.data != null) {
                                    if (data.data.isReanName()) {
                                        if (!data.data.isFace()) {
                                            // 如果该主播还没有做过人脸识别，需要先进行人脸识别
                                            showFaceCheckDialog(data.data);
                                        } else {
                                            Intent intent = new Intent(getActivity(), StartLiveActivity.class);
                                            getActivity().startActivity(intent);
                                        }
                                    }else if (data.data.myIdCardWithdraw == UserInfo.IDCARD_STATUS_ING) {
                                        ToastUtils.showShort(getContext(), "实名认证中,请您耐心等待", Gravity.CENTER);
                                    } else {
                                        showIsRealNameDialog(userProviderService.getUserInfo());
                                    }
                                }
                            }
                        });


            }
        }
    }

    private void showFaceCheckDialog(UserInfo userInfo) {
        CustomerDialog dialog = new CustomerDialog.Builder(getActivity()).setTitle("温馨提示")
                .setMsg("该账户未进行人脸认证")
                .setNegativeButton("取消", () -> {

                })
                .setPositiveButton("去认证", () -> {
                    ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                            .withInt("type", Common.SKIP_USER)
                            .withBoolean("isReview",false)
                            .withInt("status", userInfo.myIdCardWithdraw)
                            .navigation();
                        }
                ).create();
        dialog.show();
    }

    private void showIsRealNameDialog(UserInfo userInfo) {
        String msg = "";
        String btnText = "";
        if (userInfo.myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_YES) {
            // 已经实名认证
            switch (userInfo.getMyIsCardStatus()) {
                case UserInfo.IDCARD_STATUS_NULL:
                    msg = "您未进行主播认证";
                    break;
                case UserInfo.IDCARD_STATUS_ING:
                    msg = "该账户主播认证中";
                    break;
                case UserInfo.IDCARD_STATUS_NO:
                    msg = "该账户主播认证不通过";
                    break;
            }
            btnText = "主播认证";
        } else {
            // 未实名认证
            msg = "您未进行实名认证，\n认证后可申请成为主播";
            btnText = "去认证";
        }
        if (userInfo.getMyIsCardStatus() == UserInfo.IDCARD_STATUS_ING) {
            ToastUtils.showShort(getContext(), msg);
            return;
        }
        CustomerDialog dialog = new CustomerDialog.Builder(getActivity()).setTitle("温馨提示").setMsg(msg)
                .setPositiveButton(btnText, () -> {
                            ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                                    .withInt("status", userInfo.getMyIdCardWithdraw())
                                    .withBoolean("isReview", true)
                                    .withInt("type", Common.SKIP_ANCHOR)
                                    .navigation();

                }
                ).create();
        dialog.show();
        if (userInfo.getMyIsCardStatus() != UserInfo.IDCARD_STATUS_NULL) {
            dialog.hideCancle();
        }

    }
}
