package com.fengwo.module_flirt.IM.bean;

/**
 * @Author BLCS
 * @Time 2020/4/30 17:28
 */
public class ImReceiverBean<T> {
    @Override
    public String toString() {
        return "ImReceiverBean{" +
                "fromUid='" + fromUid + '\'' +
                ", toUid='" + toUid + '\'' +
                ", msgId='" + msgId + '\'' +
                ", version='" + version + '\'' +
                ", vendor='" + vendor + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", busiEvent='" + busiEvent + '\'' +
                ", description='" + description + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * fromUid : -1
     * toUid : 主播ID
     * msgId : 202004231425558571253208463557091322
     * version : 2.0
     * vendor : Im Server
     * timestamp : 15846568956
     * busiEvent : notice
     * description : 点单通知
     * data : {"action":"order","user":{"userId":"60000","nickname":"用户昵称"},"order":{"ordNo":""},"content":{"type":"text","value":"用户昵称1请求接入(半小时，30000花钻)是否接单"}}
     */

    private String fromUid;
    private String toUid;
    private String msgId;
    private String version;
    private String vendor;
    private String timestamp;
    private String busiEvent;
    private String description;
    private T data;

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBusiEvent() {
        return busiEvent;
    }

    public void setBusiEvent(String busiEvent) {
        this.busiEvent = busiEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
