package com.fengwo.module_login.mvp.ui.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.manage.AutoLoginManage;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.mvp.ui.fragment.PrivacyDialogFragment;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.LoginSuccessEvent;
import com.fengwo.module_login.mvp.dto.LoginDto;
import com.fengwo.module_login.mvp.presenter.LoginPresenter;
import com.fengwo.module_login.mvp.ui.activity.acc_cancel.AccCancelConfirmActivity;
import com.fengwo.module_login.mvp.ui.iview.ILoginView;
import com.fengwo.module_login.mvp.ui.pop.ForbidenHostPop;
import com.fengwo.module_login.mvp.ui.pop.WriteOffTipsPopwindow;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = ArouterApi.LOGIN_ACTIVITY)
public class LoginActivity extends BaseMvpActivity<ILoginView, LoginPresenter> implements ILoginView, SurfaceHolder.Callback {
    @BindView(R2.id.sv_start)
    SurfaceView svStart;
    @BindView(R2.id.rl_all)
    RelativeLayout rlAll;
    @BindView(R2.id.btn_rule)
    TextView btnRule;
    @BindView(R2.id.ims_yd)
    CheckBox mRbDeal;


    private MediaPlayer player;
    private SurfaceHolder holder;

    //记录一下友盟分享
    //如果用户开启了应用分身
    //点击取消后 会导致loading界面无法销毁
    private boolean UMShareFlag;



