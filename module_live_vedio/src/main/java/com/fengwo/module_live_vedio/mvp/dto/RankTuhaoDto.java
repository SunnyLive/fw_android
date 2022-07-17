package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public class RankTuhaoDto {

    /**
     * score : 40
     * userLevel : 1
     * headImg : null
     * nickname : 用户108
     * value : 108
     * userId : 108
     * anchorLevel : ""
     */


    private double score;
    private String userLevel;
    private String headImg;
    private String nickname;
    private int value;
    private int userId;
    private String anchorLevel;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAnchorLevel() {
        return anchorLevel;
    }

    public void setAnchorLevel(String anchorLevel) {
        this.anchorLevel = anchorLevel;
    }
}
