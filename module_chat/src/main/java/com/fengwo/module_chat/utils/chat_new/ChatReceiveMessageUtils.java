package com.fengwo.module_chat.utils.chat_new;

import android.text.TextUtils;

import com.fengwo.module_chat.base.ChatSocketRequest;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatSocketBean;
import com.fengwo.module_chat.mvp.model.bean.chat_new.VoiceInfoBean;
import com.fengwo.module_comment.utils.L;
import com.google.gson.Gson;

import java.util.List;

public class ChatReceiveMessageUtils {
    public static ChatMsgEntity createListItem(ChatSocketBean messageModel) {
        int msgType = messageModel.msgType;
        long listDate = messageModel.listDate;
        String sendUserName = messageModel.memeberNickName;
        String sendUserAvatar = messageModel.userAvatarFileName;
        String sendUserId = messageModel.memeberUid;
        String message = messageModel.message;
        String fingerprint = messageModel.fingerPring;
        ChatMsgEntity entity = null;
        switch (msgType) {
            case ChatMsgEntity.MsgType.toText:
                L.e("===listDate "+listDate);
                entity = ChatMsgEntityFactory.buildComeText(sendUserName, sendUserAvatar, message,
                        listDate, fingerprint, false, ChatMsgEntity.SendStatus.beReceived);
                entity.userId = Integer.parseInt(sendUserId);
                return entity;
            case ChatMsgEntity.MsgType.toImage:
                entity = ChatMsgEntityFactory.buildComeImage(sendUserName,
                        sendUserAvatar, message, listDate, fingerprint, false, ChatMsgEntity.SendStatus.beReceived);
                entity.userId = Integer.parseInt(sendUserId);
                return entity;
            case ChatMsgEntity.MsgType.toVoice:
                String messageUrl = messageModel.messageUrl;
                int duration = messageModel.duration;
                VoiceInfoBean infoBean = new VoiceInfoBean(message, duration)
                        .setFileUrl(messageUrl)
                        .setSendId(sendUserId);
                entity = ChatMsgEntityFactory.buildComeVoice(sendUserName, sendUserAvatar,
                        new Gson().toJson(infoBean), listDate, fingerprint, false, ChatMsgEntity.SendStatus.beReceived);
                entity.userId = Integer.parseInt(sendUserId);
                return entity;
            default:
                return null;
        }
    }

