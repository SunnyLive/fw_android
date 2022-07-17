package com.fengwo.module_flirt.UI.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.GameEvent;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.event.RechargeSuccessEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.NetWorkChangReceiver;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.floatingview.FloatingMagnetView;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_comment.widget.floatingview.MagnetViewListener;
import com.fengwo.module_flirt.bean.EnterRoomBean;
import com.fengwo.module_flirt.bean.GetAnchorRoomInfo;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_flirt.dialog.CommonHeaderDialog;
import com.fengwo.module_flirt.dialog.FinishChatPopwindow;
import com.fengwo.module_flirt.dialog.HeaderPopWindow;
import com.fengwo.module_flirt.dialog.MoerPopwindow;
import com.fengwo.module_flirt.manager.HandleMsgManager;
import com.fengwo.module_flirt.manager.WenboMsgManager;
import com.fengwo.module_flirt.widget.AnchorListButtonView;
import com.fengwo.module_live_vedio.eventbus.ChatEvaluateEvent;
import com.fengwo.module_live_vedio.mvp.ui.df.ShareCodeDialog;
import com.fengwo.module_login.mvp.dto.WalletDto;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.tencent.rtmp.TXLiveConstants;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import razerdp.basepopup.BasePopupWindow;

/**
 * 聊天室
 * * @Author BLCS * @Time 2020/4/1 16:47
 */
@Route(path = ArouterApi.CHAT_ROOM_ACTION)
public class ChatRoomActivity extends BaseChatRoomButtonActivity {
    private static final String TAG = "ChatRoomActivity";

    private CountBackUtils countBackUtils;
    private CountBackUtils gameCountBackUtils;


    HeaderPopWindow netStatusPopwindow;
    private NetWorkChangReceiver netWorkChangReceiver;


    private AnchorListButtonView anchorListButtonView;
    private long livingRoomUserId;

    private FinishChatPopwindow popwindow;


    private boolean isFirst = true;
    @Autowired
    UserProviderService userProviderService;

    private MagnetViewListener mMagnetViewListener;


    /**
     * 用户支付成功后进入直播间等待状态
     *
     * @param anchorId 主播ID  用于获取主播信息
     * @param bgUrl    背景图  用于未读取到信息时显示的界面
     * @status 进入状态  1 等待主播确认
     */
    public static void startWait(Activity context, int anchorId, String bgUrl, int status) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra("anchorId", anchorId);
        intent.putExtra("status", status);
        intent.putExtra("bgUrl", bgUrl);
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*判断关注状态*/
        p.isAttention(anchorId);
    }

    @Override
    protected boolean isHost() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        ARouter.getInstance().inject(this);
        anchorId = getIntent().getIntExtra("anchorId", 0);
        status = getIntent().getIntExtra("status", 0);
        bgUrl = getIntent().getStringExtra("bgUrl");

        /*用于显示订单时长*/
        countBackUtils = new CountBackUtils();
        /*记录游戏*/
        gameCountBackUtils = new CountBackUtils();
        /*用户端显示聊天控件*/
        crvRoom.setShowChat(true);
        /*判断关注状态*/
        p.isAttention(anchorId);
        /*进入直播间*/
        p.enterRoom(anchorId);
        /*加载背景*/
        if (bgUrl.equals("-1")) {
            rl_zw.setVisibility(View.VISIBLE);

        } else {
            ImageLoader.loadImg(ivBg, bgUrl);
            ivBg.setVisibility(View.VISIBLE);
        }


        /*点击屏幕隐藏键盘*/
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (KeyBoardUtils.isSHowKeyboard(ChatRoomActivity.this, crvRoom.getEditInput().getInputEdit()))
                    KeyBoardUtils.closeKeybord(crvRoom.getEditInput().getInputEdit(), ChatRoomActivity.this);
                return false;
            }
        });
        crbUser.setVideoTime(0);
        //注册网络状态监听广播
        networkListener();
        /* 接收到通知 */
        receiveNotice();
        /*关闭礼物弹窗*/

        //  crvRoom.setHy("欢迎进入"+userProviderService.getUserInfo().nickname+"的心动小屋，开启我们的缘分");
        initFloatingView();
        crbUser.setCommentViewVisiblegone(false);
        updateWallet();
