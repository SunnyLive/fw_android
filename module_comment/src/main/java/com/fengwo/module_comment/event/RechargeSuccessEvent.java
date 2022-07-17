package com.fengwo.module_comment.event;

public class RechargeSuccessEvent {

    public int code; //1:成功

    public RechargeSuccessEvent() {

    }

    public RechargeSuccessEvent(int success) {
        this.code = success;
    }
}
