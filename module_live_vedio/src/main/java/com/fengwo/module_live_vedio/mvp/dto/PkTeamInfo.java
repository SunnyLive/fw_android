package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class PkTeamInfo implements Serializable {

    /**
     * headImg : string
     * isOver : string
     * nickname : string
     * userId : 0
     */

    private String headImg;
    private String isOver;
    private String nickname;
    private int userId;



    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
