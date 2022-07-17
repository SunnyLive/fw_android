package com.fengwo.module_live_vedio.mvp.dto;

public class GiftPiaopingDto {

    /**
     * giftTotal : 9999
     * giftIcon : http://fwtv.oss-cn-hangzhou.aliyuncs.com/fengwoUploadFile/backend/qdzs5d131a752b65b.png
     * broadCast : 1
     * giftName : 情定终身
     * anchorNickname : 优秀的烟雾
     * nickname : 用户1143
     * anchorUserId : 1096
     * giftType : 0
     * sendNum : 1
     * userId : 1143
     * toUid : 1143
     * "giftSwf":"http://fwtv.oss-cn-hangzhou.aliyuncs.com/fengwoUploadFile/backend/FLL5dc915a9ccca5.zip"
     */

    private int giftTotal;
    private String giftIcon;
    private int broadCast = -1;
    private String giftName;
    private String anchorNickname;
    private String anchorHeadImg;
    private String giftSwf;
    private String headerurl;
    private String nickname;
    private int headTimes;// 上头条次数
    private int timeLeft;// 剩余倒计时时间
    private int anchorUserId;
    private int giftId;
    private int giftType;
    private int sendNum;
    private int userId;
    private String toUid;
    private int giftFrameRate;

    public int getGiftFrameRate() {
        return giftFrameRate;
    }

    public void setGiftFrameRate(int giftFrameRate) {
        this.giftFrameRate = giftFrameRate;
    }

    public String getGiftSwf() {
        return giftSwf;
    }

    public void setGiftSwf(String giftSwf) {
        this.giftSwf = giftSwf;
    }

    public int getGiftTotal() {
        return giftTotal;
    }

    public void setGiftTotal(int giftTotal) {
        this.giftTotal = giftTotal;
    }

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public int getBroadCast() {
        return broadCast;
    }

    public void setBroadCast(int broadCast) {
        this.broadCast = broadCast;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getAnchorNickname() {
        return anchorNickname;
    }

    public void setAnchorNickname(String anchorNickname) {
        this.anchorNickname = anchorNickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAnchorUserId() {
        return anchorUserId;
    }

    public void setAnchorUserId(int anchorUserId) {
        this.anchorUserId = anchorUserId;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getAnchorHeaderImg() {
        return anchorHeadImg;
    }

    public void setAnchorHeaderImg(String anchorHeaderImg) {
        this.anchorHeadImg = anchorHeaderImg;
    }

    public String getHeaderurl() {
        return headerurl;
    }

    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getHeadTimes() {
        return headTimes;
    }

    public void setHeadTimes(int headTimes) {
        this.headTimes = headTimes;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