    public static ChatMsgEntity createGroupListItem(ChatSocketBean messageModel, String belongUserId) {
        int msgType = messageModel.msgType;
        long listDate = messageModel.listDate;
        String sendUserName = messageModel.memeberNickName;
        String sendUserAvatar = messageModel.userAvatarFileName;
        String sendUserId = messageModel.memeberUid;
        String groupId = messageModel.groupId;
        String message = messageModel.message;
        String fingerprint = messageModel.fingerPring;
        if (TextUtils.equals(belongUserId, sendUserId)) {
            ChatMsgEntity entity;
            switch (msgType) {
                case ChatMsgEntity.MsgType.comeText:
                case ChatMsgEntity.MsgType.toText:
                    entity = ChatMsgEntityFactory.buildText(message, sendUserAvatar, fingerprint, listDate, true, ChatMsgEntity.SendStatus.beReceived);
                    entity.userId = Integer.parseInt(sendUserId);
                    entity.groupId = Integer.parseInt(groupId);
                    return entity;
                case ChatMsgEntity.MsgType.toImage:
                case ChatMsgEntity.MsgType.comeImage:
                    entity = ChatMsgEntityFactory.buildImage(sendUserAvatar, message, fingerprint, listDate, true, ChatMsgEntity.SendStatus.beReceived);
                    entity.userId = Integer.parseInt(sendUserId);
                    entity.groupId = Integer.parseInt(groupId);
                    return entity;
                case ChatMsgEntity.MsgType.toVoice:
                case ChatMsgEntity.MsgType.comeVoice:
                    String messageUrl = messageModel.messageUrl;
                    int duration = messageModel.duration;
                    VoiceInfoBean infoBean = new VoiceInfoBean(message, duration)
                            .setFileUrl(messageUrl)
                            .setSendId(sendUserId);
                    entity = ChatMsgEntityFactory.buildVoice(sendUserAvatar, new Gson().toJson(infoBean), fingerprint, listDate, true, ChatMsgEntity.SendStatus.beReceived);
                    entity.userId = Integer.parseInt(sendUserId);
                    entity.groupId = Integer.parseInt(groupId);
                    return entity;
                default:
                    return null;
            }
        } else {
            ChatMsgEntity entity;
            switch (msgType) {
                case ChatMsgEntity.MsgType.comeText:
                case ChatMsgEntity.MsgType.toText:
                    entity = ChatMsgEntityFactory.buildComeText(sendUserName, sendUserAvatar, message,
                            listDate, fingerprint, true, ChatMsgEntity.SendStatus.beReceived);
                    entity.userId = Integer.parseInt(sendUserId);
                    entity.groupId = Integer.parseInt(groupId);
                    return entity;
                case ChatMsgEntity.MsgType.toImage:
                case ChatMsgEntity.MsgType.comeImage:
                    entity = ChatMsgEntityFactory.buildComeImage(sendUserName,
                            sendUserAvatar, message, listDate, fingerprint, true, ChatMsgEntity.SendStatus.beReceived);
                    entity.userId = Integer.parseInt(sendUserId);
                    entity.groupId = Integer.parseInt(groupId);
                    return entity;
                case ChatMsgEntity.MsgType.toVoice:
                case ChatMsgEntity.MsgType.comeVoice:
                    String messageUrl = messageModel.messageUrl;
                    int duration = messageModel.duration;
                    VoiceInfoBean infoBean = new VoiceInfoBean(message, duration)
                            .setFileUrl(messageUrl)
                            .setSendId(sendUserId);
                    entity = ChatMsgEntityFactory.buildComeVoice(sendUserName, sendUserAvatar,
                            new Gson().toJson(infoBean), listDate, fingerprint, true, ChatMsgEntity.SendStatus.beReceived);
                    entity.userId = Integer.parseInt(sendUserId);
                    entity.groupId = Integer.parseInt(groupId);
                    return entity;
                default:
                    return null;
            }
        }
    }

    public static ChatMsgEntity createEnterGroupEntity(ChatSocketRequest request, String belongUserId) {
        StringBuilder builder = new StringBuilder(" 邀请了 ");
        List<GroupMemberModel> users = request.data.users;
        for (GroupMemberModel user : users) {
            if (TextUtils.equals(request.data.createId, user.uid)) {
                if (TextUtils.equals(belongUserId, user.uid)) {
                    builder.insert(0, "您");
                } else builder.insert(0, user.nickname);
            } else {
                if (TextUtils.equals(belongUserId, user.uid)) {
                    builder.append("您").append(",");
                } else builder.append(user.nickname).append(",");
            }
        }
        String s = builder.substring(0, builder.length() - 1);
        String message = String.format("%s 加入群聊", s);
        return ChatMsgEntityFactory.createInviteIntoGroupMsgEntity(message, System.currentTimeMillis() / 1000, true);
    }

    public static ChatMsgEntity createMemberDeleteEntity(ChatSocketRequest request, String belongUserId) {
        String deleteName = request.data.deleteNickname;
        String s;
        if (TextUtils.equals(request.data.deleteUid, belongUserId)) {
            s = "您已被群主移除";
        } else {
            s = String.format("%s已被群主移除", deleteName);
        }
        return ChatMsgEntityFactory.createDeleteGroupMsgEntity(s, System.currentTimeMillis() / 1000, true);
    }

    public static ChatMsgEntity createMemberQuitEntity(ChatSocketRequest request) {
        return ChatMsgEntityFactory.createDeleteGroupMsgEntity(request.data.msg, System.currentTimeMillis() / 1000, true);
    }

    public static ChatMsgEntity createMemberEnterGroupEntity(ChatSocketRequest request, String belongUserId) {
        String s;
        if (TextUtils.equals(belongUserId, request.data.userId)) {
            s = "您加入了群聊";
        } else s = String.format("%s 加入了群聊", request.data.nickname);
        return ChatMsgEntityFactory.createDeleteGroupMsgEntity(s, System.currentTimeMillis() / 1000, true);
    }

    public static ChatMsgEntity createNoticeEntity(ChatSocketRequest request) {
        return ChatMsgEntityFactory.createNoticeEntity(request.data.message, System.currentTimeMillis() / 1000, false);
    }
}
