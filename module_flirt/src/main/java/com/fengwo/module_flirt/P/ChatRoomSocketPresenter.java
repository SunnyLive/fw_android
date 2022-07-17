package com.fengwo.module_flirt.P;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.event.GameEvent;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.KeyFilterUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_flirt.Interfaces.IChatRoomView;
import com.fengwo.module_flirt.manager.ChatHistroySQLHelper;
import com.fengwo.module_flirt.manager.WenboMsgManager;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocketWenBo;
import com.fengwo.module_websocket.SendStatus;
import com.fengwo.module_websocket.bean.AckMsg;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.ReceiveCommentMsg;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.StatFateMsg;
import com.fengwo.module_websocket.bean.WebboBulletin;
import com.fengwo.module_websocket.bean.WenboAddTimeMsg;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboMsgAction;
import com.fengwo.module_websocket.bean.WenboMsgBusiEvent;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 放 IM 逻辑
 *
 * @Author BLCS
 * @Time 2020/4/6 11:51
 */
public class ChatRoomSocketPresenter extends BaseFlirtPresenter<IChatRoomView> {
    @Autowired
    UserProviderService userService;
    public String userId;
    private FWWebSocketWenBo webSocket;
    private Gson gson;
//    public MutableLiveData<SocketRequest<WenboWsChatDataBean>> refreshData = new MutableLiveData<>(); // 显示自己发的数据

    public MutableLiveData<List<SocketRequest<WenboWsChatDataBean>>> refreshList = new MutableLiveData<>(); // 显示聊天数据
    public String talkId;
    public String talkName;
    public String talkAvatar;
    private String roomId;
    private String roomTitle;
    private final FWWebSocketWenBo.OnSocketConnectListener onSocketConnectListener;
    private final ChatHistroySQLHelper sQLHelper;
    private final LinkedBlockingQueue<String> messageQueue;//缓冲的消息队列
    private boolean isReadMessage = false;
    private final KeyFilterUtils mKeyFilterUtils;

    public ChatRoomSocketPresenter() {
        ARouter.getInstance().inject(this);
        sQLHelper = ChatHistroySQLHelper.getInstance();
        userId = String.valueOf(UserManager.getInstance().getUser().getId());
        gson = new Gson();
        messageQueue = new LinkedBlockingQueue<>();
        mKeyFilterUtils = new KeyFilterUtils();//去重工具
        onSocketConnectListener = new FWWebSocketWenBo.OnSocketConnectListener() {
            @Override
            public void onMessage(String playLoad) {
                KLog.v("=========接收到聊天消息 " + playLoad);
                if (isReadMessage) {
                    updateUiMsg(playLoad);
                } else {
                    messageQueue.offer(playLoad);
                }
            }

            @Override
            public void onReconnect() {

            }

            @Override
            public void onFaildMsg(String msgid) {
                SocketRequest msg = WenboMsgManager.getInstant().getMsgFromMsgId(msgid);
                getView().updateMsgFaild(msg.msgId);
                sQLHelper.editTypeChatMsg(SendStatus.comeBack, msg.msgId);
            }
        };
        webSocket = FWWebSocketWenBo.getInstance();
        if (webSocket == null) {
            KLog.e("socket 异常断开，请重启。");
        } else {
            webSocket.addOnConnectListener(onSocketConnectListener);
        }
    }

