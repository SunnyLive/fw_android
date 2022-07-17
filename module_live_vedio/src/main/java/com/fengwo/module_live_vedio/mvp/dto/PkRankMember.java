package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/10
 */
public class PkRankMember implements Serializable {

    /**
     * isGuard : false
     * headImg :
     * nickname : 用户7008224
     * userRank : 1
     * userId : 7008224
     */

    private boolean isGuard;
    private String headImg;
    private String nickname;
    private int userRank;
    private int userId;

    public PkRankMember(){

    }

    public boolean isIsGuard() {
        return isGuard;
    }

    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
