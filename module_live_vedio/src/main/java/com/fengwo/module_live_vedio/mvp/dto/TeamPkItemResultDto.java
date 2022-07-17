package com.fengwo.module_live_vedio.mvp.dto;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class TeamPkItemResultDto implements Serializable {
    /**
     * headImg : string
     * isOver : 0
     * isWin : 0
     * nickname : string
     * objectHeadImg : string
     * objectId : 0
     * objectIsOver : 0
     * objectIsWin : 0
     * objectNickName : string
     * userId : 0
     */

    private String headImg;
    private int isOver;
    private int isWin;
    private String nickname;
    private String objectHeadImg;
    private int objectId;
    private int objectIsOver;
    private int objectIsWin;
    private String objectNickName;
    private int userId;

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

    public int getIsWin() {
        return isWin;
    }

    public void setIsWin(int isWin) {
        this.isWin = isWin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getObjectHeadImg() {
        return objectHeadImg;
    }

    public void setObjectHeadImg(String objectHeadImg) {
        this.objectHeadImg = objectHeadImg;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectIsOver() {
        return objectIsOver;
    }

    public void setObjectIsOver(int objectIsOver) {
        this.objectIsOver = objectIsOver;
    }

    public int getObjectIsWin() {
        return objectIsWin;
    }

    public void setObjectIsWin(int objectIsWin) {
        this.objectIsWin = objectIsWin;
    }

    public String getObjectNickName() {
        return objectNickName;
    }

    public void setObjectNickName(String objectNickName) {
        this.objectNickName = objectNickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