    private void updateUiMsg(String playLoad) {
        if (null == getView()) return;
        ((Activity) getView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //KLog.v("=========接收到聊天消息 " + playLoad);
                handMsg(playLoad);
            }
        });
    }


    /**
     * 从缓冲消息队列中读取消息
     */
    @SuppressLint("CheckResult")
    public void startReadMessage() {
        isReadMessage = true;
        Observable.just("")
                .observeOn(Schedulers.io())
                .subscribe(s -> {
                    while (!messageQueue.isEmpty()) {
                        String playLoad = messageQueue.poll();
                        if (playLoad != null) {
                            updateUiMsg(playLoad);
                        }
                    }
                });
    }

    public void addSqMsg(SocketRequest<WenboWsChatDataBean> chatDataBeanSocketRequest) {
        sQLHelper.addChatRecord(chatDataBeanSocketRequest).subscribe();
    }

    /**
     * 获取聊天列表
     */
    private int page = 0;

    /**
     * 获取聊天历史
     *
     * @param belongUserId
     * @param talkUserId
     */
    @SuppressLint("CheckResult")
    public void getHistoryList(boolean isRefresh, int belongUserId, int talkUserId) {
        if (isRefresh) {
            page = 0;
        }
        sQLHelper.getChatHistroy(page, belongUserId, talkUserId).subscribeWith(new LoadingObserver<ArrayList<SocketRequest<WenboWsChatDataBean>>>() {
            @Override
            public void _onNext(ArrayList<SocketRequest<WenboWsChatDataBean>> data) {
                page++;
                if (data != null && data.size() != 0) {
                    //getView().addHistoryMsg(isRefresh, data);
                    L.e("======== getHistoryList " + data.size());
                    for (int i = 0; i < data.size(); i++) {
                        if (WenboMsgAction.SEND_GIFT.equals(data.get(i).data.getAction())) {
//                        data.get(i).msgType = MsgType.systemText;
//                        data.get(i).msgType = MsgType.systemWelcome;
//                        data.get(i).msgType = MsgType.systemGift;
                            if (data.get(i).data.getContent().isOrdinaryGift() == 0) {
                                data.get(i).msgType = MsgType.giftMeg;
                            } else {
                                //  data.get(i).msgType = MsgType.toGiftMsg;

                            }

                        } else if (WenboMsgAction.GAME.equals(data.get(i).data.getAction())) {
                            if (!data.get(i).fromUid.equals(userId)) {
                                data.get(i).msgType = MsgType.fromGameMsg;
                            }
                        } else if (WenboMsgAction.SPLASH.equals(data.get(i).data.getAction())) {
                            data.get(i).msgType = MsgType.splashMeg;
                        } else if (WenboMsgAction.WITHDRAW.equals(data.get(i).data.getAction())) {//消息撤回

                        } else {
                            if (null != data.get(i).fromUid && !data.get(i).fromUid.equals(userId)) {
                                if (data.get(i).data.getContent().getType().equals("voice")) {
                                    data.get(i).msgType = MsgType.comeVoice;
                                } else if (data.get(i).data.getContent().getType().contains("小仙女的私密空间欢迎你")) {
                                    data.get(i).msgType = MsgType.splashMeg;
                                } else {
                                    if (data.get(i).msgType != MsgType.toGiftMsg && data.get(i).msgType != MsgType.fromGiftMsg) {
                                        data.get(i).msgType = MsgType.comeText;
                                    }

                                }
                            }
                        }
                    }
                    getView().addHistoryMsg(isRefresh, data);
                } else {
                    data = new ArrayList<>();
                    getView().addHistoryMsg(isRefresh, data);
                }

            }

            @Override
            public void _onError(String msg) {
                L.e("msg " + msg);
                getView().toastTip(msg);
            }
        });
    }

    public void delData(int page, int belongUserId, int talkUserId) {
        sQLHelper.deleteFlirtChat(page, belongUserId, talkUserId).subscribeWith(new LoadingObserver<ArrayList<SocketRequest<WenboWsChatDataBean>>>() {
            @Override
            public void _onNext(ArrayList<SocketRequest<WenboWsChatDataBean>> data) {
                if (data == null)
                    return;
                L.e("======== getHistoryList " + data.size());
                for (int i = 0; i < data.size(); i++) {
                    //  sQLHelper.deleteChatRecord(data.get(i));
                }

            }

            @Override
            public void _onError(String msg) {

            }
        });
    }

