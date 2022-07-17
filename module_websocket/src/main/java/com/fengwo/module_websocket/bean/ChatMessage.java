package com.fengwo.module_websocket.bean;

import android.text.TextUtils;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class ChatMessage {

    String guid;
    String fromUid;
    String toUid;
    String message_type;
    String message;
    String addTime;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getMessageType() {
        if (TextUtils.isEmpty(message_type)) {
            message_type = "0";
        }
        return message_type;
    }

    public void setMessageType(String messageType) {
        this.message_type = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String add_time) {
        this.addTime = add_time;
    }
}
