package com.fengwo.module_login.eventbus;

public class BackToMainEvent {

    public static final int BACK_TO_MAIN = 1;
    public static final int BACK_TO_ILIAO= 2;

    private int backEvent;          // 1 返回首页 2 返回i撩

    public int getBackEvent() {
        return backEvent;
    }

    public void setBackEvent(int backEvent) {
        this.backEvent = backEvent;
    }
}
