package com.fengwo.module_login.mvp.dto;

public class RecordDto {
    public int id;
    public int userId;
    public int balanceType;
    public String money;
    public int status;
    public String createTime;
    public String updateTime;
    public int payType; //1：支付宝，2：微信 3：苹果商店 4：易宝支付 6:后台补单

    public String getPayType() {
        switch (payType) {
            case 1:
                return "支付宝充值";
            case 2:
                return "微信充值";
            case 3:
                return "苹果商店充值";
            case 4:
                return "易宝支付充值";
            case 6:
                return "补单充值";
            default:
                return "";
        }

    }
}
