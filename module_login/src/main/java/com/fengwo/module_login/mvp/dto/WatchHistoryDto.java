package com.fengwo.module_login.mvp.dto;

public class WatchHistoryDto {

    /**
     * channelId : 0
     * createTime : 2019-10-29T12:43:29.150Z
     * enterTime : 2019-10-29T12:43:29.150Z
     * headImg : string
     * id : 0
     * leaveTime : 2019-10-29T12:43:29.150Z
     * nickname : string
     * sex : 0
     * signature : string
     * updateTime : 2019-10-29T12:43:29.150Z
     * userId : 0
     */

    private int channelId;
    private String createTime;
    private String enterTime;
    private String headImg;
    private int id;
    private String leaveTime;
    private String nickname;
    private int sex;
    private String signature;
    private String updateTime;
    private String lookLiveTime;
    private int userId;
    private int status;//

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isLiving() {
        return status == 2;
    }

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

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
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

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public String getLookLiveTime() {
        return lookLiveTime;
    }

    public void setLookLiveTime(String lookLiveTime) {
        this.lookLiveTime = lookLiveTime;
    }
}
