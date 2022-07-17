package com.fengwo.module_live_vedio.mvp.dto;

public class LivingMsgDto {
    public static final int TYPE_GIFT_BOARDCAST = 3;
    public static final int TYPE_SYSTEM = 2;
    public static final int TYPE_SEND = 1;
    public String headerurl;
    public String level;
    public String message;
    public String nickname;
    public int type = 1;//

    public LivingMsgDto(String headerurl, String level, String message, String nickname, int type) {
        this.headerurl = headerurl;
        this.level = level;
        this.message = message;
        this.nickname = nickname;
        this.type = type;
    }

    public LivingMsgDto(String message, int type) {
        this.message = message;
        this.type = type;
    }
}
