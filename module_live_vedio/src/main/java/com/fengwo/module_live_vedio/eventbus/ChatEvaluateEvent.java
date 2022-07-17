package com.fengwo.module_live_vedio.eventbus;

public class ChatEvaluateEvent {
    public static final int GIFT_TYPE_NORMAL = 1;
    public static final int GIFT_TYPE_TOUTIAO = 2;
    private int type;

    public ChatEvaluateEvent(int t) {
        type = t;

    }

    public int getType() {
        return type;
    }
}
