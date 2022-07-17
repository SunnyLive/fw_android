package com.fengwo.module_flirt.bean;

import java.util.Objects;

public class MessageListVO {

    /**
     * action : string
     * anchorId : 0
     * audienceHeadImg : string
     * audienceNickname : string
     * audienceUid : 0
     * expireTime : 0
     * gears : 0
     * id : 0
     * isPay : true
     * lastMsg : string
     * onLine : true
     * roomId : string
     * totalOrderTime : 0
     * updateTime : 2020-10-23T03:17:44.018Z
     */

    private String action;    //业务类型
    private int anchorId;    //主播ID
    private String audienceHeadImg;    //用户头像
    private String audienceNickname;    //用户昵称
    private int audienceUid;    //用户ID
    private int expireTime;    //到期时间
    private int gears;    //阶段
    private int id;
    private boolean isPay;    //0-未支付进入直播间 1-已支付进入直播间
    private String lastMsg;    //最后发言内容
    private boolean onLine;
    private String roomId;    //直播间ID
    private int totalOrderTime;    //点单总时长
    private String updateTime;    //更新时间


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getAudienceHeadImg() {
        return audienceHeadImg;
    }

    public void setAudienceHeadImg(String audienceHeadImg) {
        this.audienceHeadImg = audienceHeadImg;
    }

    public String getAudienceNickname() {
        return audienceNickname;
    }

    public void setAudienceNickname(String audienceNickname) {
        this.audienceNickname = audienceNickname;
    }

    public int getAudienceUid() {
        return audienceUid;
    }

    public void setAudienceUid(int audienceUid) {
        this.audienceUid = audienceUid;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        this.gears = gears;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsPay() {
        return isPay;
    }

    public void setIsPay(boolean isPay) {
        this.isPay = isPay;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getTotalOrderTime() {
        return totalOrderTime;
    }

    public void setTotalOrderTime(int totalOrderTime) {
        this.totalOrderTime = totalOrderTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageListVO that = (MessageListVO) o;
        return anchorId == that.anchorId &&
                audienceUid == that.audienceUid &&
                roomId.equals(that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anchorId, audienceUid, roomId);
    }
}
