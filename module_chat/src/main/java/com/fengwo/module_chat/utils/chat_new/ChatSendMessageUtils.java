package com.fengwo.module_chat.utils.chat_new;

import com.fengwo.module_chat.base.ChatSocketRequest;
import com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatSocketBean;
import com.fengwo.module_websocket.EventConstant;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;

import static com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean.TYPE_GROUP;
import static com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean.TYPE_SINGLE;

/**
 * 发送消息数据结构
 */
public class ChatSendMessageUtils {
    /**
     * 生成文字聊天构造体
     */
    public static String createTextMessage(String uid, String toId, String message, long timestamp,
                                           String nickName, String avatar, String fingerPrint, Boolean isGroup, String groupName,
                                           String groupAvatar,int userType) {
        ChatSocketBean chatSocketBean = ChatSocketBean.builder()
                .setOtherUid(toId)
                .setMemeberUid(uid)
                .setMemeberNickName(nickName)
                .setUserAvatarFileName(avatar)
                .setMsgType(ChatMsgEntity.MsgType.toText)
                .setListDate(timestamp)
                .setDate(timestamp)
                .setFingerPring(fingerPrint)
                .setMessage(message)
                .setGroupId(isGroup ? toId : null)
                .setGroupName(isGroup ? groupName : null)
                .setGroupIMG(isGroup ? groupAvatar : null);
        ChatSocketRequest request = ChatSocketRequest.builder()
                .setChatType(0).setFromUid(uid)
                .setIsGroupMsg(isGroup)
                .setToUid(toId).setGroupId(toId)
                .setEventId(isGroup ? EventConstant.broadcastMessage : EventConstant.saveAMessage)
                .setUsertype(userType)
                .setData(chatSocketBean, message);
        return new Gson().toJson(request);
    }

    /**
     * 生成图片聊天构造体
     */
    public static String createImageMessage(String uid, String toId, String imgUrl, long timestamp,
                                            String nickName, String avatar, String fingerPrint, Boolean isGroup, String groupName, String groupAvatar,int userType) {
        ChatSocketBean chatSocketBean = ChatSocketBean.builder()
                .setOtherUid(toId)
                .setMemeberUid(uid)
                .setMemeberNickName(nickName)
                .setUserAvatarFileName(avatar)
                .setMsgType(ChatMsgEntity.MsgType.toImage)
                .setListDate(timestamp)
                .setDate(timestamp)
                .setFingerPring(fingerPrint)
                .setMessage(imgUrl)
                .setGroupId(isGroup ? toId : null)
                .setGroupName(isGroup ? groupName : null)
                .setGroupIMG(isGroup ? groupAvatar : null);
        ChatSocketRequest request = ChatSocketRequest.builder()
                .setChatType(0)
                .setFromUid(uid)
                .setIsGroupMsg(isGroup)
                .setToUid(toId)
                .setGroupId(toId)
                .setUsertype(userType)
                .setEventId(isGroup ? EventConstant.broadcastMessage : EventConstant.saveAMessage)
                .setData(chatSocketBean, imgUrl);
        return new Gson().toJson(request);
    }


    /**
     * 生成图片聊天构造体
     */
    public static String createVoiceMessage(String uid, String toId, long timestamp,
                                            String nickName, String avatar, String fileName,
                                            String fileUrl, int duration, String fingerPrint, Boolean isGroup, String groupName,
                                            String groupAvatar,int userType) {
        ChatSocketBean chatSocketBean = ChatSocketBean.builder()
                .setOtherUid(toId)
                .setMemeberUid(uid)
                .setMemeberNickName(nickName)
                .setUserAvatarFileName(avatar)
                .setMsgType(ChatMsgEntity.MsgType.toVoice)
                .setListDate(timestamp)
                .setDate(timestamp)
                .setMessage(fileName)
                .setMessageUrl(fileUrl)
                .setFingerPring(fingerPrint)
                .setGroupId(isGroup ? toId : null)
                .setGroupName(isGroup ? groupName : null)
                .setGroupIMG(isGroup ? groupAvatar : null)
                .setDuration(duration);
        ChatSocketRequest chatSocketRequest = ChatSocketRequest.builder()
                .setChatType(0)
                .setFromUid(uid)
                .setToUid(toId)
                .setIsGroupMsg(isGroup)
                .setGroupId(toId)
                .setUsertype(userType)
                .setEventId(isGroup ? EventConstant.broadcastMessage : EventConstant.saveAMessage)
                .setData(chatSocketBean, fileUrl);
        return new Gson().toJson(chatSocketRequest);
    }

    /**
     * 生成撤回消息的聊天构造体
     */
    public static String createRevocationMessage(String fingerPrint, String uid, String toId, boolean isGroup,int userType) {
        ChatSocketBean chatSocketBean = ChatSocketBean.builder().setFingerPring(fingerPrint).setDate(System.currentTimeMillis() / 1000);
        ChatSocketRequest chatSocketRequest = ChatSocketRequest.builder()
                .setChatType(0)
                .setFromUid(uid)
                .setToUid(toId)
                .setIsGroupMsg(isGroup)
                .setGroupId(toId)
                .setUsertype(userType)
                .setEventId(EventConstant.rebackAChatMessage)
                .setData(chatSocketBean, null);
        return new Gson().toJson(chatSocketRequest);
    }
}
