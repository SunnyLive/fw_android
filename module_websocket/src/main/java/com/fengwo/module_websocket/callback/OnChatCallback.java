package com.fengwo.module_websocket.callback;

import com.fengwo.module_websocket.bean.ChatMessage;

import java.util.List;

public interface OnChatCallback {
    void deleteMsg(String uids);

    void rebackMsg(String guidList);

    void onMessage(List<ChatMessage> msgs);
}