    @Autowired
    public String id;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        fullScreen(this);
        initAutoLogin();
        RxBus.get().toObservable(LoginSuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(loginSuccessEvent -> {
                    LoginActivity.this.finish();
                });

        btnRule.setText(Html.fromHtml(getResources().getString(R.string.login_rule)));
//        if (null != UserManager.getInstance().getUser() && !TextUtils.isEmpty(UserManager.getInstance().getToken())) {
//            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
//            finish();
//        }

        if (TextUtils.equals(id, "banned")) {
            ForbidenHostPop forbidenHostPop = new ForbidenHostPop(this);
            forbidenHostPop.setPopupGravity(Gravity.CENTER);
            forbidenHostPop.showPopupWindow();
        }
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                holder = svStart.getHolder();
                holder.addCallback(LoginActivity.this);
                holder.setKeepScreenOn(true);
                initPlay();
            }
        });
        //隐私弹窗
        Boolean IsHide = (Boolean) SPUtils1.get(this, "IsHide", false);
        if (!IsHide) {
            new PrivacyDialogFragment().show(getSupportFragmentManager());
        }


        //生命周期监听
        getLifecycle().addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume(LifecycleOwner source) { //一个参数
                safeStartPlay();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void onPause() {
                if (player != null) {
                    player.pause();
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy() {
                releasePlayerIfNeeded();
            }

        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (UMShareFlag) {
            UMShareFlag = false;
            hideLoadingDialog();
        }
    }

    private void safeStartPlay() {
        //如果从后台返回，则需要进一步处理
        if (flagIsBackFromPress) {
            //停止播放
            player.stop();
            //重置播放器状态
            player.reset();
            //释放播放器资源
            player.release();
            player = null;
            //修改编辑
            flagIsBackFromPress = false;
            //重新初始化播放器
            initPlay();
            return;
        }
        if (player != null) {
            player.start();
        }
    }


    /**
     * 一键登录初始化
     */
    private void initAutoLogin() {
        // 初始化一键登录
        AutoLoginManage.getInstance(this).init(new AutoLoginManage.OnTokenResultListener() {
            @Override
            public void getTokenSuccess(String token) {
                if (p == null) return;
                p.getAutoLogin(token, AppUtils.getChannelName(LoginActivity.this));
            }

            @Override
            public void getTokenFailed(String msg) {
                startActivity(LoginByPhoneActivity.class);
            }

            @Override
            public void hideLoading() {
                hideLoadingDialog();
            }
        }).addUIClickListener(new AutoLoginManage.OnUIClickImplements() {
            @Override
            public void onClickToggle(JSONObject jsonObj) {
                startActivity(LoginByPhoneActivity.class);
            }
        });
    }

    private void initPlay() {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(mp -> {
            try {
                player.start();
                player.setDisplay(holder);
                componentComing();
            }catch (IllegalArgumentException e){

            }

        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if (i != MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    //播放出错后，重置播放器状态
                    player.reset();
                    //修改标记
                    flagIsBackFromPress = true;
                    return true;
                }
                return false;
            }
        });
        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.log_in);
            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                    file.getLength());
            player.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            player.setLooping(true);
            player.prepare();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void componentComing() {
        if (rlAll.getAlpha() == 1) return;
        ObjectAnimator animator = ObjectAnimator.ofFloat(rlAll, "alpha", 0, 1);
        animator.setDuration(3000);//播放时长
        animator.setStartDelay(500);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }


    @Override
    protected int getContentView() {
        return R.layout.login_activity_login;
//        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R2.id.btn_login_wechat, R2.id.btn_stop, R2.id.btn_rule, R2.id.tv_custom_service, R2.id.btn_automatic_login,R2.id.ims_yd})
    public void onViewClicked(View view) {
        if (isFastClick()) {
            return;
        }
        int id = view.getId();
        if (id == R.id.btn_automatic_login) { //一键登录
            if(!mRbDeal.isChecked()){
                ToastUtils.showShort(this,"请先勾选用户协议");
                return;
            }
            showLoadingDialog();
            AutoLoginManage.getInstance(this).login();
        } else if (id == R.id.btn_login_wechat) {
            if(!mRbDeal.isChecked()){
                ToastUtils.showShort(this,"请先勾选用户协议");
                return;
            }
            showLoadingDialog();
            UMShareFlag = true;
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    String name = map.get("name");
                    String imgUrl = map.get("iconurl");
                    String openid = map.get("openid");
                    String gender = map.get("gender");
                    String unionid = map.get("unionid");
                    if (p != null) {
                        p.thirdLogin(imgUrl, name, openid, gender, 2, AppUtils.getChannelName(LoginActivity.this), unionid);
                        hideLoadingDialog();
                    }
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    hideLoadingDialog();
                    if (!UMShareAPI.get(LoginActivity.this).isInstall(LoginActivity.this, SHARE_MEDIA.WEIXIN))
                        toastTip("手机未安装微信");
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    hideLoadingDialog();
                }
            });
        } else if (id == R.id.btn_stop) {
        } else if (id == R.id.btn_rule) {
            BrowserActivity.startRuleActivity(this);
        } else if (id == R.id.tv_custom_service) {
            DialogUtil.showAlertDialog(this, "拨打客服电话?", "是否拨打客服电话：400-005-1118",
                    "确定", "取消", false, new DialogUtil.AlertDialogBtnClickListener() {
                        @Override
                        public void clickPositive() {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:400-005-1118");
                            intent.setData(data);
                            startActivity(intent);
                        }

                        @Override
                        public void clickNegative() {

                        }
                    });
        }
    }

    @Override
    public void setCode(String code) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=player){
            player.reset();
            player.pause();
            player.release();
        }
        AutoLoginManage.getInstance(this).quitLoginPage();

    }

    @Override
    public void loginSuccess(UserInfo userInfo,boolean isEditInfo) {
        //手机号码登录成功后直接进入主页面
        //判断用户是否需要修改昵称和头像
        hideLoadingDialog();
        if (isEditInfo) {
            Intent i = new Intent(this,LoginInfoActivity.class);
            i.putExtra(LoginInfoActivity.NICK_NAME,userInfo.nickname);
            i.putExtra(LoginInfoActivity.HEADER_URL,userInfo.headImg);
            startActivity(i);
        }else {
            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
            //登陆IM系统
            finish();
        }
    }

    @Override
    public void toBindPhone(String token) {
        BindPhoneActivity.startBindPhoneActivity(this, token);
    }

    @Override
    public void toAccDestroyCancel(LoginDto loginDto) {
        WriteOffTipsPopwindow writeOffTipsPopwindow = new WriteOffTipsPopwindow(this, loginDto.fwId);
        writeOffTipsPopwindow.setOnConfirmListener(() ->
                AccCancelConfirmActivity.start(LoginActivity.this, loginDto.userId + "", loginDto.mobile, false));
        writeOffTipsPopwindow.showPopupWindow();
    }


    private boolean flagIsBackFromPress;

    @Override
    public void onBackPressed() {
        if (ActivitysManager.getInstance().getSize() > 1) {
            ActivitysManager.getInstance().popAll();
        } else {
            flagIsBackFromPress = true;
            //按下返回键，停止播放
            player.stop();
            super.onBackPressed();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
    /**
     * 释放播放器资源
     */
    private void releasePlayerIfNeeded() {
        if (player == null) return;
        player.stop();
        player.release();
        player = null;
        AutoLoginManage.getInstance(this).quitLoginPage();
    }
}
