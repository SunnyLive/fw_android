package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/7/23 10:15
 */
public class GetAnchorRoomInfo {

    /**
     * roomId : 5130841595831119
     * roomTile : 我就是我，是不一样的
     * anchorId : 513084
     * nickname : 用户513084
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1592559590589.jpg
     * expireTime : null
     * streamPull : https://play.yinkehuyu.cn/dev/dev_wenbo_513084.flv
     * orderType : null
     * orderId : null
     * livingRoomUserId : null
     */

    private String roomId;
    private String roomTile;
    private int anchorId;
    private String nickname;
    private String headImg;
    private Object expireTime;
    private String streamPull;
    private Object orderType;
    private Object orderId;
    private Object livingRoomUserId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomTile() {
        return roomTile;
    }

    public void setRoomTile(String roomTile) {
        this.roomTile = roomTile;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Object getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Object expireTime) {
        this.expireTime = expireTime;
    }

    public String getStreamPull() {
        return streamPull;
    }

    public void setStreamPull(String streamPull) {
        this.streamPull = streamPull;
    }

    public Object getOrderType() {
        return orderType;
    }

    public void setOrderType(Object orderType) {
        this.orderType = orderType;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    public Object getLivingRoomUserId() {
        return livingRoomUserId;
    }

    public void setLivingRoomUserId(Object livingRoomUserId) {
        this.livingRoomUserId = livingRoomUserId;
    }
}
