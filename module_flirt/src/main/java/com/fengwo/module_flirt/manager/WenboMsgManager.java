package com.fengwo.module_flirt.manager;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocket1;
import com.fengwo.module_websocket.FWWebSocketWenBo;
import com.fengwo.module_websocket.SendStatus;
import com.fengwo.module_websocket.Utils;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboMsgAction;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.google.gson.Gson;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WenboMsgManager {
    public final static String TYPE_TEXT = "text";
    public final static String TYPE_VOICE = "voice";
    public final static String TYPE_IMAGE = "image";
    public final static String TYPE_WITHDRAW = "withdraw";

    public static WenboMsgManager getInstant() {
        if (INSTANCE == null) {
            synchronized (WenboMsgManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WenboMsgManager();
                }
            }
        }
        return INSTANCE;
    }


    private Map<String, SocketRequest> messageCache;

    private static WenboMsgManager INSTANCE;


    public Disposable disposable;
    private Gson gson;

    private WenboMsgManager() {
        messageCache = new HashMap<>();


        gson = new Gson();
    }

    public void setSocket3(long time) {
        if(null!=FWWebSocketWenBo.getInstance()){
            FWWebSocketWenBo.getInstance().endHeartBeat(time);
        }else {
//            FlirtSocketManager.init();
//            FWWebSocketWenBo.getInstance().endHeartBeat(time);
        }

        //   Log.d("hexj", "WenboMsgManager init webSocketWenBo=" + webSocketWenBo);

    }


    public void checkReceive(String msgId) {
        SocketRequest msg = messageCache.get(msgId);
        if (null != msg) {

        }
    }


    public SocketRequest<WenboWsChatDataBean> sendChatMsg(String uid, String userNickName,
                                                          String userHeadImg, String talkId,
                                                          String talkName, String talkAvatar,
                                                          String content, String roomId,
                                                          String roomTitle, int msgType, int duration,
                                                          String isGears, boolean isOrdinaryGift,
                                                          String smallImgPath, String anchorId, boolean isPay) {

        return sendChatMsg(uid, userNickName, userHeadImg, talkId, talkName, talkAvatar,
                content, roomId, roomTitle, msgType, duration, isGears, null, isOrdinaryGift, smallImgPath, anchorId, isPay);
    }

    public SocketRequest<WenboWsChatDataBean> sendChatMsg(String uid, String
            userNickName, String userHeadImg, String talkId, String talkName, String talkAvatar,
                                                          String content, String roomId, String roomTitle,
                                                          int msgType, int duration, String isGears,
                                                          String withdrawId, boolean isOrdinaryGift,
                                                          String smallImgPath, String anchorId, boolean isPay) {

        SocketRequest<WenboWsChatDataBean> msg = createChatMsg(uid, userNickName, userHeadImg, talkId,
                talkName, talkAvatar, content, roomId, roomTitle, msgType, duration, isGears, withdrawId,
                isOrdinaryGift, smallImgPath, anchorId, isPay);

        if (msgType == MsgType.toText || msgType == MsgType.toVoice || msgType ==
                MsgType.toRevocation) {
    if(null!=FWWebSocketWenBo.getInstance()){
        FWWebSocketWenBo.getInstance().sendTextMessage(gson.toJson(msg));

        put(msg.msgId, msg);
    }else {

    }

        }
        if (msgType != MsgType.toRevocation) {//消息撤销不入库
            ChatHistroySQLHelper.getInstance().addChatRecord(msg).subscribe();
        }
        return msg;
    }

    public SocketRequest<WenboWsChatDataBean> createChatMsg(String uid, String
            userNickName, String userHeadImg, String talkId, String talkName, String talkAvatar,
                                                            String content, String roomId, String roomTitle, int msgType, int duration, String
                                                                    isGears, String withdrawId, boolean isOrdinaryGift, String smallImgPath, String anchorId, boolean isPay) {

        SocketRequest<WenboWsChatDataBean> msg = new SocketRequest<>();
        msg.fromUid = uid;//谁发的
        msg.toUid = talkId;//谁收的
        msg.msgId = Utils.createMsgId(uid);
        msg.vendor = Utils.getModel();
        msg.timestamp = System.currentTimeMillis() + "";
        msg.busiEvent = "msg";
        WenboWsChatDataBean wenboWsChatDataBean = new WenboWsChatDataBean();
        if (isPay) {
            wenboWsChatDataBean.setIsGears("1");
        } else {
            wenboWsChatDataBean.setIsGears("0");
        }
        wenboWsChatDataBean.setGears(isGears);
        //如果是消息撤回
        if (msgType == MsgType.toRevocation) {
            wenboWsChatDataBean.setAction(WenboMsgAction.WITHDRAW);
        } else if (msgType == MsgType.giftMeg || msgType == MsgType.toGiftMsg || msgType == MsgType.fromGiftMsg) {
            wenboWsChatDataBean.setAction(WenboMsgAction.SEND_GIFT);
        } else {
            wenboWsChatDataBean.setAction(WenboMsgAction.CHAT);
        }
        WenboWsChatDataBean.FromUserBean fromUserBean = new
                WenboWsChatDataBean.FromUserBean();
        fromUserBean.setRole("ROLE_USER");
        fromUserBean.setNickname(userNickName);
        fromUserBean.setUserId(uid);
        fromUserBean.setHeadImg(userHeadImg);
        wenboWsChatDataBean.setFromUser(fromUserBean);
        WenboWsChatDataBean.ToUserBean toUserBean = new WenboWsChatDataBean.ToUserBean();
        toUserBean.setRole("ROLE_ANCHOR");
        toUserBean.setNickname(talkName);
        toUserBean.setUserId(talkId);
        toUserBean.setHeadImg(talkAvatar);
        wenboWsChatDataBean.setToUser(toUserBean);
        WenboWsChatDataBean.RoomBean roomBean = new WenboWsChatDataBean.RoomBean();
        roomBean.setRoomId(roomId);
        roomBean.setRoomTitle(roomTitle);
        roomBean.setAnchorId(anchorId);
        wenboWsChatDataBean.setRoom(roomBean);
        WenboWsChatDataBean.ContentBean contentBean = new
                WenboWsChatDataBean.ContentBean();
        contentBean.setType(msgType == MsgType.toVoice ? TYPE_VOICE : msgType ==
                MsgType.toRevocation ? TYPE_WITHDRAW : TYPE_TEXT);
        contentBean.setValue(content);
        contentBean.setType(smallImgPath);
        contentBean.setWithdrawId(withdrawId);
        contentBean.setDuration(duration);
        contentBean.setOrdinaryGift(isOrdinaryGift ? 0 : 1);
        wenboWsChatDataBean.setContent(contentBean);
        msg.data = wenboWsChatDataBean;
        msg.msgType = msgType;
        msg.sendStatus = SendStatus.sending;
        return msg;
    }

    public SocketRequest<WenboWsChatDataBean> sendGametMsg(String uid, String
            userNickName, String userHeadImg, String talkId, String talkName, String talkAvatar, int
                                                                   gameType,
                                                           String roomId, String
                                                                   roomTitle, String gears) {
        SocketRequest<WenboWsChatDataBean> msg = new SocketRequest<>();
        msg.fromUid = uid;
        msg.toUid = talkId;
        msg.msgId = Utils.createMsgId(uid);
        msg.vendor = Utils.getModel();
        msg.timestamp = System.currentTimeMillis() + "";
        msg.busiEvent = "msg";
        WenboWsChatDataBean wenboWsChatDataBean = new WenboWsChatDataBean();
        wenboWsChatDataBean.setAction(WenboMsgAction.GAME);
        wenboWsChatDataBean.setIsGears(gears);
        WenboWsChatDataBean.FromUserBean fromUserBean = new
                WenboWsChatDataBean.FromUserBean();
        fromUserBean.setRole("ROLE_USER");
        fromUserBean.setNickname(userNickName);
        fromUserBean.setUserId(uid);
        fromUserBean.setHeadImg(userHeadImg);
        wenboWsChatDataBean.setFromUser(fromUserBean);
        WenboWsChatDataBean.ToUserBean toUserBean = new WenboWsChatDataBean.ToUserBean
                ();
        toUserBean.setRole("ROLE_ANCHOR");
        toUserBean.setNickname(talkName);
        toUserBean.setUserId(talkId);
        toUserBean.setHeadImg(talkAvatar);
        wenboWsChatDataBean.setToUser(toUserBean);
        WenboWsChatDataBean.RoomBean roomBean = new WenboWsChatDataBean.RoomBean();
        roomBean.setRoomId(roomId);
        roomBean.setRoomTitle(roomTitle);
        roomBean.setAnchorId(talkId);
        wenboWsChatDataBean.setRoom(roomBean);
        WenboWsChatDataBean.ContentBean contentBean = new
                WenboWsChatDataBean.ContentBean();
        String[] caiquan = {"scissors", "paper", "stone"};
        String[] types = {"finger-guessing", "dice"};
        Random random = new Random();
        String type = "finger-guessing";
        String value = "";
        switch (gameType) {//1猜拳 2骰子
            case 0:
                type = types[0];
                value = caiquan[random.nextInt(3)];
                break;
            case 1:
                type = types[1];
                value = random.nextInt(6) + 1 + "";
                break;
        }
        contentBean.setType(type);
        contentBean.setValue(value);
        wenboWsChatDataBean.setContent(contentBean);
        msg.data = wenboWsChatDataBean;
        msg.msgType = MsgType.toGameMsg;
        msg.sendStatus = SendStatus.sending;
        ChatHistroySQLHelper.getInstance().addChatRecord(msg).subscribe();
        FWWebSocketWenBo.getInstance().sendTextMessage(gson.toJson(msg));
        put(msg.msgId, msg);

        return msg;
    }


    public SocketRequest<WenboWsChatDataBean> sendGametMsgsss(String uid, String
            userNickName, String userHeadImg, String talkId, String talkName, String talkAvatar, int
                                                                      gameType,
                                                              String roomId, String
                                                                      roomTitle) {
        SocketRequest<WenboWsChatDataBean> msg = new SocketRequest<>();
        msg.fromUid = uid;
        msg.toUid = talkId;
        msg.msgId = Utils.createMsgId(uid);
        msg.vendor = Utils.getModel();
        msg.timestamp = System.currentTimeMillis() + "";
        msg.busiEvent = "msg";
        WenboWsChatDataBean wenboWsChatDataBean = new WenboWsChatDataBean();
        wenboWsChatDataBean.setAction(WenboMsgAction.GAME);
        WenboWsChatDataBean.FromUserBean fromUserBean = new
                WenboWsChatDataBean.FromUserBean();
        fromUserBean.setRole("ROLE_USER");
        fromUserBean.setNickname(userNickName);
        fromUserBean.setUserId(uid);
        fromUserBean.setHeadImg(userHeadImg);
        wenboWsChatDataBean.setFromUser(fromUserBean);
        WenboWsChatDataBean.ToUserBean toUserBean = new WenboWsChatDataBean.ToUserBean
                ();
        toUserBean.setRole("ROLE_ANCHOR");
        toUserBean.setNickname(talkName);
        toUserBean.setUserId(talkId);
        toUserBean.setHeadImg(talkAvatar);
        wenboWsChatDataBean.setToUser(toUserBean);
        WenboWsChatDataBean.RoomBean roomBean = new WenboWsChatDataBean.RoomBean();
        roomBean.setRoomId(roomId);
        roomBean.setRoomTitle(roomTitle);
        roomBean.setAnchorId(talkId);
        wenboWsChatDataBean.setRoom(roomBean);
        WenboWsChatDataBean.ContentBean contentBean = new
                WenboWsChatDataBean.ContentBean();
        String[] caiquan = {"scissors", "paper", "stone"};
        String[] types = {"finger-guessing", "dice"};
        Random random = new Random();
        String type = "finger-guessing";
        String value = "";
        switch (gameType) {//1猜拳 2骰子
            case 0:
                type = types[0];
                value = caiquan[random.nextInt(3)];
                break;
            case 1:
                type = types[1];
                value = random.nextInt(6) + 1 + "";
                break;
        }
        contentBean.setType(type);
        contentBean.setValue(value);
        wenboWsChatDataBean.setContent(contentBean);
        msg.data = wenboWsChatDataBean;
        msg.msgType = MsgType.toGameMsg;
        msg.sendStatus = SendStatus.sending;
        ChatHistroySQLHelper.getInstance().addChatRecord(msg).subscribe();
        return msg;
    }

    //不存数据库
    public SocketRequest<WenboWsChatDataBean> sendChatMsgTemporary(String uid, String
            userNickName, String userHeadImg, String talkId, String talkName, String talkAvatar,
                                                                   String content, String roomId, String roomTitle, int msgType, int duration) {
        SocketRequest<WenboWsChatDataBean> msg = new SocketRequest<>();
        msg.fromUid = uid;
        msg.toUid = talkId;
        msg.msgId = Utils.createMsgId(uid);
        msg.vendor = Utils.getModel();
        msg.timestamp = System.currentTimeMillis() + "";
        msg.busiEvent = "msg";
        WenboWsChatDataBean wenboWsChatDataBean = new WenboWsChatDataBean();
        wenboWsChatDataBean.setAction(WenboMsgAction.CHAT);
        WenboWsChatDataBean.FromUserBean fromUserBean = new
                WenboWsChatDataBean.FromUserBean();
        fromUserBean.setRole("ROLE_USER");
        fromUserBean.setNickname(userNickName);
        fromUserBean.setUserId(uid);
        fromUserBean.setHeadImg(userHeadImg);
        wenboWsChatDataBean.setFromUser(fromUserBean);
        WenboWsChatDataBean.ToUserBean toUserBean = new WenboWsChatDataBean.ToUserBean
                ();
        toUserBean.setRole("ROLE_ANCHOR");
        toUserBean.setNickname(talkName);
        toUserBean.setUserId(talkId);
        toUserBean.setHeadImg(talkAvatar);
        wenboWsChatDataBean.setToUser(toUserBean);
        WenboWsChatDataBean.RoomBean roomBean = new WenboWsChatDataBean.RoomBean();
        roomBean.setRoomId(roomId);
        roomBean.setRoomTitle(roomTitle);
        roomBean.setAnchorId(talkId);
        wenboWsChatDataBean.setRoom(roomBean);
        WenboWsChatDataBean.ContentBean contentBean = new
                WenboWsChatDataBean.ContentBean();
        contentBean.setType(msgType == MsgType.toVoice ? TYPE_VOICE : TYPE_TEXT);
        contentBean.setValue(content);
        contentBean.setDuration(duration);
        wenboWsChatDataBean.setContent(contentBean);
        msg.data = wenboWsChatDataBean;
        msg.msgType = msgType;
        msg.sendStatus = SendStatus.sending;
        if (msgType == MsgType.toText || msgType == MsgType.toVoice) {
            FWWebSocketWenBo.getInstance().sendTextMessage(gson.toJson(msg));
            put(msg.msgId, msg);
        }

        return msg;
    }

    //存消息
    public void put(String msgId, SocketRequest msg) {
        messageCache.put(msgId, msg);
        setSocketMsgFor(3);
    }

    boolean isCf = false;

    //未发送重发
    private void setSocketMsgFor(long time) {
        if (messageCache.size() == 0) {
            return;
        }
        if (disposable != null) {
            KLog.e("SocketMsg", "rxjava状态=" + disposable.isDisposed());
        }
        isCf = true;
        disposable = Observable.interval(3, time + 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeWhile(statusInfo -> isCf)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (messageCache.size() == 0 || aLong > time + 1) {
                            isCf = false;
                        } else {
                            KLog.e("SocketMsg", "第" + aLong + "次重发，未发送的消息数量" + messageCache.size());
                            for (String key : messageCache.keySet()) {
                                if (!TextUtils.isEmpty(messageCache.get(key).timestamp)) {
                                    BigDecimal time = new BigDecimal(messageCache.get(key).timestamp);
                                    //    time.add(new BigDecimal("10000"));
                                    BigDecimal DQtime = new BigDecimal(System.currentTimeMillis() + "");
                                    KLog.e("SocketMsg", "发送时间" + time.toString() + "当前时间" + DQtime.toString() + "相差" + DQtime.subtract(time).toString());

                                    if (DQtime.subtract(time).compareTo(new BigDecimal("10000")) == 1) {//发送时间大于当前时间
                                        KLog.e("SocketMsg", "重发消息超时的数据" + messageCache.get(key).data);
                                        FWWebSocketWenBo.getInstance().faildMsg(key);
                                    } else {
                                        //  System.out.println("key= " + key + " and value= " + messageCache.get(key));
                                        FWWebSocketWenBo.getInstance().sendTextMessage(gson.toJson(messageCache.get(key)));
                                        KLog.e("SocketMsg", "正在执行重发消息的数据" + messageCache.get(key).data);
                                    }
                                }

                            }
                        }


                    }
                });
    }

    public static String parseServerTime(String serverTime, String format) {
        if (format == null || format.isEmpty()) {
            format = "HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String sd = sdf.format(new Date(Long.parseLong(serverTime)));
        return sd;
    }

    /**
     * 停止定时执行
     */
    protected void stopTimer() {
        if (null != disposable) {
            disposable.dispose();
            disposable = null;
        }
    }

    //发送成功移除
    public SocketRequest getMsgFromMsgId(String msgId) {
        SocketRequest msg = messageCache.get(msgId);
        if (null != msg) {
            messageCache.remove(msgId);
        }
        if (messageCache.size() == 0 && null != disposable) {
            //  stopTimer();
        }
        return msg;
    }

    //设置某个msgid 的消息 进行重发
    public void setItemMsgFor(String msgid, SocketRequest<WenboWsChatDataBean> data) {
        boolean istype = false;
        for (String key : messageCache.keySet()) {
            if (key.equals(msgid)) {
                messageCache.get(msgid).timestamp = System.currentTimeMillis() + "";
                setSocketMsgFor(3);
                istype = true;
            }
        }
        if (!istype) {
            data.timestamp = System.currentTimeMillis() + "";
            put(msgid, data);
        }
    }
}
