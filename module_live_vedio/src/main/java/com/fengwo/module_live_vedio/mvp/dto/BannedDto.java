package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/22
 */
public class BannedDto {

    /**
     * channelId : 0
     * createTime : 2019-10-22T12:18:51.514Z
     * endTime : 2019-10-22T12:18:51.514Z
     * headImg : string
     * id : 0
     * nickname : string
     * type : 0
     * updateTime : 2019-10-22T12:18:51.514Z
     * userId : 0
     * userLevel : 0
     */

    private int channelId;
    private String createTime;
    private String endTime;
    private String headImg;
    private int id;
    private String nickname;
    private int type;
    private String updateTime;
    private int userId;
    private int userLevel;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
}
