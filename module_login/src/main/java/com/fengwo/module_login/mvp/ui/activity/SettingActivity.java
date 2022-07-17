package com.fengwo.module_login.mvp.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.StringDef;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.event.LogoutEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DeviceUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.widget.LogoutPopwindow;
import com.fengwo.module_comment.widget.MenuItem;
import com.fengwo.module_live_vedio.mvp.ui.activity.TeenagerModeSettingActivity;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocket1;
import com.tencent.bugly.beta.Beta;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class SettingActivity extends BaseMvpActivity {
    @BindView(R2.id.item_versionname)
    MenuItem itemVersionname;
    @BindView(R2.id.swTopChat)
    Switch swTopChat;
    @BindView(R2.id.btn_teenager)
    MenuItem btnTeenager;

    @Autowired
    UserProviderService userProviderService;

    private Boolean isNightMode = false;
    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        if(null!=UserManager.getInstance().getUser()){
            isNightMode = (Boolean) SPUtils1.get(this, "IS_MODE_NIGHT" + UserManager.getInstance().getUser().id, false);
        }else {
            userProviderService.updateUserInfo(new LoadingObserver<UserInfo>(){
                @Override
                public void _onNext(UserInfo data) {
                    isNightMode =(Boolean) SPUtils1.get(SettingActivity.this, "IS_MODE_NIGHT" + data.id, false);
                }

                @Override
                public void _onError(String msg) {

                }
            });

        }

        swTopChat.setChecked(isNightMode);

        new ToolBarBuilder().showBack(true)
                .setTitle("设置")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build();
        itemVersionname.setRightText(AppUtils.getVersionName(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (userProviderService != null) {
            if (TextUtils.equals(Constants.TEENAGER_MODE_ENABLE, userProviderService.getUserInfo().teenagerMode)) {
                btnTeenager.setRightText("已开启");
            } else {
                btnTeenager.setRightText("未开启");
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_setting;
    }

    @OnClick({R2.id.btn_tosafe, R2.id.btn_logout, R2.id.btn_about_us, R2.id.btn_black_list, R2.id.item_versionname, R2.id.swTopChat, R2.id.btn_teenager})
    public void onViewClicked(View view) {
        if(isFastClick()){
            //点击防抖
            return;
        }
        int id = view.getId();
        if (id == R.id.btn_teenager) {
            //跳转青少年模式开关页面
            startActivity(TeenagerModeSettingActivity.class);
        } else if (id == R.id.btn_tosafe) {
            startActivity(SafeActivity.class);
        } else if (id == R.id.btn_logout) {
            logout();
        } else if (id == R.id.btn_about_us) {
            startActivity(AboutUsActivity.class);
        } else if (id == R.id.btn_black_list) {
            startActivity(BlackListActivity.class);
        } else if (id == R.id.item_versionname) {
            Beta.checkUpgrade();
        } else if (id == R.id.swTopChat) {
            if (isNightMode) {
                SPUtils1.put(this, "IS_MODE_NIGHT" + UserManager.getInstance().getUser().id, false);
                DarkUtil.setDarkTheme(MODE_NIGHT_NO);
            } else {
                SPUtils1.put(this, "IS_MODE_NIGHT" + UserManager.getInstance().getUser().id, true);
                DarkUtil.setDarkTheme(MODE_NIGHT_YES);
            }

            ActivitysManager.getInstance().popAll();
            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY)
                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();
        }
    }

    LogoutPopwindow logoutPopwindow;

    private void logout() {
        if (null == logoutPopwindow) {
            logoutPopwindow = new LogoutPopwindow(this);
            logoutPopwindow.setOnLogoutListener(new LogoutPopwindow.OnLogoutClickListener() {
                @Override
                public void onLogout() {
//                    doLogoout();
                    exitLog();
                }
            });
        }
        logoutPopwindow.showPopupWindow();
    }

    private void doLogoout() {
        RxBus.get().post(new LogoutEvent());
        new RetrofitUtils().createApi(LoginApiService.class)
                .logout()
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        finish();
                        try {
                            UserManager.getInstance().clearUser();
                            FWWebSocket1.getInstance().destroy();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(LoginActivity.class);
                    }

                    @Override
                    public void _onError(String msg) {
                        finish();
                        try {
                            UserManager.getInstance().clearUser();
                            FWWebSocket1.getInstance().destroy();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(LoginActivity.class);
                    }
                });
    }

    public void exitLog() {
        String appVersion = AppUtils.getVersionName(this);
        BasePresenter basePresenter = new BasePresenter();
        Map map = new HashMap();
        map.put("appVersion", appVersion);
        map.put("loginIp", AppUtils.getIPAddress(this));
        map.put("mobileType", DeviceUtils.getModel());
        map.put("mobileVersion", DeviceUtils.getSDKVersionName());
        disposable.add(new RetrofitUtils().createApi(LoginApiService.class)
                .exitLog(basePresenter.createRequestBody(map))
                .compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                doLogoout();
            }

            @Override
            public void _onError(String msg) {
                doLogoout();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
