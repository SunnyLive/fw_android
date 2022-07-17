package com.fengwo.module_flirt.manager;

import android.annotation.SuppressLint;

import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.base.RefreshUnReadMessageEvent;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_chat.utils.chat_new.NoticeWebSocketDelegate;
import com.fengwo.module_comment.bean.InvtationBean;
import com.fengwo.module_comment.bean.InvtationDataBean;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_flirt.IM.bean.OrderMessageBean;
import com.fengwo.module_flirt.IM.notify.RefreshFlirtNoticeOrderEvent;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocketWenBo;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 消息存储数据库
 * @Author BLCS
 * @Time 2020/4/28 16:59
 */
public class FlirtSocketManager implements FWWebSocketWenBo.OnSocketConnectListener {
    //接收消息类型
    public final static String RECEIVE_ACK = "ack";/*回执*/
    public final static String RECEIVE_REQ = "req";/*同步*/
    public final static String RECEIVE_MSG = "msg";/*聊天消息*/
    public final static String RECEIVE_CONNECT = "connect";/*客户端连接*/
    public final static String RECEIVE_NOTICE = "notice";/*通知*/
    public final static String RECEIVE_WEB = "msgSync";/*通知*/
    public final static String OFFICIAL_NEW = "official_news";/*通知*/
    private static FlirtSocketManager instance;


    private ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
    //private final int userId;
    private Gson gson;
    private final ChatHistroySQLHelper sQLHelper;

    public static FlirtSocketManager getInstance() {
        synchronized (FlirtSocketManager.class) {
            if (instance == null) {
                synchronized (FlirtSocketManager.class) {
                    if (instance == null) {
                        instance = new FlirtSocketManager();
                    }
                }
            }
        }
        return instance;
    }

    private FlirtSocketManager() {
        gson = new Gson();
        //userId = UserManager.getInstance().getUser().id;
        //connect();
        sQLHelper = ChatHistroySQLHelper.getInstance();
    }

    /**
     * 连接
     */
    public void connect(int uid) {
        FWWebSocketWenBo.getInstance().init(uid);
        FWWebSocketWenBo.getInstance().startRequest();
        FWWebSocketWenBo.getInstance().setOnConnectListener(this);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (FWWebSocketWenBo.getInstance() != null) {
            FWWebSocketWenBo.getInstance().removeListener(this);
            FWWebSocketWenBo.getInstance().closeWebSocket();
            FWWebSocketWenBo.getInstance().destroy();
        }
        instance = null;
    }

    @Override
    public void onMessage(String message) {
        KLog.v("Socket=== " + message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            switch (jsonObject.getString("busiEvent")) {
                case RECEIVE_NOTICE:
                    handleNotice(jsonObject, message);
                    break;
                case OFFICIAL_NEW:
                    NoticeWebSocketDelegate.handleChatMessage(message, String.valueOf(UserManager.getInstance().getUser().id), daoHelper);
                    break;
                case RECEIVE_WEB:
                case RECEIVE_MSG:
                    try {
                        Type type = new TypeToken<SocketRequest<WenboWsChatDataBean>>() {
                        }.getType();
                        SocketRequest<WenboWsChatDataBean> chatBean = gson.fromJson(message, type);
                        HandleMsgManager.getInstance().addMsg(chatBean.data.getRoom().getRoomId());
                        sQLHelper.addChatRecordAsync(chatBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理通知消息
     *
     * @param mesObject
     */
    private void handleNotice(JSONObject mesObject, String msg) {
        try {
            JSONObject data = mesObject.getJSONObject("data");
            String action = data.getString("action");
            if (action.equals("anchorInviteUser")) {
                Gson mGson = new Gson();
                InvtationBean invtationBean = mGson.fromJson(msg, InvtationBean.class);
                RxBus.get().post(new InvtationDataBean(invtationBean.getData().getAnchor().getHeadImg(),
                        invtationBean.getData().getRoom().getAnchorId(),
                        invtationBean.getData().getAnchor().getNickname(),
                        invtationBean.getData().getContent().getValue()));
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject data = mesObject.getJSONObject("data");
            String action = data.getString("action");
            if (action.equals("order") || action.equals("anchorAddTime")) { //点单通知
                OrderMessageBean orderMessageBean = new Gson().fromJson(data.toString(), OrderMessageBean.class);
                RxBus.get().post(new RefreshFlirtNoticeOrderEvent(orderMessageBean));
            } else if (action.equals("appointmentNoticeAnchor") || action.equals("userLaterNoticeAnchor")
                    || action.equals("appointment") || action.equals("appointmenting") || action.equals("appointmented")
                    || action.equals("initiativeNoticeUser") || action.equals("inviteUser") || action.equals("refuseUser") || action.equals("leaveEarly")) {//约会预约
                handleAppointmentNotice(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 预约相关 通知处理
     *
     * @param data
     * @throws JSONException
     */
    @SuppressLint("CheckResult")
    private void handleAppointmentNotice(JSONObject data) {
        ReceiveSocketBean bean = new Gson().fromJson(data.toString(), ReceiveSocketBean.class);

        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, System.currentTimeMillis() / 1000, bean.getContent().getValue(), ChatMsgEntity.MsgType.comeText, false);
        daoHelper.insertOrReplaceList(UserManager.getInstance().getUser().id + "", EventConstant.appoint_event + "",
                null, null,
                null, null, systemEntity, false, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    //刷新消息列表
                    RxBus.get().post(new RefreshMessageListEvent());
                    //刷新未读消息
                    RxBus.get().post(new RefreshUnReadMessageEvent());
                }, Throwable::printStackTrace);
    }

    @Override
    public void onReconnect() {

    }

    @Override
    public void onFaildMsg(String msgid) {
        KLog.e("tag", "到这里了？？");
    }

}
