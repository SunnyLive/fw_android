package com.fengwo.module_live_vedio.eventbus;

public class BannedPostEvent {
    public int type; //0 静止  1私信

    public BannedPostEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