//    public void getHistoryList() {
//        RequestBody build = new WenboParamsBuilder()
//                .put("size", 20)
//                .put("current", page)
//                .put("userId", talkId)
//                .build();
//        addNet(service.getChatHistory(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<SocketRequest<WenboWsChatDataBean>>>() {
//            @Override
//            public void _onNext(BaseListDto<SocketRequest<WenboWsChatDataBean>> data) {
//                page++;
//                for (int i = 0; i < data.records.size(); i++) {
//                    if (WenboMsgAction.SEND_GIFT.equals(data.records.get(i).data.getAction())) {
//                        data.records.get(i).msgType = MsgType.systemText;
//
//                    } else if (WenboMsgAction.GAME.equals(data.records.get(i).data.getAction())) {
//                        if (!data.records.get(i).fromUid.equals(userId)) {
//                            data.records.get(i).msgType = MsgType.fromGameMsg;
//                        }
//                    } else {
//                        if (!data.records.get(i).fromUid.equals(userId)) {
//                            if (data.records.get(i).data.getContent().getType().equals("voice")){
//                                data.records.get(i).msgType = MsgType.comeVoice;
//                            }else{
//                                data.records.get(i).msgType = MsgType.comeText;
//                            }
//
//                        }
//                    }
//                }
//                getView().addHistoryMsg(data.records);
//            }
//
//            @Override
//            public void _onError(String msg) {
//
//            }
//        }));
//    }

    private void handMsg(String playLoad) {
        try {
            //   KLog.d("receive msg: ", playLoad);
            JSONObject json = new JSONObject(playLoad);
            String busiEvent = json.getString("busiEvent");
            String msgId = json.getString("msgId");
            //去重处理
            if (!mKeyFilterUtils.put(msgId)) {
                return;
            }
            switch (busiEvent) {
                case WenboMsgBusiEvent.ACK:
                    if (getView() == null) return;
                    AckMsg ackMsg = gson.fromJson(playLoad, AckMsg.class);
                    SocketRequest msg = WenboMsgManager.getInstant().getMsgFromMsgId(ackMsg.getOriMsgId());
                    sQLHelper.editTypeChatMsg(SendStatus.beReceived, ackMsg.getOriMsgId());
                    if (null == msg) return;
                    if (msg.data instanceof WenboWsChatDataBean) {
                        getView().updateMsgStatus(msg.msgId);
                    }
                    break;
                case WenboMsgBusiEvent.NOTICE://公告
                    String action = json.getJSONObject("data").getString("action");
                    switch (action) {
                        case WenboMsgAction.SEND_GIFT:
                            WenboGiftMsg gift = gson.fromJson(json.getJSONObject("data").toString(), WenboGiftMsg.class);
                            if (null != getView()) {
                                getView().addGift(gift, gift.getGift().isGears());
                            }

                            break;
                        case WenboMsgAction.LIVE_END:
                            ReceiveSocketBean bean = gson.fromJson(json.getJSONObject("data").toString(), ReceiveSocketBean.class);
                            if (null != getView()) getView().anchorClose(bean);
                            break;
                        case WenboMsgAction.LIVE_WARN:
                            ReceiveSocketBean bean1 = gson.fromJson(json.getJSONObject("data").toString(), ReceiveSocketBean.class);
                            if (null != getView()) getView().toastWarn(bean1);
                            break;
                        case WenboMsgAction.LIVE_BANNED:
                            ReceiveSocketBean banned = gson.fromJson(json.getJSONObject("data").toString(), ReceiveSocketBean.class);
                            if (null != getView()) getView().toastBanned(banned);
                            break;
                        case WenboMsgAction.ACCEPT_ORDER:
                            ReceiveSocketBean acceptOrder = gson.fromJson(json.getJSONObject("data").toString(), ReceiveSocketBean.class);
                            acceptOrder(acceptOrder);
                            break;
                        case WenboMsgAction.USER_CANCEL_ORDER://用户取消订单
                            ReceiveSocketBean cancelOrder = gson.fromJson(json.getJSONObject("data").toString(), ReceiveSocketBean.class);
                            if (null != getView()) getView().receiveCancelOrder(cancelOrder);
                            break;
                        case WenboMsgAction.USER_ADDTIME:
                            WenboAddTimeMsg wenboAddTimeMsg = gson.fromJson(json.getJSONObject("data").toString(), WenboAddTimeMsg.class);
                            if (null == getView()) return;
                            if (wenboAddTimeMsg.getAddTimeresult().getAccept() > 0) {
                                getView().updatetime(wenboAddTimeMsg.getAddTimeresult().getExpireTime());
                                getView().toastTip(wenboAddTimeMsg.getContent().getValue());
                            } else {
                                getView().toastTip(wenboAddTimeMsg.getContent().getValue());
                            }
                            RxBus.get().post(new PaySuccessEvent(""));
                            break;
                        case WenboMsgAction.FLIRT_BULLETIN://心动小屋消息
                            L.d("yang", json.getJSONObject("data").toString());
                            WebboBulletin webboBulletin = gson.fromJson(json.getJSONObject("data").toString(), WebboBulletin.class);
                            if (null == getView()) return;
                            getView().notifyBulletinMsg(webboBulletin);
                            break;
                        case WenboMsgAction.SPLASH://im区
                            if (getView() == null) return;
                            WebboBulletin webboBulletinsss = gson.fromJson(json.getJSONObject("data").toString(), WebboBulletin.class);
                            getView().notifyBulletinMsg(webboBulletinsss);
                            break;
                        case WenboMsgAction.ENTER_LIVING_ROOM://用户进入达人直播间
                            if (getView() == null) return;
                            getView().onUserEnterRoom(gson.fromJson(json.getJSONObject("data").toString(), WebboBulletin.class));
                            break;
                        case WenboMsgAction.EXIT_LIVING_ROOM://用户退出达人直播间
                            if (getView() == null) return;
                            getView().onUserExitRoom(gson.fromJson(json.getJSONObject("data").toString(), WebboBulletin.class));
                            break;
                        case WenboMsgAction.STAT_FATE://接收到缘分礼物
                            if (getView() == null) return;
                            getView().onStatFate(gson.fromJson(json.getJSONObject("data").toString(), StatFateMsg.class));
                            break;
                        case WenboMsgAction.RECEIVE_COMMENT:
                            if (getView() == null) return;
                            getView().onReceiveComment(gson.fromJson(json.getJSONObject("data").toString(), ReceiveCommentMsg.class));
                            break;


                    }
                    break;
                case WenboMsgBusiEvent.WEB:
                case WenboMsgBusiEvent.MSG://聊天消息
                    if (getView() == null) return;
                    Type type = new TypeToken<SocketRequest<WenboWsChatDataBean>>() {
                    }.getType();
                    SocketRequest<WenboWsChatDataBean> chatBean = gson.fromJson(playLoad, type);
                    L.e("============msgType " + chatBean.msgType);
                    if (chatBean.data.getAction().equals(WenboMsgAction.GAME)) {
                        if (chatBean.data.getFromUser().getUserId().equals(userId)) {//同步pc消息
                            chatBean.msgType = MsgType.toGameMsg;
                        } else {
                            chatBean.msgType = MsgType.fromGameMsg;
                        }
                        chatBean.data.getContent().setTime(System.currentTimeMillis() + 3000);
                        RxBus.get().post(new GameEvent(GameEvent.RECEIVE));
                    } else if (chatBean.data.getAction().equals(WenboMsgAction.WITHDRAW)) {//消息撤回
                        if (chatBean.data.getFromUser().getUserId().equals(userId)) {//同步pc消息
                            chatBean.msgType = MsgType.toRevocation;
                        } else {
                            chatBean.msgType = MsgType.comeRevocation;
                        }
                        chatBean.msgSuccess();
                        //消息撤回不入库，直接修改库里的数据
                        sQLHelper.revokeChatMsg(false, chatBean.data.getContent().getWithdrawId());
                        getView().receiveMsg(chatBean);
                        return;
                    } else if (chatBean.data.getContent().getType().equals(WenboMsgManager.TYPE_VOICE)) {
                        if (chatBean.data.getFromUser().getUserId().equals(userId)) {//同步pc消息
                            chatBean.msgType = MsgType.toVoice;
                        } else
                            chatBean.msgType = MsgType.comeVoice;
                    } else {
                        if (chatBean.data.getFromUser().getUserId().equals(userId)) {//同步pc消息
                            chatBean.msgType = MsgType.toText;
                        } else {
                            chatBean.msgType = MsgType.comeText;
                        }
                    }
                    if (null != chatBean.data.getIsGears()) {
                        if (chatBean.data.getIsGears().equals("true")) {
                            chatBean.data.setIsGears("1");
                        }
                        if (chatBean.data.getIsGears().equals("false")) {
                            chatBean.data.setIsGears("0");
                        }
                    }

                    chatBean.msgSuccess();
                    //这里等数据入库成功后通知刷新页面，这里不等入库成功可能会出现读取未读消息时有异常
                    if (null == getView()) return;
                    if (busiEvent.equals(WenboMsgBusiEvent.WEB)) {
                        if (chatBean.toUid.equals(talkId)) {
                            getView().receiveMsg(chatBean);
                        }
                    } else {
                        getView().receiveMsg(chatBean);
                    }


//                    sQLHelper.addChatRecord(chatBean)
//                            .subscribe(o -> {
//                                if (null == getView()) return;
//                                getView().receiveMsg(chatBean);
//                            });
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void acceptOrder(ReceiveSocketBean acceptOrder) {

    }

    public void setToUserInfo(String talkId, String talkName, String talkAvatar) {
        KLog.e("tag", "切换直播间 名称：" + talkName + "//////id=" + talkId);
        this.talkId = talkId;
        this.talkName = talkName;
        this.talkAvatar = talkAvatar;
    }

    public void setRoomInfo(String roomId, String roomTitle) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
    }


    /**
     * 发送文本消息
     *
     * @msgType MsgType.toText发送自己消息 // MsgType.systemText 系统消息 显示再中间
     */
    public void sendText(String content, int msgType, String isGears, String anchorId, boolean isPay) {
        sendText(content, msgType, isGears, null, true, anchorId, isPay);
    }

    //快捷回复 调用
    public void sendText(String content, int msgType, String isGears, boolean isReceiveMsg, String anchorId, boolean isPay, WenboWsChatDataBean.FromUserBean fromUserBean) {
        //    sendText(content, msgType, isGears, null, isReceiveMsg, anchorId, isPay);

        UserInfo userInfo = UserManager.getInstance().getUser();
        SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendChatMsg(userId, userInfo.getNickname(), userInfo.getHeadImg(),
                fromUserBean.getUserId(), fromUserBean.getNickname(), fromUserBean.getHeadImg(), content, roomId, roomTitle, msgType, 0, isGears, null, false, WenboMsgManager.TYPE_TEXT, anchorId, isPay);
        //如果是发出的消息撤回则不需要在页面上通知
        if (!isReceiveMsg || null == getView()) return;
        getView().receiveMsg(chatBean);
    }

    public void sendText(String content, int msgType, String isGears, String withdrawId, boolean isReceiveMsg, String anchorId, boolean isPay) {
//        if (Banned()) return;
        UserInfo userInfo = UserManager.getInstance().getUser();
        SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendChatMsg(userId, userInfo.getNickname(), userInfo.getHeadImg(),
                talkId, talkName, talkAvatar, content, roomId, roomTitle, msgType, 0, isGears, withdrawId, false, WenboMsgManager.TYPE_TEXT, anchorId, isPay);
        //如果是发出的消息撤回则不需要在页面上通知
        if (!isReceiveMsg || null == getView()) return;
        getView().receiveMsg(chatBean);
    }

    /**
     * 发送语音
     *
     * @param file
     * @param duration
     */
    public void sendAudioMessage(File file, int duration, String isGears, String anchorId, boolean isPay) {
        UploadHelper uploadHelper = UploadHelper.getInstance(((Activity) getView()).getApplicationContext());
        uploadHelper.doUpload(UploadHelper.TYPE_AUDIOS, file, new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @Override
            public void onSuccess(String url) {
                try {
//                    if (Banned()) return;
                    getVoiceCheckResult(url, anchorId, duration, isGears, isPay);
//                    UserInfo userInfo = UserManager.getInstance().getUser();
//                    SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendChatMsgAudio(userId, userInfo.getNickname(), userInfo.getHeadImg(), talkId, talkName, talkAvatar, url, roomId, roomTitle, MsgType.toVoice, duration, isGears, false, WenboMsgManager.TYPE_VOICE, anchorId, isPay);
//                    if (null == getView()) return;
//                    ((Activity) getView()).runOnUiThread(() -> getView().receiveMsg(chatBean));
                } catch (Exception e) {
                    L.e("语音：" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
            }
        });
    }

    private void sendRealAudioMsg(String voiceUrl, String anchorId, int duration, String isGears, boolean isplay) {
        UserInfo userInfo = UserManager.getInstance().getUser();
        SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendChatMsg(userId,
                userInfo.getNickname(), userInfo.getHeadImg(), talkId, talkName, talkAvatar, voiceUrl,
                roomId, roomTitle, MsgType.toVoice, duration, isGears, false, WenboMsgManager.TYPE_VOICE, anchorId, isplay);
        if (null == getView()) return;
        ((Activity) getView()).runOnUiThread(() -> getView().receiveMsg(chatBean));
    }

    private void sendRealAudioMsgFailed(String voiceUrl, String anchorId, int duration, String isGears, boolean isplay) {
        UserInfo userInfo = UserManager.getInstance().getUser();

        SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().createChatMsg(userId,
                userInfo.getNickname(), userInfo.getHeadImg(), talkId, talkName, talkAvatar, voiceUrl,
                roomId, roomTitle, MsgType.toVoice, duration, isGears, null, false,
                WenboMsgManager.TYPE_VOICE, anchorId, isplay);

        chatBean.sendStatus = SendStatus.sendFaild;
        if (null == getView()) return;
        ((Activity) getView()).runOnUiThread(() -> getView().receiveMsg(chatBean));
    }

    /**
     * 发送小游戏信息
     *
     * @param event
     */
    public void sendGameMsg(GameEvent event, String gears) {
        UserInfo userInfo = UserManager.getInstance().getUser();
        SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendGametMsg(userId, userInfo.getNickname(), userInfo.getHeadImg(), talkId, talkName, talkAvatar, event.getType(), roomId, roomTitle, gears);
        chatBean.data.getContent().setTime(System.currentTimeMillis() + 3000);
        if (null == getView()) return;
        getView().receiveMsg(chatBean);
    }

    /**
     * 禁言处理
     */
    private boolean Banned() {
        //UserInfo 不能放外面 可能导致信息不同步
        UserInfo userInfo = UserManager.getInstance().getUser();
        if (userInfo.privateLetter == 0) {
            getView().toastTip("对方未开通私聊功能");
            return true;
        }
        if (isBanned(UserManager.getInstance().getClosureTime())) {
            getView().toastTip("您已被禁言");
            return true;
        }
        return false;
    }

    /**
     * 是否禁言
     */
    public boolean isBanned(long forbiddenWords) {
        return forbiddenWords > System.currentTimeMillis() ? true : false;
    }

    /**
     * 销毁
     */
    public void clean() {
        if (webSocket != null && onSocketConnectListener != null) {
            L.e("==========删除聊天室监听 ");
            webSocket.removeListener(onSocketConnectListener);
        }
    }

    public void getVoiceCheckResult(String voiceUrl, String anchorId, int duration, String isGears, boolean isplay) {
        // 该接口不需要加密
        LoginApiService baseService = new RetrofitUtils().createApi(LoginApiService.class);
        RequestBody requestBody = new HttpUtils.ParamsBuilder()
                .put("userId", userId + "")
                .put("content", voiceUrl)
                .build();
        baseService.getVoiceCheckResult(requestBody)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (getView() == null) return;
                        if (data != null && data.data != null) {
                            boolean isPass = (boolean) data.data;
                            if (isPass) {
                                sendRealAudioMsg(voiceUrl, anchorId, duration, isGears, isplay);
                            } else {
                                sendRealAudioMsgFailed(voiceUrl, anchorId, duration, isGears, isplay);
                                getView().onVoiceFailed(data.description);
                            }
                        } else {
                            getView().onVoiceFailed(data.description);
                        }

                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        sendRealAudioMsgFailed(voiceUrl, anchorId, duration, isGears, isplay);
                        getView().onVoiceFailed(msg);
                    }
                });
    }

}
