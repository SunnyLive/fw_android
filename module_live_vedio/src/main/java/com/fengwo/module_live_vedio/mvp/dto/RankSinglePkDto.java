package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public class RankSinglePkDto {
    /**
     * channelId : 0
     * headImg : string
     * isAttention : 0
     * loss : 0
     * nickname : string
     * userId : 0
     * userLevel : string
     * vector : 0
     */

    private int channelId;
    private String headImg;
    private int isAttention;
    private int loss;
    private String nickname;
    private int userId;
    private String userLevel;
    private int vector;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
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
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public int getVector() {
        return vector;
    }

    public void setVector(int vector) {
        this.vector = vector;
    }
}
