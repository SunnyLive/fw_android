package com.fengwo.module_chat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class ChatListItemEntity implements Serializable, Comparable<ChatListItemEntity> {
    @Id
    private Long id;
    private int belongUserId;
    private int talkUserId;
    private String userName;
    private String userAvatar;
    private long timestamp;
    private long topTime;
    private String message;
    private int msgType;
    private int sendStatus;
    private int duration;
    private String fileUrl;
    private String fileName;
    private int unreadCount;
    private String groupId;
    private String groupName;
    private String groupAvatar;
    private String fingerPrint;
    private boolean isRevocation = false;

    private static final long serialVersionUID = -1452463816398357943L;

    @Generated(hash = 595099566)
    public ChatListItemEntity(Long id, int belongUserId, int talkUserId, String userName, String userAvatar,
                              long timestamp, long topTime, String message, int msgType, int sendStatus, int duration,
                              String fileUrl, String fileName, int unreadCount, String groupId, String groupName,
                              String groupAvatar, String fingerPrint, boolean isRevocation) {
        this.id = id;
        this.belongUserId = belongUserId;
        this.talkUserId = talkUserId;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.timestamp = timestamp;
        this.topTime = topTime;
        this.message = message;
        this.msgType = msgType;
        this.sendStatus = sendStatus;
        this.duration = duration;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.unreadCount = unreadCount;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
        this.fingerPrint = fingerPrint;
        this.isRevocation = isRevocation;
    }

    @Generated(hash = 1763326638)
    public ChatListItemEntity() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getBelongUserId() {
        return this.belongUserId;
    }

    public void setBelongUserId(int belongUserId) {
        this.belongUserId = belongUserId;
    }

    public int getTalkUserId() {
        return this.talkUserId;
    }

    public void setTalkUserId(int talkUserId) {
        this.talkUserId = talkUserId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAvatar() {
        return this.groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public boolean getIsRevocation() {
        return this.isRevocation;
    }

    public void setIsRevocation(boolean isRevocation) {
        this.isRevocation = isRevocation;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public long getTopTime() {
        return topTime;
    }

    public void setTopTime(long topTime) {
        this.topTime = topTime;
    }

    @Override
    public int compareTo(ChatListItemEntity entity) {
        if (this.topTime > entity.topTime) {
            return -1;
        } else if (this.topTime < entity.topTime) {
            return 1;
        } else {
            return Long.compare(entity.timestamp, this.timestamp);
        }
    }
}