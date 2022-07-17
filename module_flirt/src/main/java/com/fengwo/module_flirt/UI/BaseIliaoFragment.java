package com.fengwo.module_flirt.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentTransaction;

import com.amap.api.location.AMapLocation;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_flirt.Interfaces.IFlirtHomeView;
import com.fengwo.module_flirt.Interfaces.SelectListenet;
import com.fengwo.module_flirt.P.FlirtHomePresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.fragment.FilterAnimationFragment;
import com.fengwo.module_flirt.UI.fragment.FlirtTalentFragment;
import com.fengwo.module_login.api.LoginApiService;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

/**
 * @anchor Administrator
 * @date 2020/9/22
 */
public class BaseIliaoFragment extends BaseMvpFragment<IFlirtHomeView, FlirtHomePresenter> implements SelectListenet {

    private static int STARRY_SKY_DURATION = 1500;

    @BindView(R2.id.frame_flirt_talent)
    FrameLayout frameFlirtTalent;
    @BindView(R2.id.frame_filter)
    FrameLayout frameFilter;
    FlirtTalentFragment flirtTalentFragment = new FlirtTalentFragment();
    FilterAnimationFragment filterAnimationFragment = new FilterAnimationFragment();
    //转场动画的中心点
    private int x;
    private int y;
    private float endRadius;//动画最大半径

    /*当前正在动画*/
    private boolean isAnimating = false;

    @Override
    protected FlirtHomePresenter initPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_il_base;
    }

    private boolean isstyle = true;

    @Override
    public void initUI(Bundle savedInstanceState) {
        initSize();
        initFragment();

        // setTitleBackground(Color.parseColor("#ffffff"));
    }

    @Override
    public void initImmersionBar() {
        if (isstyle) {
            ImmersionBar.with(this)
                    .statusBarDarkFont(false, 0.2f)
                    .navigationBarDarkIcon(true, 0.2f)
                    .navigationBarColor(com.fengwo.module_comment.R.color.white)

                    .init();
        } else {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true, 0.2f)
                    .navigationBarDarkIcon(true, 0.2f)
                    .navigationBarColor(com.fengwo.module_comment.R.color.white)

                    .init();
        }

    }

    /**
     * 初始化需要的尺寸大小和位置
     */
    private void initSize() {
        //计算动画的中心点，根据FlirtTalent中的btn_label位置和大小(32+12)已经statusBar和MainActivity底部的tab高度处理
        int dp44 = (int) getActivity().getResources().getDimension(R.dimen.dp_44);
        //MainActivity底部的tab高度
        int dp48 = (int) getActivity().getResources().getDimension(R.dimen.dp_48);
        int statusHeight = ScreenUtils.getStatusHeight(getContext());
        x = ScreenUtils.getScreenWidth(getActivity()) - dp44;
        y = ScreenUtils.getScreenHeight(getActivity()) + statusHeight - dp44 - dp48;
        endRadius = (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        flirtTalentFragment.setIOnclickListener(this);
        filterAnimationFragment.setIOnclickListener(this);
        frameFilter.post(new Runnable() {
            @Override
            public void run() {
                if (frameFilter == null) return;
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.frame_filter, filterAnimationFragment)
                        .add(R.id.frame_flirt_talent, flirtTalentFragment).commitAllowingStateLoss();
                starrySkyEnterAnim(true);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpLocation();
    }

    private void setUpLocation() {
        MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
            @Override
            public void onLocationSuccess(AMapLocation location) {

                new RetrofitUtils().createApi(LoginApiService.class)
                        .updateUserinfo(new HttpUtils.ParamsBuilder()
//                        .put("signature", userInfo.signature)
//                        .put("sex", userInfo.sex + "")
                                        .put("location", location.getCity())
                                        .put("longitude", String.valueOf(location.getLongitude()))
                                        .put("latitude", String.valueOf(location.getLatitude()))
//                        .put("headImg", userInfo.headImg)
//                        .put("birthday", userInfo.birthday)
                                        .build()
                        )
                        .subscribe(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                L.e("更新地址成功");
                            }

                            @Override
                            public void _onError(String msg) {
                                L.e("更新地址失败 " + msg);
                            }
                        });
            }

            @Override
            public void onLocationFailure(String msg) {
                p.upLoading("0", "0", "");
                new RetrofitUtils().createApi(LoginApiService.class)
                        .updateUserinfo(new HttpUtils.ParamsBuilder()
                                .put("location", "")
                                .put("longitude", "0")
                                .put("latitude", "0")
                                .build()
                        )
                        .subscribe(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                //    L.e("更新地址成功");
                            }

                            @Override
                            public void _onError(String msg) {
                                //    L.e("更新地址失败 " + msg);
                            }
                        });
            }
        });
    }

    /**
     * 星空进入
     */
    private void starrySkyEnterAnim(boolean isStart) {
        if (isAnimating) return;
        isAnimating = true;
        //进场可以先加载数据，出场时的数据在动画播完后清空
        if (!isStart) filterAnimationFragment.resume();

        frameFilter.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.show(filterAnimationFragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frameFilter.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (frameFilter == null) return;
                        Animator enterAnim = ViewAnimationUtils.createCircularReveal(frameFilter, x, y, 0, endRadius);
                        enterAnim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                isAnimating = false;
                                try {
                                    frameFlirtTalent.setVisibility(View.GONE);
                                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                    if (null == flirtTalentFragment) {
                                        flirtTalentFragment = new FlirtTalentFragment();
                                    }
                                    transaction.hide(flirtTalentFragment).commitAllowingStateLoss();
                                } catch (NullPointerException e) {

                                }
                                isstyle = true;
                            }
                        });
                        enterAnim.setDuration(STARRY_SKY_DURATION);
                        enterAnim.start();
                    } catch (NullPointerException e) {

                    }
                }
            });

        } else {
            isAnimating = false;
            frameFlirtTalent.setVisibility(View.GONE);
            transaction.hide(flirtTalentFragment);
            isstyle = true;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 星空退出
     */
    private void starrySkyExitAnim() {
        if (isAnimating) return;
        isAnimating = true;
        frameFlirtTalent.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.show(flirtTalentFragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frameFilter.post(new Runnable() {
                @Override
                public void run() {
                    Animator exitAnim = ViewAnimationUtils.createCircularReveal(frameFilter, x, y, endRadius, 0);
                    exitAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isAnimating = false;
                            frameFilter.setVisibility(View.GONE);
                            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                            transaction.hide(filterAnimationFragment).commitAllowingStateLoss();
                            isstyle = false;
                            //刷新对应数据
                            flirtTalentFragment.resume();
                            filterAnimationFragment.close();
                        }
                    });
                    exitAnim.setDuration(STARRY_SKY_DURATION);
                    exitAnim.start();
                }
            });
        } else {
            frameFilter.setVisibility(View.GONE);
            frameFlirtTalent.setVisibility(View.VISIBLE);
            transaction.hide(filterAnimationFragment);
            isstyle = false;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void select(int i) {
        switch (i) {
            case 0:
                starrySkyEnterAnim(false);
                break;
            case 1:
                starrySkyExitAnim();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        frameFilter = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        KLog.e("tag", isVisibleToUser + "");
        if (isVisibleToUser) {

            setUpLocation();
            if (filterAnimationFragment.isAdded()) {
                filterAnimationFragment.resume();

            }

            if (flirtTalentFragment.isAdded()) {
                flirtTalentFragment.resume();
            }
        } else {
            if (filterAnimationFragment.isAdded()) {
                filterAnimationFragment.close();
            }
        }

    }

}
