package com.fengwo.module_websocket.my_interface;

public interface OnMessageListener {
    void onLoginSuccess();

    void onFirstComeinMessage(String firstMessage);

    void onLoadMoreMessage(String moreMsg);

    void onDeleteMessage(String delMsg);

    void onRebackMessage(String msg);
}
