package com.fengwo.module_login.mvp.dto;

import com.google.gson.annotations.SerializedName;

public class VerifyTokenDto {

    /**
     * token : 6e5039fdb9ac4802b537dcee32ff6317
     * bizId : 5bf1fcd3-9574-46d8-a72f-9579057798e1
     */

    @SerializedName("token")
    private String token;
    @SerializedName("bizId")
    private String bizId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }
}
