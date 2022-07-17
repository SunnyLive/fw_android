package com.fengwo.module_live_vedio.mvp.dto;

/**
 * anchor：yzf
 * date：2018/12/12 17:48
 * email：570363518@qq.com
 */
public class ToutiaoBean {
   private String userName;
   private String userAvatar;
   private String hostName;
   private String hostAvatar;
   private String userLevel;
   private String roomId;
   private String giftUrl;
   private String giftName;
   private String giftNum;
   private String giftPrice;
   private String ttl;

    public ToutiaoBean(String userName, String userAvatar, String hostName, String hostAvatar, String userLevel, String roomId, String giftUrl, String giftName, String giftNum, String giftPrice, String leftTime) {
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.hostName = hostName;
        this.hostAvatar = hostAvatar;
        this.userLevel = userLevel;
        this.roomId = roomId;
        this.giftUrl = giftUrl;
        this.giftName = giftName;
        this.giftNum = giftNum;
        this.giftPrice = giftPrice;
        this.ttl = leftTime;
    }

    public String getLeftTime() {
        return ttl;
    }

    public void setLeftTime(String leftTime) {
        this.ttl = leftTime;
    }

    public ToutiaoBean(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(String hostAvatar) {
        this.hostAvatar = hostAvatar;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(String giftNum) {
        this.giftNum = giftNum;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }
}
