package com.fengwo.module_flirt.IM.bean;

/**
 * 回执 内容
 * @Author BLCS
 * @Time 2020/5/5 11:47
 */
public class ReceiverBackBean {

    /**
     * fromUid : 512191
     * toUid : -1
     * msgId : 202004231425558571253208463557091322
     * oriMsgId : 202004231425558571253208463557091329
     * busiEvent : ack
     * version : 2.0
     * vendor : Huawei P30
     * description : 消息回执
     * timestamp : 15846568956
     */

    private String fromUid;
    private String toUid;
    private String msgId;
    private String oriMsgId;
    private String busiEvent;
    private String version;
    private String vendor;
    private String description;
    private String timestamp;

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

    public String getOriMsgId() {
        return oriMsgId;
    }

    public void setOriMsgId(String oriMsgId) {
        this.oriMsgId = oriMsgId;
    }

    public String getBusiEvent() {
        return busiEvent;
    }

    public void setBusiEvent(String busiEvent) {
        this.busiEvent = busiEvent;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
