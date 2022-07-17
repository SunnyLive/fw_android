package com.fengwo.module_live_vedio.eventbus;

public class SendGiftToUserEvent {
    private int uid;
    private String userName;

    public SendGiftToUserEvent(int uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }
}
