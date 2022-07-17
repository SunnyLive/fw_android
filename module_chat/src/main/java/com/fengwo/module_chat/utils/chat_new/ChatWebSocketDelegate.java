package com.fengwo.module_chat.utils.chat_new;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.fengwo.module_chat.base.ChatSocketRequest;
import com.fengwo.module_chat.base.RefreshChatMessageEvent;
import com.fengwo.module_chat.base.RefreshGroupSettingEvent;
import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.base.RefreshUnReadMessageEvent;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatSocketBean;
import com.fengwo.module_chat.mvp.ui.event.ChatGroupResRefreshEvent;
import com.fengwo.module_chat.mvp.ui.event.ChatMessageErrorEvent;
import com.fengwo.module_chat.mvp.ui.event.GroupForbiddenEvent;
import com.fengwo.module_chat.mvp.ui.event.GroupShowBubbleEvent;
import com.fengwo.module_chat.utils.manager.ChatFunManager;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_websocket.EventConstant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

import static com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean.TYPE_GROUP;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public class ChatWebSocketDelegate {
    @SuppressLint("CheckResult")
    public static void handleChatMessage(String msg, String belongUserId, int privateLetter, ChatGreenDaoHelper daoHelper) throws Exception {
        ChatSocketRequest request = new Gson().fromJson(msg, ChatSocketRequest.class);
        if (request == null) return;
        int eventId = request.data.eventId;
        switch (eventId) {
            case EventConstant.saveAMessage:// 接收到单聊消息
                receiveSingleMessage(belongUserId, privateLetter, daoHelper, request);
                break;
            case EventConstant.CMD_SYSTEM_ENTER_GROUP://系统通知-创建小群
                receiveCreatGroup(belongUserId, daoHelper, request, ChatReceiveMessageUtils.createEnterGroupEntity(request, belongUserId), request.data.groupHeadImg);
                break;
            case EventConstant.broadcastMessage: // 系统消息
                if (TextUtils.equals(request.data.actionId, TYPE_GROUP)) { // 群聊消息
                    receiveGroupMessage(belongUserId, daoHelper, request);
                }
                break;
            case EventConstant.CMD_GROUP_FORBIDDEN: // 群禁言
                RxBus.get().post(new GroupForbiddenEvent(request.groupId, true));
                break;
            case EventConstant.CMD_GROUP_REMOVE_FORBIDDEN: // 取消群禁言
                RxBus.get().post(new GroupForbiddenEvent(request.groupId, false));
                break;
            case EventConstant.CMD_GROUP_TOAST_BUBBLE: // 飘气球
                String id = request.data.id;
                String bubbleName = request.data.label;
                RxBus.get().post(new GroupShowBubbleEvent(id, bubbleName, request.groupId));
                break;
            case EventConstant.CMD_MERCHANT_LIST:// 刷新群聊资源
                ChatGroupResRefreshEvent event = new ChatGroupResRefreshEvent(request.data.id,
                        request.data.imgUrl, request.data.linkUrl, request.data.name);
                RxBus.get().post(event);
                break;
            case EventConstant.CMD_GROUP_MEMBER_DELETE:// 群主删除群成员
                receiveDeleteGroupMember(belongUserId, daoHelper, request);
                break;
            case EventConstant.CMD_GROUP_MEMBER_QUIT: // 群成员离开群
                receiveLeaveGroup(belongUserId, daoHelper, request);
                break;
            case EventConstant.CMD_GROUP_ENTER: // 有人加入群聊
                receiveJoinGroup(belongUserId, daoHelper, request);
                break;
            case EventConstant.rebackAChatMessage: // 消息撤回
                ChatFunManager.withdrawMessage(daoHelper, request.data.messageModel.fingerPring, request.isGroupMsg,null);
                break;
            case EventConstant.CMD_CHAT_ERROR:
                if (request.isGroupMsg) {
                    RxBus.get().post(new ChatMessageErrorEvent(request.groupId));
                }
                break;
            case EventConstant.CMD_SEND_SUCCESS://单聊发送成功通知
                handlerSingleCallback(msg, daoHelper);
                break;

        }
    }

    /**
     * 群主踢人
     */
    @SuppressLint("CheckResult")
    private static void receiveDeleteGroupMember(String belongUserId, ChatGreenDaoHelper daoHelper, ChatSocketRequest request) {
        //判断删除人员是不是自己  如果是则会收到通知 其他人员不会收到通知
        L.e("===========" + request.data.deleteUid);
        L.e("===========belongUserId " + belongUserId);
        if (!TextUtils.equals(request.data.deleteUid, belongUserId)) return;
        ChatMsgEntity systemEntity1 = ChatReceiveMessageUtils.createMemberDeleteEntity(request, belongUserId);
        daoHelper.addChatHistoryItem(belongUserId, request.groupId,
                request.fromUid, request.toUid, systemEntity1)
                .flatMap(o -> daoHelper.insertOrReplaceList(belongUserId, request.groupId,
                        request.data.messageModel.memeberNickName, request.data.messageModel.userAvatarFileName,
                        request.data.groupName, request.data.groupImg, systemEntity1, false, request.isGroupMsg))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    //通知聊天列表
                    RxBus.get().post(new RefreshMessageListEvent());
                    //通知聊天界面
                    RxBus.get().post(new RefreshChatMessageEvent(systemEntity1, RefreshChatMessageEvent.TYPE_RECEIVE));
                    //通知未读消息
                    RxBus.get().post(new RefreshUnReadMessageEvent());
                    //隐藏群设置
                    RxBus.get().post(new RefreshGroupSettingEvent());
                }
                , Throwable::printStackTrace);
    }

    /**
     * 群成员离开群
     */
    @SuppressLint("CheckResult")
    private static void receiveLeaveGroup(String belongUserId, ChatGreenDaoHelper daoHelper, ChatSocketRequest request) {
        ChatMsgEntity systemEntity2 = ChatReceiveMessageUtils.createMemberQuitEntity(request);
        daoHelper.addChatHistoryItem(belongUserId, String.valueOf(request.data.groupId),
                request.fromUid, request.toUid, systemEntity2)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o ->
                RxBus.get().post(new RefreshChatMessageEvent(systemEntity2, RefreshChatMessageEvent.TYPE_RECEIVE)), Throwable::printStackTrace);
    }

    /**
     * 有人加入群聊通知
     */
    @SuppressLint("CheckResult")
    private static void receiveJoinGroup(String belongUserId, ChatGreenDaoHelper daoHelper, ChatSocketRequest request) {
        ChatMsgEntity systemEntity3 = ChatReceiveMessageUtils.createMemberEnterGroupEntity(request, belongUserId);
        //1.加入数据库 2.更新聊天列表
        daoHelper.addChatHistoryItem(belongUserId, request.groupId,
                request.fromUid, request.toUid, systemEntity3)
//                        .flatMap(entity -> daoHelper.insertOrReplaceList(belongUserId, request.groupId,
//                                null, null, request.data.groupName,
//                                request.data.groupHeadImg, systemEntity3, false, request.isGroupMsg))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o ->
                RxBus.get().post(new RefreshChatMessageEvent(systemEntity3, RefreshChatMessageEvent.TYPE_RECEIVE)), Throwable::printStackTrace);
    }

    /**
     * 系统通知-创建小群
     */
    @SuppressLint("CheckResult")
    private static void receiveCreatGroup(String belongUserId, ChatGreenDaoHelper daoHelper, ChatSocketRequest request, ChatMsgEntity enterGroupEntity, String groupHeadImg) {
        // 加入数据库
        ChatMsgEntity systemEntity = enterGroupEntity;
        daoHelper.addChatHistoryItem(belongUserId, request.groupId, request.fromUid, request.toUid, systemEntity)
                .flatMap(entity -> daoHelper.insertOrReplaceList(belongUserId, request.groupId,
                        null, null,
                        request.data.groupName, groupHeadImg, systemEntity, false, request.isGroupMsg))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    //刷新消息列表
                    RxBus.get().post(new RefreshMessageListEvent());
                    //刷新未读消息
                    RxBus.get().post(new RefreshUnReadMessageEvent());
                }, Throwable::printStackTrace);
    }

    /**
     * 接收单聊消息
     */
    @SuppressLint("CheckResult")
    private static void receiveSingleMessage(String belongUserId, int privateLetter, ChatGreenDaoHelper daoHelper, ChatSocketRequest request) {
        if (privateLetter == 0 && request.usertype == 1) return;//禁止私信
        ChatSocketBean messageModel = request.data.messageModel;
        ChatMsgEntity item = ChatReceiveMessageUtils.createListItem(messageModel);
        // 加入数据库
        daoHelper.changeUserAvatar(request.fromUid, item.getHeaderImg())
                .flatMap((Function<Boolean, ObservableSource<?>>) aBoolean ->
                        daoHelper.addChatHistoryItem(belongUserId, request.fromUid, request.fromUid,
                                request.toUid, item))
                .flatMap(o -> daoHelper.insertOrReplaceList(belongUserId, request.fromUid,
                        messageModel.memeberNickName, messageModel.userAvatarFileName,
                        null, null, item, request.fromUid.equals(Constants.CURRENT_CHAT_TARGET_ID), request.isGroupMsg))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    //通知刷新聊天列表
                    RxBus.get().post(new RefreshMessageListEvent());
                    //通知刷新未读消息数
                    RxBus.get().post(new RefreshUnReadMessageEvent());
                    //通知刷新聊天界面
                    RxBus.get().post(new RefreshChatMessageEvent(item, RefreshChatMessageEvent.TYPE_RECEIVE));
                }
                , Throwable::printStackTrace);
    }

    /**
     * 接收群聊消息
     */
    @SuppressLint("CheckResult")
    private static void receiveGroupMessage(String belongUserId, ChatGreenDaoHelper daoHelper, ChatSocketRequest request) {
        ChatSocketBean groupMessageModel = request.data.messageModel;
        ChatMsgEntity groupItem = ChatReceiveMessageUtils.createGroupListItem(groupMessageModel,
                belongUserId);
        if (request.fromUid.equals(belongUserId)) {//自己发的消息 刷新消息状态 通知UI
            daoHelper.updateMessageStatus(groupItem.getFingerPrintOfProtocal())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(chatMsgEntity -> {
                //刷新消息状态
                RxBus.get().post(new RefreshChatMessageEvent(groupItem, RefreshChatMessageEvent.TYPE_SEND));
            }, Throwable::printStackTrace);
        } else {
            //存数据库
            daoHelper.changeUserAvatar(request.fromUid, request.data.messageModel.userAvatarFileName)
                    .flatMap((Function<Boolean, ObservableSource<?>>) aBoolean ->
                            daoHelper.addChatHistoryItem(belongUserId, request.groupId,
                                    request.fromUid, request.groupId, groupItem))
                    .flatMap(o -> daoHelper.insertOrReplaceList(belongUserId, request.groupId,
                            request.data.messageModel.memeberNickName, request.data.messageModel.userAvatarFileName,
                            groupMessageModel.groupName, groupMessageModel.groupIMG, groupItem, false, request.isGroupMsg))
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                        //通知刷新聊天列表
                        RxBus.get().post(new RefreshMessageListEvent());
                        //通知刷新未读消息数
                        RxBus.get().post(new RefreshUnReadMessageEvent());
                        //刷新聊天界面
                        RxBus.get().post(new RefreshChatMessageEvent(groupItem, RefreshChatMessageEvent.TYPE_RECEIVE));
                    }
                    , Throwable::printStackTrace);

        }
    }

    /**
     * 单聊发送成功回调
     */
    @SuppressLint("CheckResult")
    private static void handlerSingleCallback(String msg, ChatGreenDaoHelper daoHelper) {
        L.e("======" + msg);
        try {
            JSONObject object = new JSONObject(msg);
            if (object.has("status") && object.getString("status").equals("OK")) {
                daoHelper.updateMessageStatus()
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(chatMsgEntity -> {
                            //刷新消息状态
                            RxBus.get().post(new RefreshChatMessageEvent(chatMsgEntity, RefreshChatMessageEvent.TYPE_SEND));
                        }
                        , Throwable::printStackTrace);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
