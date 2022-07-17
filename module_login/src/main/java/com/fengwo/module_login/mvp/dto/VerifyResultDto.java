package com.fengwo.module_login.mvp.dto;

import com.google.gson.annotations.SerializedName;

public class VerifyResultDto {

    /**
     * code : 0
     * msg : string
     */

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
