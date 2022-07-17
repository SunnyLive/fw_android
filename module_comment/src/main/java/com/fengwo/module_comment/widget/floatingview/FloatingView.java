package com.fengwo.module_comment.widget.floatingview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.core.view.ViewCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.api.CommentService;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.base.live.PlayerManager;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.floatingview.utils.EnContext;

import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;

import static com.zhy.view.flowlayout.TagFlowLayout.dip2px;


/**
 * @ClassName FloatingView
 * @Description 悬浮窗管理器
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:05
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:05
 */
public class FloatingView implements IFloatingView {

    private FloatingMagnetView mEnFloatingView;
    private static volatile FloatingView mInstance;
    private WeakReference<FrameLayout> mContainer;
    @LayoutRes
    private int mLayoutId = R.layout.en_floating_view;
    private String playUrl;
    private ViewGroup.LayoutParams mLayoutParams = getParams();
    private int anchorId;   // 主播id
    private int status;     // 进入状态 0未付费 1已付费
    private String bgUrl;   // 背景图片
    private String gear;   // 0 开启缘分  1 再续前缘 2 缘定三生
    private long expireTime; // 印象值倒计时
    private String nickname;// 主播昵称
    private String headImg; // 主播头像
    private String roomId;  // 当前房间id
    private CountBackUtils mCountBackUtils;
    private CompositeDisposable mCompositeDisposable;
    private CommentService service;
    private RetrofitUtils retrofitUtils;
    private WeakReference<BaseActivity> mActivity;

//    private FWWebSocketWenBo.OnSocketConnectListener onSocketConnectListener;
//    private final ChatHistroySQLHelper sQLHelper;


    private FloatingView() {
    }

    public static FloatingView getInstance() {
        if (mInstance == null) {
            synchronized (FloatingView.class) {
                if (mInstance == null) {
                    mInstance = new FloatingView();
                }
            }
        }
        return mInstance;
    }

