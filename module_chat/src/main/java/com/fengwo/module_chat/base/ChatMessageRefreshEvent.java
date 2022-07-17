package com.fengwo.module_chat.base;

public class ChatMessageRefreshEvent {
    public String toId;

    public ChatMessageRefreshEvent(String talkerId) {
        this.toId = talkerId;
    }
}