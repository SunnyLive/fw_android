package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/5/6 10:17
 */
public class AppointmentListBean {

    /**
     * id : 36
     * type : 1
     * receiverUid : 513037
     * senderUid : 512026
     * senderNickname : 用户512026
     * senderHeadImg : null
     * appointmentTime : 05月06日20:00-21:00
     * reason : null
     * status : null
     * itemId : 121
     * content : 预约了你的视频交友，花费125花钻
     * createTime : 2020-05-06T10:49:39Z
     * msgData : null
     */

    private int id;
    private int type;
    private int receiverUid;
    private int senderUid;
    private String senderNickname;
    private String senderHeadImg;
    private String appointmentTime;
    private String reason;
    private int status;  // 0 未操作 1 已操作 已同意  2 已拒绝 3.已撤销 4 已过期
    private int itemId;
    private String content;
    private long createTime;
    private Object msgData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(int receiverUid) {
        this.receiverUid = receiverUid;
    }

    public int getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(int senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getSenderHeadImg() {
        return senderHeadImg;
    }

    public void setSenderHeadImg(String senderHeadImg) {
        this.senderHeadImg = senderHeadImg;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getMsgData() {
        return msgData;
    }

    public void setMsgData(Object msgData) {
        this.msgData = msgData;
    }
}
