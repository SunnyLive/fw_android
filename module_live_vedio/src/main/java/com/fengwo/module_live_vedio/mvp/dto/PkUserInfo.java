package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class PkUserInfo implements Serializable {


    /**
     * headImg : string
     * isOver : 0
     * nickname : string
     * anchorUrl : string
     * userId : 0
     */

    private String headImg;
    private int isOver;
    private String nickname;
    private String anchorUrl;
    private String audienceUrl;//观众端使用flv的地址播放
    private int userId;



    public String getAudienceUrl() {
        return audienceUrl;
    }

    public void setAudienceUrl(String audienceUrl) {
        this.audienceUrl = audienceUrl;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getIsOver() {
        return isOver;
    }

    public void setIsOver(int isOver) {
        this.isOver = isOver;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAnchorUrl() {
        return anchorUrl;
    }

    public void setAnchorUrl(String anchorUrl) {
        this.anchorUrl = anchorUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    public PkUserInfo() {
    }

}
