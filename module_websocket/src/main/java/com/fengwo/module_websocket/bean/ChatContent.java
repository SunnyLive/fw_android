package com.fengwo.module_websocket.bean;


public class ChatContent {

    String guid;
    int eventId;
    String fromUid;
    String toUid;
    String forword;
    String message;
    String message_type;
    String addTime;
    String fromHeadImg;
    String fromHeadImgIp;
    String toHeadImg;
    String toHeadImgIp;

    int checkbox_show = 0;
    int checkbox_is_true = 0;


    public int getCheckbox_is_true() {
        return checkbox_is_true;
    }

    public void setCheckbox_is_true(int checkbox_is_true) {
        this.checkbox_is_true = checkbox_is_true;
    }

    public int getCheckbox_show() {
        return checkbox_show;
    }

    public void setCheckbox_show(int checkbox_show) {
        this.checkbox_show = checkbox_show;
    }

    public String getFromHeadImgIp() {
        return fromHeadImgIp;
    }

    public void setFromHeadImgIp(String fromHeadImgIp) {
        this.fromHeadImgIp = fromHeadImgIp;
    }

    public String getToHeadImgIp() {
        return toHeadImgIp;
    }

    public void setToHeadImgIp(String toHeadImgIp) {
        this.toHeadImgIp = toHeadImgIp;
    }

    public String getFromHeadImg() {
        return fromHeadImg;
    }

    public void setFromHeadImg(String fromHeadImg) {
        this.fromHeadImg = fromHeadImg;
    }

    public String getToHeadImg() {
        return toHeadImg;
    }

    public void setToHeadImg(String toHeadImg) {
        this.toHeadImg = toHeadImg;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    boolean isMeSend;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(boolean isMeSend) {
        this.isMeSend = isMeSend;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
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

    public String getForword() {
        return forword;
    }

    public void setForword(String forword) {
        this.forword = forword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}

