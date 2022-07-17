package com.fengwo.module_flirt.bean;

public class TimeGiftResponse {

    /**
     * expireTime : 0
     * msg : string
     * type : 0
     */

    private int expireTime;
    private String msg;
    private int type;
    private int conId;

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
