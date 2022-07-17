package com.fengwo.module_websocket.callback;

import com.fengwo.module_websocket.bean.ChatMessage;

import java.util.List;

public interface OnLivingCallBack {
    void onMessage(List<ChatMessage> msg);
}
