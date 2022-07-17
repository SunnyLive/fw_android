package com.fengwo.module_chat.utils.chat_new;

import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;

public class ChatMsgEntityFactory {
    public static ChatMsgEntity buildText(String message, String headerImg, String fingerPrint, long time, boolean isGroup, int sendStatus) {
        return new ChatMsgEntity("您", headerImg, time, message, ChatMsgEntity.MsgType.toText, fingerPrint, isGroup, sendStatus, ChatMsgEntity.SendStatusSecondary.none);
    }

    public static ChatMsgEntity buildComeText(String nickName, String headerImg, String message, long time, String fingerPrint, boolean isGroup, int sendStatus) {
        return new ChatMsgEntity(nickName, headerImg, time, message, ChatMsgEntity.MsgType.comeText, fingerPrint, isGroup, sendStatus, ChatMsgEntity.SendStatusSecondary.none);
    }

    public static ChatMsgEntity buildVoice(String headerImg, String message, String fingerPrint, long time, boolean isGroup, int sendStatus) {
        return new ChatMsgEntity("您", headerImg, time, message, ChatMsgEntity.MsgType.toVoice, fingerPrint, isGroup, sendStatus, ChatMsgEntity.SendStatusSecondary.pending);
    }

    public static ChatMsgEntity buildComeVoice(String nickName, String headerImg, String fileInfoStr, long time, String fingerPrint, boolean isGroup, int sendStatus) {
        // 当是图片消息时，message里存放的就是语音留言所存放于服务端的文件名
        return new ChatMsgEntity(nickName, headerImg, time, fileInfoStr, ChatMsgEntity.MsgType.comeVoice, fingerPrint, isGroup, sendStatus, ChatMsgEntity.SendStatusSecondary.none);
    }

    public static ChatMsgEntity buildImage(String headerImg, String message, String fingerPrint, long time, boolean isGroup, int sendStatus) {
        // 当是图片消息时，message里存放的就是图片所存放于服务端的文件名（原图而非缩略图的文件名哦）
        return new ChatMsgEntity("您", headerImg, time, message, ChatMsgEntity.MsgType.toImage, fingerPrint, isGroup, sendStatus, ChatMsgEntity.SendStatusSecondary.pending);
    }

    public static ChatMsgEntity buildComeImage(String nickName, String headerImg, String fileName, long time, String fingerPrint, boolean isGroup, int sendStatus) {
        // 当是图片消息时，message里存放的就是图片所存放于服务端的文件名（原图而非缩略图的文件名哦）
        return new ChatMsgEntity(nickName, headerImg, time, fileName, ChatMsgEntity.MsgType.comeImage, fingerPrint, isGroup, sendStatus, ChatMsgEntity.SendStatusSecondary.none);
    }


    public static ChatMsgEntity createRecovationEntity(String nickName, String headerImg, long time, boolean isGroup) {
        return new ChatMsgEntity(nickName, headerImg, time, null, ChatMsgEntity.MsgType.revocation, isGroup);
    }

    public static ChatMsgEntity createInviteIntoGroupMsgEntity(String message, long time, boolean isGroup) {
        return new ChatMsgEntity("", null, time <= 0 ? System.currentTimeMillis() : time, message, ChatMsgEntity.MsgType.inviteIntoGroup, isGroup);
    }

    public static ChatMsgEntity createSystemTxtAttentionMsgEntity(String message, long time, boolean isGroup) {
        return new ChatMsgEntity("", null, time <= 0 ? System.currentTimeMillis() : time, message, ChatMsgEntity.MsgType.systemTxtAttention, isGroup);
    }

    public static ChatMsgEntity createDeleteGroupMsgEntity(String message, long time, boolean isGroup) {
        return new ChatMsgEntity("", null, time <= 0 ? System.currentTimeMillis() : time, message, ChatMsgEntity.MsgType.inviteIntoGroup, isGroup);
    }

    public static ChatMsgEntity createNoticeEntity(String message, long time, boolean isGroup) {
        return new ChatMsgEntity("", null, time <= 0 ? System.currentTimeMillis() : time, message, ChatMsgEntity.MsgType.comeText, isGroup);
    }
}
