package com.fengwo.module_live_vedio.mvp.dto;

public class PKPlayerInfoDTO {
    public String headImg;
    public String nickname;
    public String url;
    public String userId;
    public int isOver;

    public PKPlayerInfoDTO(String headImg, String nickname) {
        this.headImg = headImg;
        this.nickname = nickname;
    }
}
