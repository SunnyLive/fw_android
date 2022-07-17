package com.fengwo.module_comment.bean;

import com.fengwo.module_comment.utils.L;

public class MyOrderDto {


    @Override
    public String toString() {
        return "MyOrderDto{" +
                "anchorId=" + anchorId +
                ", endTime=" + endTime +
                ", evaluationStatus=" + evaluationStatus +
                ", expireTime=" + expireTime +
                ", headImg='" + headImg + '\'' +
                ", livingRoomUserId=" + livingRoomUserId +
                ", nickname='" + nickname + '\'' +
                ", roomId='" + roomId + '\'' +
                ", status=" + status +
                '}';
    }

    /**
     * anchorId : 0
     * endTime : 0
     * evaluationStatus : 0
     * expireTime : 0
     * headImg : string
     * livingRoomUserId : 0
     * nickname : string
     * roomId : string
     * status : 0
     */

    private long anchorId;
    private long endTime;
    private int evaluationStatus;
    private long expireTime;
    private String headImg;
    private long livingRoomUserId;
    private String nickname;
    private String roomId;
    private int status;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(long anchorId) {
        this.anchorId = anchorId;
    }

    public long getEndTime() {
        return endTime - System.currentTimeMillis();
    }

    public boolean refreshTime() {
        return (endTime/1000) % 10 ==Math.abs((getEndTime()/1000)%10) ;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getEvaluationStatus() {
        return evaluationStatus;
    }

    public void setEvaluationStatus(int evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
    }

    public long getExpireTime() {
        return  expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime ;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public long getLivingRoomUserId() {
        return livingRoomUserId;
    }

    public void setLivingRoomUserId(long livingRoomUserId) {
        this.livingRoomUserId = livingRoomUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
