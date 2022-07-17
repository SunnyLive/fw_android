package com.fengwo.module_chat.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.lang.annotation.Annotation;

@Entity
public class FlirtChatEntity implements Property {
    @Id
    private Long id;
    private int msgType;
    private String toUid;
    private String fromUid;
    private String vendor;
    private String busiEvent;
    @Index(unique = true)
    private String msgId;
    private int sendStatus;
    private String version;
    private String timestamp;
    private String action;

    /* fromUser */
    private String fromUser_userId;
    private String fromUser_nickname;
    private String fromUser_role;
    private String fromUser_headImg;
    /* toUser */
    private String toUser_userId;
    private String toUser_nickname;
    private String toUser_role;
    private String toUser_headImg;
    /* room */
    private String roomId;
    private String roomTitle;
    private String anchorId;
    /* content */
    private String type;
    private String value;
    private long time;
    private int duration;

    /*是否开启缘分*/
    private String isGears;
    //是否是加时礼物
   private int isOrdinaryGift;
//缘分等级
    private String gears;

    @Generated(hash = 942727082)
    public FlirtChatEntity(Long id, int msgType, String toUid, String fromUid, String vendor,
            String busiEvent, String msgId, int sendStatus, String version, String timestamp,
            String action, String fromUser_userId, String fromUser_nickname, String fromUser_role,
            String fromUser_headImg, String toUser_userId, String toUser_nickname, String toUser_role,
            String toUser_headImg, String roomId, String roomTitle, String anchorId, String type,
            String value, long time, int duration, String isGears, int isOrdinaryGift, String gears) {
        this.id = id;
        this.msgType = msgType;
        this.toUid = toUid;
        this.fromUid = fromUid;
        this.vendor = vendor;
        this.busiEvent = busiEvent;
        this.msgId = msgId;
        this.sendStatus = sendStatus;
        this.version = version;
        this.timestamp = timestamp;
        this.action = action;
        this.fromUser_userId = fromUser_userId;
        this.fromUser_nickname = fromUser_nickname;
        this.fromUser_role = fromUser_role;
        this.fromUser_headImg = fromUser_headImg;
        this.toUser_userId = toUser_userId;
        this.toUser_nickname = toUser_nickname;
        this.toUser_role = toUser_role;
        this.toUser_headImg = toUser_headImg;
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.anchorId = anchorId;
        this.type = type;
        this.value = value;
        this.time = time;
        this.duration = duration;
        this.isGears = isGears;
        this.isOrdinaryGift = isOrdinaryGift;
        this.gears = gears;
    }

    @Generated(hash = 2046756783)
    public FlirtChatEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public FlirtChatEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public FlirtChatEntity setMsgType(int msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getToUid() {
        return this.toUid;
    }

    public FlirtChatEntity setToUid(String toUid) {
        this.toUid = toUid;
        return this;
    }

    public String getFromUid() {
        return this.fromUid;
    }

    public FlirtChatEntity setFromUid(String fromUid) {
        this.fromUid = fromUid;
        return this;
    }

    public String getVendor() {
        return this.vendor;
    }

    public FlirtChatEntity setVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getBusiEvent() {
        return this.busiEvent;
    }

    public FlirtChatEntity setBusiEvent(String busiEvent) {
        this.busiEvent = busiEvent;
        return this;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public FlirtChatEntity setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public int getSendStatus() {
        return this.sendStatus;
    }

    public FlirtChatEntity setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public FlirtChatEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public FlirtChatEntity setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getAction() {
        return this.action;
    }

    public FlirtChatEntity setAction(String action) {
        this.action = action;
        return this;
    }

    public String getFromUser_userId() {
        return this.fromUser_userId;
    }

    public FlirtChatEntity setFromUser_userId(String fromUser_userId) {
        this.fromUser_userId = fromUser_userId;
        return this;
    }

    public String getFromUser_nickname() {
        return this.fromUser_nickname;
    }

    public FlirtChatEntity setFromUser_nickname(String fromUser_nickname) {
        this.fromUser_nickname = fromUser_nickname;
        return this;
    }

    public String getFromUser_role() {
        return this.fromUser_role;
    }

    public FlirtChatEntity setFromUser_role(String fromUser_role) {
        this.fromUser_role = fromUser_role;
        return this;
    }

    public String getFromUser_headImg() {
        return this.fromUser_headImg;
    }

    public FlirtChatEntity setFromUser_headImg(String fromUser_headImg) {
        this.fromUser_headImg = fromUser_headImg;
        return this;
    }

    public String getToUser_userId() {
        return this.toUser_userId;
    }

    public FlirtChatEntity setToUser_userId(String toUser_userId) {
        this.toUser_userId = toUser_userId;
        return this;
    }

    public String getToUser_nickname() {
        return this.toUser_nickname;
    }

    public FlirtChatEntity setToUser_nickname(String toUser_nickname) {
        this.toUser_nickname = toUser_nickname;
        return this;
    }

    public String getToUser_role() {
        return this.toUser_role;
    }

    public FlirtChatEntity setToUser_role(String toUser_role) {
        this.toUser_role = toUser_role;
        return this;
    }

    public String getToUser_headImg() {
        return this.toUser_headImg;
    }

    public FlirtChatEntity setToUser_headImg(String toUser_headImg) {
        this.toUser_headImg = toUser_headImg;
        return this;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public FlirtChatEntity setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public String getRoomTitle() {
        return this.roomTitle;
    }

    public FlirtChatEntity setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
        return this;
    }

    public String getAnchorId() {
        return this.anchorId;
    }

    public FlirtChatEntity setAnchorId(String anchorId) {
        this.anchorId = anchorId;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public FlirtChatEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public FlirtChatEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public long getTime() {
        return this.time;
    }

    public FlirtChatEntity setTime(long time) {
        this.time = time;
        return this;
    }

    public int getDuration() {
        return this.duration;
    }

    public FlirtChatEntity setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getIsGears() {
        return this.isGears;
    }

    public FlirtChatEntity setIsGears(String isGears) {
        this.isGears = isGears;
        return this;
    }

    public int getIsOrdinaryGift() {
        return this.isOrdinaryGift;
    }

    public void setIsOrdinaryGift(int isOrdinaryGift) {
        this.isOrdinaryGift = isOrdinaryGift;
    }

    public String getGears() {
        return this.gears;
    }

    public FlirtChatEntity setGears(String gears) {
        this.gears = gears;
        return this;
    }


    @Override
    public String nameInDb() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
