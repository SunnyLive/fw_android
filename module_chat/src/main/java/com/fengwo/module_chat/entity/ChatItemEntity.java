package com.fengwo.module_chat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ChatItemEntity {
    @Id
    private Long id;
    private int belongUserId;
    private int talkUserId;
    private int userId;
    private int toId;
    private String sendUserName;
    private String sendUserHeader;
    private long timestamp;
    private String message;
    private int msgType;
    private int sendStatus;
    private int duration;
    private String fileUrl;
    private String fileName;
    private String fingerPrint;
    private boolean isRevocation = false;

    @Generated(hash = 909267541)
    public ChatItemEntity(Long id, int belongUserId, int talkUserId, int userId,
            int toId, String sendUserName, String sendUserHeader, long timestamp,
            String message, int msgType, int sendStatus, int duration,
            String fileUrl, String fileName, String fingerPrint,
            boolean isRevocation) {
        this.id = id;
        this.belongUserId = belongUserId;
        this.talkUserId = talkUserId;
        this.userId = userId;
        this.toId = toId;
        this.sendUserName = sendUserName;
        this.sendUserHeader = sendUserHeader;
        this.timestamp = timestamp;
        this.message = message;
        this.msgType = msgType;
        this.sendStatus = sendStatus;
        this.duration = duration;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fingerPrint = fingerPrint;
        this.isRevocation = isRevocation;
    }
    @Generated(hash = 119742389)
    public ChatItemEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getToId() {
        return this.toId;
    }
    public void setToId(int toId) {
        this.toId = toId;
    }
    public String getSendUserName() {
        return this.sendUserName;
    }
    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
    public String getSendUserHeader() {
        return this.sendUserHeader;
    }
    public void setSendUserHeader(String sendUserHeader) {
        this.sendUserHeader = sendUserHeader;
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
    public String getFingerPrint() {
        return this.fingerPrint;
    }
    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
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

}
