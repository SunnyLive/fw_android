package com.fengwo.module_chat.base;

import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
/**
 * 接收IM消息
 * @Author BLCS
 * @Time 2020/3/12 10:27
 */
public class RefreshChatMessageEvent {
    public ChatMsgEntity bean;
    public int type;
    public static final int TYPE_SEND =0x01; // 发送消息成功 刷新发送消息状态
    public static final int TYPE_RECEIVE =0x02; // 接收到消息 刷新消息
    public RefreshChatMessageEvent(ChatMsgEntity chatMsgEntity, int type) {
        this.bean = chatMsgEntity;
        this.type = type;
    }
}