    @Override
    public FloatingView remove() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mEnFloatingView == null) {
                    return;
                }
                if (ViewCompat.isAttachedToWindow(mEnFloatingView) && getContainer() != null) {
                    getContainer().removeView(mEnFloatingView);
                }
                mEnFloatingView = null;
            }
        });
        return this;
    }

    private void ensureFloatingView() {
        synchronized (this) {
            if (mEnFloatingView != null) {
                return;
            }
            EnFloatingView enFloatingView = new EnFloatingView(EnContext.get(), mLayoutId);
            mEnFloatingView = enFloatingView;
            enFloatingView.setLayoutParams(mLayoutParams);
            addViewToWindow(enFloatingView);
            hide();
        }
    }

    @Override
    public FloatingView add() {
        ensureFloatingView();
        return this;
    }

    @Override
    public FloatingView attach(Activity activity) {
        attach(getActivityRoot(activity));
        mActivity = new WeakReference(activity);
        return this;
    }

    @Override
    public FloatingView attach(FrameLayout container) {
        if (container == null || mEnFloatingView == null) {
            mContainer = new WeakReference<>(container);
            return this;
        }
        if (mEnFloatingView.getParent() == container) {
            return this;
        }
        if (mEnFloatingView.getParent() != null) {
            ((ViewGroup) mEnFloatingView.getParent()).removeView(mEnFloatingView);
        }
        mContainer = new WeakReference<>(container);
        container.addView(mEnFloatingView);
        if(!TextUtils.isEmpty(playUrl)){
            PlayerManager.getInstance(container.getContext()).startPlay(playUrl,
                    ((EnFloatingView)mEnFloatingView).getVideoView(), null);
        }

        return this;
    }

    @Override
    public FloatingView detach(Activity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    @Override
    public FloatingView detach(FrameLayout container) {
        if (mEnFloatingView != null && container != null && ViewCompat.isAttachedToWindow(mEnFloatingView)) {
            container.removeView(mEnFloatingView);
        }
        if (getContainer() == container) {
            mContainer = null;
        }
        return this;
    }

    @Override
    public FloatingMagnetView getView() {
        return mEnFloatingView;
    }

    @Override
    public FloatingView customView(FloatingMagnetView viewGroup) {
        mEnFloatingView = viewGroup;
        return this;
    }

    @Override
    public FloatingView customView(@LayoutRes int resource) {
        mLayoutId = resource;
        return this;
    }

    @Override
    public FloatingView layoutParams(ViewGroup.LayoutParams params) {
        mLayoutParams = params;
        if (mEnFloatingView != null) {
            mEnFloatingView.setLayoutParams(params);
        }
        return this;
    }

    @Override
    public FloatingView listener(MagnetViewListener magnetViewListener) {
        if (mEnFloatingView != null) {
            mEnFloatingView.setMagnetViewListener(magnetViewListener);
        }
        return this;
    }

    private void addViewToWindow(final View view) {
        if (getContainer() == null) {
            return;
        }
        getContainer().addView(view);
    }

    private FrameLayout getContainer() {
        if (mContainer == null) {
            return null;
        }
        return mContainer.get();
    }

    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.setMargins(0, 0, dip2px(EnContext.get(), 10), DensityUtils.dp2px(EnContext.get(), 62));
        return params;
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取播放View
     * @return
     */
    public TXCloudVideoView getVideoView() {
        if (mEnFloatingView instanceof EnFloatingView) {
            return ((EnFloatingView)mEnFloatingView).getVideoView();
        }
        return null;
    }

    public void showClose() {
        if (mEnFloatingView instanceof EnFloatingView) {
            ((EnFloatingView)mEnFloatingView).showClose();
        }
    }

    /**
     * 设置播放地址
     * @param playUrl
     */
    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public void setStartParams(int anchorId, int status, String bgUrl) {
        this.anchorId = anchorId;
        this.status = status;
        this.bgUrl = bgUrl;
    }

    /**
     * 显示FloatingView
     */
    public void show() {
        if (mEnFloatingView != null) {
            mEnFloatingView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏FloatingView
     */
    public void hide() {
        if (mEnFloatingView != null) {
            mEnFloatingView.setVisibility(View.GONE);
            PlayerManager.getInstance(EnContext.get()).stopPlay();
            stopCountTime();
        }
    }

    public boolean isShow() {
        return mEnFloatingView != null && mEnFloatingView.getVisibility() == View.VISIBLE;
    }

    /**
     * 启动倒计时
     */
    public void startCountTime() {
        if (mCountBackUtils == null) {
            mCountBackUtils = new CountBackUtils();
        }
        mCountBackUtils.countBack(expireTime, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                expireTime = time;
                KLog.e("tag","Floation"+time);
            }

            @Override
            public void finish() {
                showExitDialog();
            }
        });
    }

    private void stopCountTime() {
        if (mCountBackUtils != null) {
            mCountBackUtils.destory();
            mCountBackUtils = null;
        }
    }

    public void quitRoom() {
        quitRoom(roomId, 1);
    }

    /**
     * 退出直播间
     * @param roomId
     * @param gotoLive  退出直播间时 0 默认不缘分值 1 清除
     */
    private void quitRoom(String roomId, int gotoLive) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        if (retrofitUtils == null) {
            retrofitUtils = new RetrofitUtils();
        }
        if (service == null) {
            service = retrofitUtils.createWenboApi(CommentService.class);
        }
        RequestBody build = new WenboParamsBuilder()
                .put("roomId", roomId)
                .put("goToLive", gotoLive + "")
                .build();
        mCompositeDisposable.add(service.quitRoom(build)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                hide();
                KLog.d("退出达人房间成功");
            }

            @Override
            public void _onError(String msg) {
                hide();
                KLog.d("退出达人房间失败");
            }
        }));
    }

    public void onDestroy() {
        if (mEnFloatingView == null) {
            return;
        }
        if (ViewCompat.isAttachedToWindow(mEnFloatingView) && getContainer() != null) {
            getContainer().removeView(mEnFloatingView);
        }
        playUrl = "";
        mEnFloatingView = null;
    }

    public void startChatRoomActivity() {
        ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                .withInt("anchorId", anchorId)
                .withInt("status", status)
                .withString("bgUrl", bgUrl)
                .navigation();
    }


    /**
     * 关闭悬浮窗弹框提示
     */
    @SuppressLint("CheckResult")
    public void showExitDialog() {
        BaseActivity activity = mActivity.get();
        if (activity == null) {
            return;
        }
        final ExitDialog dialog = new ExitDialog();
        dialog.setNeedCountdown(false);
        dialog.setNegativeButtonText("去聊天")
                .setPositiveButtonText("退出聊天室")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        // 隐藏小窗
                        quitRoom();
                    }

                    @Override
                    public void onCancel() {
                        if (!activity.getClass().getName().equals("com.fengwo.module_flirt.UI.activity.ChatRoomActivity") ) {
                            startChatRoomActivity();
                        }
                    }
                })
                .setGear(getGear())
                .setNickname(getNickname())
                .setExpireTime(getExpireTime())
                .setHeadImg(getHeadImg())
                .setRoomId(getRoomId())
                .setTip("缘分已耗尽，是否继续聊天")
                .show(activity.getSupportFragmentManager(), "");
        dialog.setOnDismissListener(() -> {
            // 隐藏小窗
            quitRoom();
            dialog.dismiss();
        });
    }




//    private void () {
//
//        sQLHelper = ChatHistroySQLHelper.getInstance();
//        userId = String.valueOf(UserManager.getInstance().getUser().getId());
//        gson = new Gson();
//        onSocketConnectListener = new FWWebSocketWenBo.OnSocketConnectListener() {
//            @Override
//            public void onMessage(String playLoad) {
//                if (null == getView()) return;
//                ((Activity) getView()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        KLog.e("=========接收到聊天消息 " + playLoad);
//                        KLog.d("yang_message", "接收到聊天消息 = " + playLoad);
//                        handMsg(playLoad);
//                    }
//                });
//            }
//
//            @Override
//            public void onReconnect() {
//
//            }
//        };
//        webSocket = FWWebSocketWenBo.getInstance();
//        if (webSocket == null) {
//            if (getView() != null)
//                getView().toastTip("连接异常请重启");
//        } else {
//            webSocket.addOnConnectListener(onSocketConnectListener);
//        }
//    }

    public String getGear() {
        return this.gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public long getExpireTime() {
        KLog.e("tag",expireTime+"");
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}