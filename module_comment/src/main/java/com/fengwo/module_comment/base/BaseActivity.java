package com.fengwo.module_comment.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.api.CommentService;
import com.fengwo.module_comment.base.live.PlayerManager;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_comment.bean.InvtationDataBean;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.dialog.InvitationNoticeDialog;
import com.fengwo.module_comment.event.ExitWindowEvent;
import com.fengwo.module_comment.event.SmallWindowEvent;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.LoadingDialog;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.OnKeyboardListener;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;

//import butterknife.ButterKnife;


/**
 * Created by D N on 2017/4/25.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements MvpView, OnKeyboardListener {

    protected View baseTitle;
    protected View toolbar;
    protected View statusBarView;
    protected ImmersionBar mImmersionBar;
    private Disposable mDisposable;
    public boolean isForeground;
    private CompositeDisposable mCompositeDisposable;
    public int myAnchorId = -1;
    public Lifecycle.Event mCurrentLifeEvent = Lifecycle.Event.ON_ANY;


    @Override
    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
        L.e("xxxxxxxxxxzzzzzzzzz", keyboardHeight + "xxxxxxx");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mCurrentLifeEvent = Lifecycle.Event.ON_CREATE;
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        ActivitysManager.getInstance().pushActivity(this);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
        if (getImmersionBar()) {
            initImmersionBar();
        }
        setContentView(getContentView());
        baseTitle = findViewById(R.id.basetitle);
        if (null != baseTitle && getImmersionBar()) {
            statusBarView = baseTitle.findViewById(R.id.status_bar_view);
            toolbar = baseTitle.findViewById(R.id.tool_bar);
            fitsLayoutOverlap();
        }

        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        ViewGroup viewGroup = getWindow().getDecorView().findViewById(android.R.id.content);
        initEvent();
    }


    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(!DarkUtil.isDarkTheme(this), 0.2f)
                .navigationBarDarkIcon(true, 0.2f)
                .navigationBarColor(R.color.white);
//                .keyboardEnable(true, getKeyBoardParams())
        mImmersionBar.init();
    }

    protected boolean getImmersionBar() {
        return true;
    }


    protected int getKeyBoardParams() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;
    }

    private void fitsLayoutOverlap() {
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView);
        } else {
            ImmersionBar.setTitleBar(this, toolbar);
        }
    }

    public void setTitleBackground(int color) {
        ColorDrawable d = new ColorDrawable(color);
        if (statusBarView != null) statusBarView.setBackground(d);
        if (toolbar != null) toolbar.setBackground(d);
        if (baseTitle != null) baseTitle.setBackground(d);
    }

    public void setWhiteTitle(String title) {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true).setTitleColor(R.color.text_33).setTitle(title).setBackIcon(R.drawable.ic_back_black).build();
    }

    public void setTitle(String title) {
        new ToolBarBuilder().showBack(true).setTitle(title).build();
    }

    protected abstract void initView();

    protected abstract int getContentView();

    LoadingDialog loadingDialog;

    @Override
    public void showLoadingDialog() {
        //   isForeground(BaseActivity.this,"LivingRoomActivity");
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.show();
                loadingDialog.setProgressPercent("");
            }
        });

    }

    @Override
    public boolean isLoadingDialogShow() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return true;
        }
        return false;
    }

    @Override
    public void setDialogProgressPercent(String percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != loadingDialog)
                    loadingDialog.setProgressPercent(percent);
            }
        });

    }


    @Override
    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isLoadingDialogShow()) {
                    loadingDialog.setProgressPercent("");
                    loadingDialog.dismiss();
                }
            }
        });
    }


    @Override
    public void toastTip(int msgId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(getApplicationContext(), msgId);
            }
        });
    }

    @Override
    public void toastTip(CharSequence msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(getApplicationContext(), msg);
            }
        });
    }

    @Override
    public void netError() {
        toastTip("请检查您的网络连接是否正常！");
    }

    public void jump() {

    }

    public void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mCurrentLifeEvent = Lifecycle.Event.ON_DESTROY;
        super.onDestroy();
        try {
            ActivitysManager.getInstance().popActivity(this);
            if (loadingDialog != null) {
                hideLoadingDialog();
                loadingDialog = null;
            }
        } catch (Exception e) {

        }
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public View getListEmptyView() {
        View v = LayoutInflater.from(this).inflate(R.layout.item_base_empty_view, null, false);
        return v;
    }

    public class ToolBarBuilder {
        public ImageView backBtn;
        public TextView titleTv;
        public ImageView right1Iv;
        public ImageView right2Iv;
        public TextView rightTv;

        private String title;
        private int right1Res = -1, right2Res = -1;
        private View.OnClickListener right1Listener, right2Listener, rightTextListener;
        private String rightText;
        private boolean isBackShow;
        private int titleTextColor, rightTextColor;
        private int backRes = -1;

        public ToolBarBuilder() {
            if (null == baseTitle) {
                throw new RuntimeException("没有 include base title");
            } else {
                titleTv = baseTitle.findViewById(R.id.title_tv);
                right1Iv = baseTitle.findViewById(R.id.right1_img);
                right2Iv = baseTitle.findViewById(R.id.right2_img);
                rightTv = baseTitle.findViewById(R.id.right_tv);
                backBtn = baseTitle.findViewById(R.id.btn_back);
            }
        }

        public ToolBarBuilder showBack(boolean isShow) {
            isBackShow = isShow;
            return this;
        }

        public ToolBarBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ToolBarBuilder setBackImg(int res) {
            this.backBtn.setImageResource(res);
            return this;
        }

        public ToolBarBuilder setTitleColor(int color) {
            this.titleTextColor = color;
            return this;
        }

        public ToolBarBuilder setRightTextColor(int color) {
            this.rightTextColor = color;
            return this;
        }

        public ToolBarBuilder setRight1Img(int res, View.OnClickListener l) {
            right1Res = res;
            right1Listener = l;
            return this;
        }

        public ToolBarBuilder setRight2Img(int res, View.OnClickListener l) {
            right2Res = res;
            right2Listener = l;
            return this;
        }

        public ToolBarBuilder setRightText(String res, View.OnClickListener l) {
            rightText = res;
            rightTextListener = l;
            return this;
        }

        public ToolBarBuilder setBackIcon(int res) {
            backRes = res;
            return this;
        }

        public void build() {
            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            }
            if (titleTextColor > 0) {
                titleTv.setTextColor(getResources().getColor(titleTextColor));
            }
            if (right1Res > 0) {
                right1Iv.setImageResource(right1Res);
                right1Iv.setOnClickListener(right1Listener);
            }
            if (right2Res > 0) {
                right2Iv.setImageResource(right2Res);
                right2Iv.setOnClickListener(right2Listener);
            }
            if (rightTextColor > 0) {
                rightTv.setTextColor(getResources().getColor(rightTextColor));
            }
            if (!TextUtils.isEmpty(rightText)) {
                rightTv.setText(rightText);
                rightTv.setOnClickListener(rightTextListener);
            }
            if (isBackShow) {
                backBtn.setVisibility(View.VISIBLE);
                backBtn.setOnClickListener(v -> {
                    onBackPressed();
                });
            }
            if (backRes != -1) {
                backBtn.setImageResource(backRes);
            }
        }

        public void setBackgroundColor() {
        }
    }

    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以考虑设置为透明色
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


    private static final int MIN_CLICK_DELAY_TIME = 600;
    private static final int MIN_CLICK_DELAY_TIME_LONG = 1200;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    public static boolean isFastClickLong() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - MIN_CLICK_DELAY_TIME_LONG) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    @Override
    public void onResume() {
        mCurrentLifeEvent = Lifecycle.Event.ON_RESUME;
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        mCurrentLifeEvent = Lifecycle.Event.ON_PAUSE;
        super.onPause();
        MobclickAgent.onPause(this);
        getWindow().getDecorView().post(() -> {
            InputMethodManager imm = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        });
    }

    @Override
    protected void onStart() {
        mCurrentLifeEvent = Lifecycle.Event.ON_START;
        super.onStart();
        FloatingView.getInstance().attach(this);
        isForeground = true;
    }

    @Override
    protected void onStop() {
        mCurrentLifeEvent = Lifecycle.Event.ON_STOP;
        super.onStop();
        FloatingView.getInstance().detach(this);
        isForeground = false;
    }

    private void initEvent() {
        mDisposable = RxBus.get().toObservable(ExitWindowEvent.class)
                .compose(bindToLifecycle())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((event -> {
                    showExitDialog();
                }));
//收到文播邀请
        RxBus.get().toObservable(InvtationDataBean.class)
                .compose(bindToLifecycle())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<InvtationDataBean>() {
                    @Override
                    public void accept(InvtationDataBean event) throws Exception {
                        showExitDialog(event);
                    }
                });
        mDisposable = RxBus.get().toObservable(SmallWindowEvent.class)
                .compose(bindToLifecycle())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((event -> {
                    if (isForeground) {
                        if (!FloatingView.getInstance().isShow()) {
                            PlayerManager.getInstance(this).startPlay(event.getPlayUrl(), FloatingView.getInstance().getVideoView(), null);
                            FloatingView.getInstance().show();
                        }
                    }
                }));
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);

//        boolean flag=false;
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
                KLog.e("tagactivityname", "" + taskInfo.topActivity.getShortClassName());
//                flag = true;
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog() {
        if (!isForeground) return;
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(getSupportFragmentManager(), "");
    }

    /**
     *
     */
    public void showExitDialog(InvtationDataBean event) {
        if (!isForeground) return;
        InvitationNoticeDialog dialog = new InvitationNoticeDialog(this, event.getPic(),
                event.getRoomId(),
                event.getNickname(),
                event.getText(), new InvitationNoticeDialog.IAddListListener() {
            @Override
            public void deleteBank() {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
                if (isForeground(BaseActivity.this, "LivingRoomActivity")) {//在秀播直播间
                    //     showExitDialog();
                    showDialog(event, "进入达人间将退出这场直播，你确认退出这场直播吗?");
                } else if (isForeground(BaseActivity.this, "ChatRoomActivity")) {//在文播直播间
                    ActivitysManager.getInstance().popActivity();
                    ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                            .withInt("anchorId", Integer.valueOf(event.getRoomId()))
                            .withInt("status", 1)
                            .withString("bgUrl", event.getPic())
                            .navigation();
                } else if (isForeground(BaseActivity.this, "LivePushActivity")) {//秀播主播在开播跳转
                    showDialog(event, "进入达人间将会关闭这场直播，您确认要关闭这场直播吗？");
                } else if (isForeground(BaseActivity.this, "HostChatRoomActivity")) {
                    showDialog(event, "进入达人间将会关闭这场直播，您确认要关闭这场直播吗？");
                } else
                    ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                            .withInt("anchorId", Integer.valueOf(event.getRoomId()))
                            .withInt("status", 1)
                            .withString("bgUrl", event.getPic())
                            .navigation();
            }
        });

        dialog.showPopupWindow();
    }

    /**
     * 退出直播间
     */
    @SuppressLint("CheckResult")
    public void quitRoom(String roomId) {
        RequestBody build = new WenboParamsBuilder()
                .put("roomId", roomId)
                .build();
        new RetrofitUtils().createWenboApi(CommentService.class).quitRoom(build).compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    /**
     * 调用离开房间接口 并发送离开房间消息
     *
     * @param
     * @param channelId 从channelId改为userId
     */
    public void leaveGroup(int channelId) {
        mCompositeDisposable.add(new RetrofitUtils().createApi(CommentService.class)
                .leaveLivingRoom(channelId).compose(RxUtils.applySchedulers()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {

                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        ToastUtils.showShort(BaseActivity.this, msg);

                    }
                }));

    }

    /**
     * 检测主播直播状态
     */
    public void checkAnchorStatus(int anchorId, String pic) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .build();
        mCompositeDisposable.add(new RetrofitUtils().createWenboApi(CommentService.class)
                .checkAnchorStatus(build).compose(RxUtils.applySchedulers()).subscribeWith(new LoadingObserver<HttpResult<CheckAnchorStatus>>() {
                    @Override
                    public void _onNext(HttpResult<CheckAnchorStatus> data) {
                        if (data.isSuccess()) {
                            ActivitysManager.getInstance().popActivity();

                            if (data.data.getBstatus() == 0) {
                                ToastUtils.showShort(BaseActivity.this, "主播已关播");
                            } else {
                                ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                                        .withInt("anchorId", Integer.valueOf(anchorId))
                                        .withInt("status", 1)
                                        .withString("bgUrl", pic)
                                        .navigation();
                            }

                        } else {
                            ToastUtils.showShort(BaseActivity.this, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        ToastUtils.showShort(BaseActivity.this, msg);

                    }
                }));
    }

    public void showDialog(InvtationDataBean event, String textview) {
        new CustomerDialog.Builder(this)
                .setTitle("温馨提示")
                .setMsg(textview)
                .setNegativeBtnShow(true)
                .setNegativeButton("真的要走了", new CustomerDialog.onNegetiveInterface() {
                    @Override
                    public void onNegetive() {
                        if (isForeground(BaseActivity.this, "LivePushActivity")) {//主播在秀播开播
                            RxBus.get().post(new JumpInvtationDataBean(event.getPic(),
                                    event.getRoomId(),
                                    event.getNickname(),
                                    event.getText()));
//                            ActivitysManager.getInstance().popActivity();
//                            quitRoom(String.valueOf(myAnchorId));
//                            checkAnchorStatus(Integer.parseInt(event.getRoomId()), event.getPic());

                        } else if (isForeground(BaseActivity.this, "LivingRoomActivity")) {//秀播用户
//                            leaveGroup(myAnchorId);
//                            ActivitysManager.getInstance().popActivity();
                            RxBus.get().post(new JumpInvtationDataBean(event.getPic(),
                                    event.getRoomId(),
                                    event.getNickname(),
                                    event.getText()));
                            ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                                    .withInt("anchorId", Integer.valueOf(event.getRoomId()))
                                    .withInt("status", 1)
                                    .withString("bgUrl", event.getPic())
                                    .navigation();
                        } else if (isForeground(BaseActivity.this, "HostChatRoomActivity")) {
                            RxBus.get().post(new JumpInvtationDataBean(event.getPic(),
                                    event.getRoomId(),
                                    event.getNickname(),
                                    event.getText()));
//                            ActivitysManager.getInstance().popActivity();
//                            ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
//                                    .withInt("anchorId", Integer.valueOf(event.getRoomId()))
//                                    .withInt("status", 1)
//                                    .withString("bgUrl", event.getPic())
//                                    .navigation();
                        } else {


                            ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                                    .withInt("anchorId", Integer.valueOf(event.getRoomId()))
                                    .withInt("status", 1)
                                    .withString("bgUrl", event.getPic())
                                    .navigation();
                        }
                    }
                })
                .setPositiveButton("取消", new CustomerDialog.onPositiveInterface() {
                    @Override
                    public void onPositive() {

                    }
                }).create().show();
    }
}