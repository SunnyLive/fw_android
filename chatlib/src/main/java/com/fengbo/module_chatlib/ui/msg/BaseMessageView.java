package com.fengbo.module_chatlib.ui.msg;

import com.fengwo.module_websocket.bean.ChatMessage;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public abstract class BaseMessageView implements IMessageView {

    private boolean isSelf = true;

    private int itemType = 1;

    ChatMessage chatMessage;

    public BaseMessageView(ChatMessage msg) {
        this.chatMessage = msg;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String getType() {
        return chatMessage.getMessageType();
    }

    @Override
    public String getMsg() {
        return chatMessage.getMessage();
    }
}
