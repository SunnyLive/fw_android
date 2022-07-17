package com.fengwo.module_websocket.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fengwo.module_websocket.SendStatus;
import com.google.gson.annotations.Expose;

public class SocketRequest<T> implements MultiItemEntity {
    public String eventId;//旧消息需要

    public int msgType;
    public int sendStatus;

    public String fromUid;
    public String toUid;
    public String version = "2.0";
    public String msgId;
    public String vendor;
    public String timestamp;
    public String busiEvent;

    public T data;


    public long getTimestamp() {
        return TextUtils.isEmpty(timestamp)?0:Long.parseLong(timestamp);
    }

    @Override
    public int getItemType() {
        return msgType;
    }

    public void msgSuccess() {
        sendStatus = SendStatus.beReceived;
    }

    public void msgFaild() {
        sendStatus = SendStatus.sendFaild;
    }

    public void msgDing() {
        sendStatus = SendStatus.sending;
    }
    public void msgComeBack() {
        sendStatus = SendStatus.comeBack;
    }
}
