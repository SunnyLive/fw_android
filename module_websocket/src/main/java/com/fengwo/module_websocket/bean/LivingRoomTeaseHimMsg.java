package com.fengwo.module_websocket.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fengwo.module_websocket.SendStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.PUT;

public class LivingRoomTeaseHimMsg extends BaseMsg {
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

    public LivingRoomTextMsg data;

    public LivingRoomTextMsg liaoData;


    public long getTimestamp() {
        return TextUtils.isEmpty(timestamp)?0:Long.parseLong(timestamp);
    }


    public void msgSuccess() {
        sendStatus = SendStatus.beReceived;
    }

    public void msgFaild() {
        sendStatus = SendStatus.sendFaild;
    }

}
