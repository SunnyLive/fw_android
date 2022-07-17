package com.fengwo.module_websocket.bean;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public enum MessageContentType {
    TEXT(0), IMAGE(1), AUDIO(2);

    public int getType() {
        return type;
    }

    private int type;

    private MessageContentType(int type) {
        this.type = type;
    }

}
