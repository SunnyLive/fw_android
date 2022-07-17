package com.fengwo.module_chat.mvp.ui.event;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public class ChatMessageErrorEvent {
    public String talkerUserId;

    public ChatMessageErrorEvent(String talkerUserId) {
        this.talkerUserId = talkerUserId;
    }
}
