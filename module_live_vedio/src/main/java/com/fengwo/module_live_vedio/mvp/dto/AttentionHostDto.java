package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/12
 */
public class AttentionHostDto {
    /**
     * channelId : 0
     * headImg : string
     * nickname : string
     * userId : 0
     * level : string
     */

    private int channelLevel;
    private String headImg;
    private String nickname;
    private int userId;
    private String level;

    public int getChannelLevel() {
        return channelLevel;
    }

    public void setChannelLevel(int channelLevel) {
        this.channelLevel = channelLevel;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLevel() {
        return level;
    }

    public void setUserLevel(String userLevel) {
        this.level = userLevel;
    }
}