//        KeyBoardUtils.setKeyboardChangedListener(this,(softKeybardHeight, visible) -> {
//            crvRoom.llShowGiftBtn.setVisibility(visible ? View.GONE : View.VISIBLE);
//        });
    }
    private void updateWallet() {
        UserManager.getInstance().updateUserWallet()
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<WalletDto>>() {
                    @Override
                    public void _onNext(HttpResult<WalletDto> data) {
                        if (data.isSuccess()) {
                            Long huazun = data.data.preBalance + data.data.balance;
                            UserInfo userInfo = userProviderService.getUserInfo();
                            userInfo.balance = huazun;
                            UserManager.getInstance().setUserInfo(userInfo);

                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    /**
     * 接收聊天信息
     *
     * @param chatBean
     */
    @Override
    public void receiveChatMsg(SocketRequest<WenboWsChatDataBean> chatBean) {

        WenboWsChatDataBean.FromUserBean fromUser = chatBean.data.getFromUser();
        //在主播间给主播发消息
        //anchorId 点前主播id
        //mUserId  当前用户id
        //From 表示发送方
        //to   表示接收方

        String mUserId = String.valueOf(UserManager.getInstance().getUser().getId());
        KLog.e("tag", "" + mUserId);

        if (chatBean.msgType == MsgType.comeRevocation) {//消息撤回
            crvRoom.comeRevoke(chatBean.data.getContent().getWithdrawId());
        } else {
            //如果消息发给我，且发送消息的主播不是当前主播--《弹幕》
            if (null != chatBean.toUid && chatBean.toUid.equals(mUserId)
                    && !fromUser.getUserId().equals(String.valueOf(anchorId))) {
                showChatAnimation(chatBean, null);
            } else {
                crvRoom.getChatAdapter().addData(chatBean);
                //显示接收数据
                crvRoom.scrollLast(0);
            }
        }

//        // from 是自己并且to是主播  就展示消息
//        if (fromUser.getUserId().equals(mUserId)
//                && toUser.getUserId().equals(String.valueOf(anchorId))) {
//            crvRoom.getChatAdapter().addData(chatBean);
//            //显示接收数据
//            crvRoom.scrollLast(0);
//        }
//        //如果 to 是自己 并且不是当前直播发的消息 就弹幕
//        else if (null != toUser.getUserId() && toUser.getUserId().equals(mUserId)
//                && !fromUser.getUserId().equals(String.valueOf(anchorId))) {
//            showChatAnimation(chatBean);
//        } else if (null != toUser.getUserId() && toUser.getUserId().equals(mUserId) && fromUser.getUserId().equals(String.valueOf(anchorId))) {
//            crvRoom.getChatAdapter().addData(chatBean);
//            //显示接收数据
//            crvRoom.scrollLast(0);
//        }
    }

    /**
     * 接收到通知
     */
    private void receiveNotice() {

        RxBus.get().toObservable(ChatEvaluateEvent.class).compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {//三阶段结束 点击去评价弹出评价弹框
                    showFinishChatPopwindow(false, false);
                });
        /*支付成功刷新金额*/
        RxBus.get().toObservable(PaySuccessEvent.class).compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    userProviderService.updateWallet(balance -> {
                        giftPopWindow.setBalance(balance);
                        UserInfo userInfo = userProviderService.getUserInfo();
                        userInfo.setBalance(balance);
                        userProviderService.setUsetInfo(userInfo);
                    });
                });
        /*充值成功刷新金额*/
        RxBus.get().toObservable(RechargeSuccessEvent.class).compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    userProviderService.updateWallet(balance -> {
                        giftPopWindow.setBalance(balance);
                        UserInfo userInfo = userProviderService.getUserInfo();
                        userInfo.setBalance(balance);
                        userProviderService.setUsetInfo(userInfo);
                    });
                });
        /*收到 游戏通知  筛子  猜拳 */
        RxBus.get().toObservable(GameEvent.class).compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.getType() != GameEvent.RECEIVE) {
                        p.sendGameMsg(event, getGears());
                    }
                    if (!gameCountBackUtils.isTiming()) {
                        gameCountBackUtils.countBack(6, new CountBackUtils.Callback() {
                            @Override
                            public void countBacking(long time) {
                                crvRoom.getChatAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void finish() {
                            }
                        });
                    } else {
                        gameCountBackUtils.updateTime(6);
                    }
                });
    }

    /**
     * 初始化悬浮窗
     */
    private void initFloatingView() {
        if (FloatingView.getInstance().isShow()) {
            FloatingView.getInstance().hide();
        }
        FloatingView.getInstance().add();
        FloatingView.getInstance().setStartParams(anchorId, status, bgUrl);
        if (mMagnetViewListener == null) {
            mMagnetViewListener = new MagnetViewListener() {
                @Override
                public void onRemove(FloatingMagnetView magnetView) {
//                Toast.makeText(BaseChatRoomButtonActivity.this, "我没了", Toast.LENGTH_SHORT).show();
                    KLog.d(TAG, "悬浮窗关闭");
                    if (isForeground) {
                        FloatingView.getInstance().hide();
                        clickSwitch();
                    }
                }

                @Override
                public void onClick(FloatingMagnetView magnetView) {
                    KLog.d(TAG, "点击悬浮窗");
                    FloatingView.getInstance().hide();
                    if (isForeground) {
                        clickSwitch();
                    }
                }
            };
        }
        FloatingView.getInstance().listener(mMagnetViewListener);
    }

    /**
     * 断网监听
     */
    private void networkListener() {
        netStatusPopwindow = new HeaderPopWindow(this, bgUrl, "视频连线不成功", "网络异常，请检查网络设置", "查看网络设置", "重新连线");
        netStatusPopwindow.setOnBtnClickListener(new HeaderPopWindow.OnBtnClickListener() {
            @Override
            public void sure() {
                if (CommentUtils.isNetworkConnected(ChatRoomActivity.this)) {
                    if (roomBean == null) return;
                    p.enterRoom(roomBean.getAnchorId());
                    p.isAttention(anchorId);
                } else {
                    CommonDialog.getInstance("请稍后重试", false, "确定", "网络异常").addOnDialogListener(new CommonDialog.OnDialogListener() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void sure() {
                            finish();
                        }
                    }).show(getSupportFragmentManager(), "NetWorkError");
                }
            }

            @Override
            public void cancle() {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                finish();
            }
        });
        netWorkChangReceiver = new NetWorkChangReceiver();
        netWorkChangReceiver.setOnNetStatusChangeListener(new NetWorkChangReceiver.OnNetStatusChangeListener() {
            @Override
            public void onNetUnblocked() {
                if (null != roomBean)
                    KLog.e("tag", "onNetUnblocked == 开始播放视频");
                try {
                    startPlay(roomBean.getStreamPull(), crvRoom.getVideoView(), null);
                } catch (NullPointerException e) {

                }

                if (isFirst) {
                    isFirst = false;
                    return;
                }
                ivBg.setVisibility(View.GONE);
            }

            @Override
            public void onNetBlocked() {
                stopPlay();
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                ivBg.setVisibility(View.VISIBLE);
                netStatusPopwindow.showPopupWindow();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
    }

    String headImg;

    @Override
    public void enterRoomSuccess(EnterRoomBean data) {
        /* 进入直播间成功 */
        roomBean = data;
        livingRoomUserId = roomBean.getLivingRoomUserId();
        anchorId = (int) roomBean.getAnchorId();
        myAnchorId = anchorId;
        roomId = roomBean.getRoomId();
        headImg = roomBean.getHeadImg();
        /*设置礼物弹窗 信息*/
        giftPopWindow.setAnchorInfo(roomBean.getHeadImg(), roomBean.getNickname());
        /*设置直播间 主播信息*/
        p.setRoomInfo(data.getRoomId(), data.getRoomTile());
        /*传入用户 信息*/
        p.setToUserInfo(String.valueOf(data.getAnchorId()), data.getNickname(), data.getHeadImg());
        /*获取聊天历史*/
        p.getHistoryList(true, userProviderService.getUserInfo().getId(), anchorId);

        /*显示聊天*/
        crvRoom.showChatVisible(true);
        /*第一次进入 显示状态*/
        crvRoom.getEditInput().setFirst(false);
        /*显示 缩放按钮*/
        crbUser.setVideoScaleVisibility(true);
        /*播放拉流*/
        pullStreamUrl = data.getStreamPull();
        setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        KLog.e("tag", "enterRoomSuccess ==开始和续费会到这里  开始播放视频");
        if (null != swWindow && swWindow.getVisibility() == View.VISIBLE) {
            clickNarrowWindow();
        } else {
            startPlay(data.getStreamPull(), crvRoom.getVideoView(), null);
        }

        crvRoom.showFreeBg(false, null);
        /*设置用户 信息*/
        crbUser.setHeader(data.getHeadImg());
        crbUser.setName(data.getNickname());
        crbUser.setExpireTime(TimeUtils.long2String(data.getDuration()));
        /*印象值比例*/
        String flirtTime = (String) SPUtils1.get(this, "Flirt_Time", "");
        int intervalTime = TextUtils.isEmpty(flirtTime) ? 1 : Integer.parseInt(flirtTime); // 1印象 = n秒
        long num = (data.getExpireTime() / 1000 - 1) % 10;
        /*观看所剩时间*/
        expireTime = data.getExpireTime();
        FloatingView.getInstance().setExpireTime(expireTime / 1000);
        FloatingView.getInstance().setGear(getGears());
        isPay = data.isIsPay();
        if (expireTime <= 0) {//默认可以看 3秒
            expireTime = 3000;
            crvRoom.getEditInput().setFirst(true);
            crvRoom.showChatVisible(false);
            crbUser.setVideoScaleVisibility(false);
            status = 1;/*设置为未付费*/
        } else {
            /*设置为已付费状态*/
            status = 0;
            crvRoom.setVider();
            isPay = true;

        }
        isFinish = !data.isEvaluated();
        //倒计时
        if (countBackUtils == null) {
            countBackUtils = new CountBackUtils();
        }
        countBackUtils.countBack(expireTime / 1000, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                if (expireTime == 3000) {//设置印象值为1
                    crbUser.setVideoTime(1);

                } else {

                    crbUser.setVideoTime(time / intervalTime); //Todo 设置印象值
                    crbUser.upDataAddTimeEndUI(time / intervalTime < 60 ? true : false);//印象值为1 的时候 开始闪动图标
                    if (time <= 180 && orderNum != 3) {
                        crvRoom.showOpenGiftBtn(true, "缘分时间只剩" + time + "s", "再续缘");
                    }

                    // 300s显示一条公告 “你与xxx缘分即将结束"
                    if (time == 300) {
                        showBulletinMsg(roomBean.getAnchorId() + "", roomBean.getNickname(), "你与", "的缘分即将结束");
                    }
                    if (time < 301 && orderNum == 3 && isFinish) {
                        //     KLog.e("tag", "////////////////////" + time + "//" + orderNum);
                        crvRoom.showOpenGiftBtn(true, "缘分时间只剩" + time + "s", "去评价");
                        if (time == 300) {//第三阶段 时间还剩5分钟跳出评价弹框
                            showFinishChatPopwindow(false, false);
                        }
                    }
                    crbUser.setExpireTime(TimeUtils.long2String((int) (roomBean.getDuration() / 1000 + expireTime / 1000 - time)));
                    FloatingView.getInstance().setExpireTime(time);
                    FloatingView.getInstance().setGear(getGears());
                }
            }


            @Override
            public void finish() {
                isPay = false;
                crvRoom.showOpenGiftBtn(false, null, null);//隐藏屏幕按钮
                crvRoom.showFreeBg(true, bgUrl);//设置背景
                swWindow.setVisibility(View.GONE);
                clickSwitch();
                crvRoom.getEditInput().setChoice();
                /*倒计时结束*/
                crbUser.setVideoTime(0);
                FloatingView.getInstance().setExpireTime(0);
                FloatingView.getInstance().hide();
                if (expireTime == 3000) {
                    //      crvRoom.setVider();
                } else {
                    clickVoice(false);
                }
                if (orderNum == 3 && expireTime != 3000) {//第三阶段缘分结束 弹出评价（评价里面有做只弹出一次）  并且弹出开启缘分提示
                    orderNum = 0;
                    crvRoom.showOpenGiftBtn(true, null, null);
                    if (isFinish) { // 如果评价过了 就弹出开启缘分动画
                        showFinishChatPopwindow(false, false);//缘定三生结束 未评价打开评价弹框
                        crvRoom.showOpenGiftBtn(true, "缘分时间只剩0s", "去评价");
                    } else {
                        // showCenterGiftPop();
                        crvRoom.showOpenGiftBtn(true, "缘分时间只剩0s", "开启缘分");
                    }
                    crvRoom.showOpenGiftBtn(true, MSG_KQYF, "开启");
                    //

                }
                /*请求加时次数*/
                p.getAddTimeNum(anchorId, true);
            }
        });
        /*清除点单浮窗未读消息*/
        HandleMsgManager.getInstance().cleanMes(data.getRoomId());
        //添加按钮
        addFloat(data);
        p.getMyOrder();
        /*请求加时次数*/
        p.getAddTimeNum(anchorId, false);
    }

    /**
     * 添加点单浮窗按钮
     *
     * @param data
     */
    private void addFloat(EnterRoomBean data) {
        if (anchorListButtonView == null) {
            anchorListButtonView = new AnchorListButtonView(this, getSupportFragmentManager());
            fl_chatroom.addView(anchorListButtonView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            anchorListButtonView.addClickViewListener(new AnchorListButtonView.ClickViewListener() {
                @Override
                public void clickView() {
                    p.getMyOrder();
                }
            });
            /*
             * 心动小屋关闭后
             * 获取心动小屋最新数据
             */
            if (anchorListButtonView.myOrderPopwindow != null) {
                anchorListButtonView.myOrderPopwindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        p.getMyOrder();
                    }
                });
            }
            anchorListButtonView.setHeader(data.getHeadImg());
        }
    }

    @Override
    public void getOrderNum(int orderNum, boolean endtime) {

        this.orderNum = orderNum;
        /*获取本场剩余次数*/
        if (endtime) {//印象不够弹出礼物
            if (status == 0) {//有订单
                showAddTimeGif(true, orderNum);
//                if (orderNum == 1 || orderNum == 2) {
//                    KLog.e("tag", "getOrderNum=" + status + "orderNum=" + orderNum);
//                    showAddTimeGif(true, orderNum);
////                handler.sendEmptyMessageDelayed(0, 60); //开启高斯模糊
//                } else {
//              //      p.quitRoom(roomBean.getRoomId() + "");
//                }
            } else {//没有订单  显示礼物蒙版
                KLog.e("tag", "getOrderNum=" + status + "orderNum=" + orderNum);
                showAddTimeGif(true, orderNum);
//            crvRoom.showGiftTime(drawable, true, 1);
//            handler.sendEmptyMessageDelayed(0, 60);//开启高斯模糊
            }
        }
    }

    @Override
    public void getShareInfoSuccess(ShareInfoBean data) {
        UMWeb web = new UMWeb(data.getShareUrl());
        web.setTitle(data.getShareTitle());
        web.setDescription(data.getShareContent());
        if (!TextUtils.isEmpty(data.getShareImg()))
            web.setThumb(new UMImage(this, data.getShareImg()));
        else web.setThumb(new UMImage(this, headImg));
        L.e("=====]SHARE_TYPE " + SHARE_TYPE);
        new ShareAction(this)
                .setPlatform(SHARE_TYPE)//传入平台
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toastTip("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        toastTip("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .share();
    }


    /*高斯模糊*/
//    private SafeHandle handler = new SafeHandle(this) {
//        @Override
//        public void handleMessage(Message msg) {
//            RenderScriptGlassBlur.bitmapBlur(ChatRoomActivity.this, status == 0 ? ivAddBgGift : crvRoom.getBgGift(), crvRoom.getVideoView().getVideoView().getBitmap(), 5);//scaleRatio值越大越模糊
//            handler.sendEmptyMessageDelayed(0, 60);
//        }
//    };

    @Override
    public void enterRoomFailure(String meg) {
        CommonHeaderDialog.getInstance(false, "", meg, "视频连线不成功", "确定").addDialogClick(new CommonHeaderDialog.OnSureClickListener() {
            @Override
            public void sure() {
                finish();
            }
        }).show(getSupportFragmentManager(), "enterRoomFailure");
    }

    @Override
    public void quitRoomSuccess(HttpResult data, boolean isShow) {
//        stopPlay();
        ivBg.setVisibility(View.VISIBLE);
        // crbUser.isShowAnchorInfo(false);
        //隐藏聊天框
        crvRoom.hideChatView(true);
        //隐藏加时
        crbUser.setVideoTimeVisibility(true);
        //隐藏缩放窗口
        crbUser.setVideoScaleVisibility(true);
//这个判断我也没看懂 之前就这么写的
        if (isShow && roomBean != null) {
            showFinishChatPopwindow(true, true);//退出直播间打开评价弹窗
        } else {
            if (null != popwindow) {
                if (!popwindow.isShowing()) {
                    finish();
                }

            } else {
                if (isFinish && roomBean != null && !isPay && 3000 != expireTime) {
                    showFinishChatPopwindow(true, true);//退出直播间打开评价弹窗
                } else
                    finish();
            }

            // 处于缘分状态，并且缘分时间大于5s时显示小窗
            if (isPay && expireTime > 5) {
                openSmallWindow();
            }else {
                FloatingView.getInstance().setPlayUrl("");
            }
        }
    }

    @Override
    public void quitRoomSuccessOut() {
        finish();
    }


    /**
     * 显示结束评价弹窗
     * isfinish //是否需要关闭  傻逼需求  三轮后从新开始第一轮   要在点击关闭-评价-关闭activity
     * finish 是否需要关闭activity
     */
    public void showFinishChatPopwindow(boolean isfinish, boolean finish) {

        if (popwindow != null && popwindow.isShowing() && isFinish) return;
        popwindow = new FinishChatPopwindow(this, anchorId, roomId, livingRoomUserId, userProviderService);
        popwindow.addOnClickListener(new FinishChatPopwindow.OnSureListener() {
            @Override
            public void sure() {//评价
                isFinish = false;
//                if (crbUser.getVideoTime() < 1) {
//                    p.quitRoom(roomId + "");
//                    //         finish();
//                } else { 神一样的代码 谁tm能看懂
                if (isfinish && finish) {
                    p.quitRoomOut(roomId + "", 1);
                }
                if (isfinish) {
                    p.quitRoom(roomId + "", 1);
                } else if (crbUser.getVideoTime() < 1) {//完成评价  如果缘分值小于1  从新开启缘分值 并且弹出礼物动画
                    if (finish) {
                        finish();
                    } else {
                        popwindow.dismiss();
                        orderNum = 0;
                        crvRoom.showOpenGiftBtn(true, MSG_KQYF, "开启");
                    }
                    //     showCenterGiftPop();
                } else {//隐藏提示框
                    crvRoom.showOpenGiftBtn(false, null, null);
                }

            }

            @Override
            public void onSendGift() {
                giftPopWindow.setOnlyGift(true);
                giftPopWindow.setIsCommend(true);
                giftPopWindow.setIsChatRoom(false, false);
                giftPopWindow.showPopupWindow();
            }

            @Override
            public void onFail() {
//                if (crbUser.getVideoTime() < 1) {
//                    finish();
//                } else {

                popwindow.dismiss();
                if (finish) {
                    finish();
                }
                //   }
            }

            @Override
            public void onDis() {
                popwindow.dismiss();
                if (finish) {
                    finish();
                }
            }
        });
        popwindow.showPopupWindow();
        //      releaseCountTime();
    }

    @Override
    public void quitRoomFailure(String msg) {
        L.e("======= " + msg);
        toastTip("退出失败，请重试");
    }

    @Override
    public void updatetime(long time) {
        countBackUtils.updateTime((int) (time / 1000));
        crvRoom.showOpenGiftBtn(false, null, null);
        crbUser.setExpireTime(String.valueOf(time / 1000));
    }

    @Override
    public void anchorClose(ReceiveSocketBean bean) {
        if (Integer.parseInt(bean.getAnchor().getUserId()) != anchorId) return;
        if (expireTime == 3000) {
            DialogUtil.showAlertDialog(this, "提示", "对方已下播", "确定", "", false, new DialogUtil.AlertDialogBtnClickListener() {
                @Override
                public void clickPositive() {
                    finish();
                }

                @Override
                public void clickNegative() {
                    finish();
                }
            });
        } else {
            isPay = false;
            showFinishChatPopwindow(true, true);//主播下播打开评价弹窗
        }
    }

    @Override
    public void acceptOrder(ReceiveSocketBean bean) {
        crbUser.isShowAnchorInfo(true);
        //隐藏聊天框
        crvRoom.hideChatView(false);
        //隐藏加时
        crbUser.setVideoTimeVisibility(true);
        //隐藏缩放窗口
        crbUser.setVideoScaleVisibility(true);
        ivBg.setVisibility(View.GONE);
    }

    @Override
    public void refuseOrder(String value) {
        Intent intent = new Intent();
        intent.putExtra("Content", value);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
//        if (crvRoom != null && crvRoom.getVideoPlay() != null) {
//            crvRoom.getVideoPlay().removeAllViews();
//        }
        super.onDestroy();
        releaseCountTime();
        stopRing();
//        if (handler != null)
//            handler.removeCallbacksAndMessages(null);
        if (netWorkChangReceiver != null) {
            unregisterReceiver(netWorkChangReceiver);
        }
        //释放内存持有
        FloatingView.getInstance().listener(null);
        //FloatingView.getInstance().listener(null);
    }

    private void releaseCountTime() {
        if (countBackUtils != null) {
            countBackUtils.destory();
        }
        if (gameCountBackUtils != null) {
            gameCountBackUtils.destory();
        }
    }

    @Override
    public void clickfinish() {
        clickClose(false);
//        if (!TextUtils.isEmpty(crbUser.getVideoTime()) && Integer.parseInt(crbUser.getVideoTime()) < 1 && expireTime > 3000) {
//            clickClose(false);
//
//        } else {
//            if(expireTime > 3000){
//
//                clickClose(false);
//            }else
//                quitChatRoom();
//          //  quitRoomSuccess(null, false);
//        }
    }


    private void quit() {
//        if (crbUser.getVideoTime() < 1 && expireTime > 3000) {
//            quitChatRoom();
//        } else {
        clickClose(false);
//        }
    }

    /**
     * 退出直播间
     */
    private void quitChatRoom() {
        if (roomBean == null) return;
//        CloseFlirtPlayPop closeFlirtPlayPop = new CloseFlirtPlayPop(this, p, roomBean.getRoomId());
//        closeFlirtPlayPop.setMsg("心动小屋未打烊，确定要走了吗？");
//        closeFlirtPlayPop.showPopupWindow();
        p.quitRoom(roomBean.getRoomId(), 1);
    }

    @Override
    public void onBackPressed() {
        quit();
    }

    @Override
    public void setMyOrderList(List<MyOrderDto> data) {
        if (data == null) return;
        anchorListButtonView.setVisibility(data.size() > 0 ? View.VISIBLE : View.GONE);
        anchorListButtonView.setOrderList(data, roomBean.getRoomId());
    }

    @Override
    public void getAnchorRoomInfoSuccess(GetAnchorRoomInfo data) {
        crvRoom.hideChatView(true);
        giftPopWindow.setAnchorInfo(data.getHeadImg(), data.getNickname());
        p.setRoomInfo(data.getRoomId(), data.getRoomTile());
        p.setToUserInfo(String.valueOf(data.getAnchorId()), data.getNickname(), data.getHeadImg());
        //Todo 播放拉流
        pullStreamUrl = data.getStreamPull();
        setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        KLog.e("tag", "getAnchorRoomInfoSuccess == 开始播放视频");
//        startPlay(data.getStreamPull(), crvRoom.getVideoView(), null);
//        startPlay(data.getStreamPull(), crvRoom.getVideoView(), null);
//        startPlay(data.getStreamPull(), crvRoom.getVideoView(), null);
        crvRoom.showFreeBg(false, null);
        crbUser.setHeader(data.getHeadImg());
        crbUser.setName(data.getNickname());
        crbUser.setVideoTime(1);
        //改变键盘
        crvRoom.getEditInput().setFirst(true);
        crvRoom.showChatVisible(false);
        crbUser.setVideoScaleVisibility(false);
        if (countBackUtils == null) {
            countBackUtils = new CountBackUtils();
        }

        countBackUtils.countBack(3000 / 1000, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {

            }

            @Override
            public void finish() {
                KLog.e("tag", "countBack=" + status + "orderNum=" + orderNum);
//                crvRoom.showGiftTime(R.drawable.ic_gift_first, true, 1);
                showAddTimeGif(true, 0);
//                handler.sendEmptyMessageDelayed(0, 60);

            }
        });

    }


    @Override
    public void getAnchorRoomInfoFailure(String description) {
        toastTip(description);
    }

    @Override
    public void addLoadHistory() {//下拉刷新
        p.getHistoryList(false, UserManager.getInstance().getUser().getId(), anchorId);
    }



    @Override
    public void isWaitStatus(String msg) {
        //   addChatMsgGift(msg);
//        crvRoom.showAddTimeGIF(false);
        p.enterRoom(anchorId);//进入直播间
        showAddTimeGif(false, 1);
        crvRoom.showOpenGiftBtn(false, null, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HandleMsgManager.getInstance().cleanMes(roomId);
    }


    @Override
    public void showMoer() {

        MoerPopwindow moerPopwindow = new MoerPopwindow(this);
        moerPopwindow.setOnItemClickListener(new MoerPopwindow.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {//分享
                    ShareCodeDialog dialog = new ShareCodeDialog();
                    dialog.setOnItemClickListener(new ShareCodeDialog.OnItemClickListener() {
                        @Override
                        public void WeiXinShare() {

                            SHARE_TYPE = SHARE_MEDIA.WEIXIN;
                            p.getShareInfo(anchorId);
                        }

                        @Override
                        public void WeiXinCircleShare() {
                            SHARE_TYPE = SHARE_MEDIA.WEIXIN_CIRCLE;
                            p.getShareInfo(anchorId);
                        }
                    });
                    getSupportFragmentManager().beginTransaction().add(dialog, "share").commitAllowingStateLoss();


                } else {//清屏
                    if (null != userProviderService) {
                        p.delData(0, userProviderService.getUserInfo().getId(), anchorId);
                    }

                    crvRoom.setScreen();

                }
                moerPopwindow.dismiss();
            }

            @Override
            public void isDismiss() {

            }
        });
        moerPopwindow.showPopupWindow();
    }

    @Override
    public void onShowOpenBtn(boolean isShow) {
        crvRoom.showOpenInt(isShow);
    }

    SocketRequest<WenboWsChatDataBean> chatMsg;

    @Override
    public void addGift(WenboGiftMsg gift, boolean istype) {
        //显示发送的礼物消息
//        p.sendText("您 送了1个" + gift.getGift().getGiftName(), MsgType.systemText);
        //  toastTip("您 送了1个" + gift.getGift().getGiftName()); isHost() ? "TA赠送" :
        bigGiftList.add(gift);
        String name = "你赠送";
        if (istype) {
            if (orderNum > 2) {
                chatMsg = addChatMsgGift(name + "达人一个|" + gift.getGift().getGiftName() + "，开启缘分~", istype, gift, "0", true);
            } else
                switch (orderNum) {
                    case 0:
                        chatMsg = addChatMsgGift(name + "达人一个|" + gift.getGift().getGiftName() + "，开启缘分~", istype, gift, "0", true);
                        break;
                    case 1:
                        chatMsg = addChatMsgGift(name + "达人一个|" + gift.getGift().getGiftName() + "，再续前缘~", istype, gift, "1", true);
                        break;
                    case 2:
                        chatMsg = addChatMsgGift(name + "达人一个|" + gift.getGift().getGiftName() + "，情牵一线，缘定三生~", istype, gift, "2", true);
                        break;
                }

        } else {

            chatMsg = addChatMsgGift(name + "达人一个|" + gift.getGift().getGiftName(), istype, gift, getGears(), false);
        }
        //  crvRoom.getChatAdapter().addData(chatMsg);
        crvRoom.scrollLast(0);
        if (!mHandle.hasMessages(WHAT_SHOW_GIFT))
            mHandle.sendEmptyMessage(WHAT_SHOW_GIFT);
    }

    public SocketRequest<WenboWsChatDataBean> addChatMsgGift(String giftName, boolean istype, WenboGiftMsg gift, String gears, boolean isgears) {
        SocketRequest<WenboWsChatDataBean> chatMsg;
        if (istype) {
            chatMsg = WenboMsgManager.getInstant().sendChatMsg(p.userId, userProviderService.getUserInfo().nickname,
                    userProviderService.getUserInfo().headImg, p.talkId, p.talkName, p.talkAvatar, giftName, anchorId + "",
                    giftName, MsgType.fromGiftMsg, 0, gears, istype, gift.getGift().getSmallImgPath(), String.valueOf(anchorId), isgears);
        } else {
            chatMsg = WenboMsgManager.getInstant().sendChatMsg(p.userId, userProviderService.getUserInfo().nickname,
                    userProviderService.getUserInfo().headImg, p.talkId, p.talkName, p.talkAvatar, giftName, anchorId + "",
                    giftName, MsgType.fromGiftMsg, 0, gears, istype, gift.getGift().getSmallImgPath(), String.valueOf(anchorId), isgears);
        }

        crvRoom.getChatAdapter().addData(chatMsg);
        crvRoom.scrollLast(0);
        return chatMsg;
    }

    @Override
    public void clickHeader() {
        /*TODO 点击头像*/
        if (!isHost()) {//只有用户可以点击跳转
            releaseCountTime();
            MineDetailActivity.startActivityWithUserIdForR(this, anchorId);
            if(isPay){//如果正在直播开启小窗
                openSmallWindow();
            }else {
                FloatingView.getInstance().setPlayUrl("");
            }


//            NewUserInfoPopwindow newUserInfoPopwindow = new NewUserInfoPopwindow(this, anchorId, anchorId, 0);
//            newUserInfoPopwindow.showPopupWindow();
        }

        //   ActivitysManager.getInstance().forEach();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10&&isPay)
            p.enterRoom(anchorId);

    }
}
