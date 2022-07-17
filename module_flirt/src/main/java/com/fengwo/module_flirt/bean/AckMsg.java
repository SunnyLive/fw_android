package com.fengwo.module_flirt.bean;

public class AckMsg {

    /**
     * busiEvent : ack
     * description : 消息回执
     * fromUid : -1
     * msgId : 202005051704016771257596904357072898
     * oriMsgId : 202005126170359345000050079452002
     * timestamp : 1588669441677
     * toUid : 500794
     * vendor : IM Server
     * version : 2.0
     */

    private String busiEvent;
    private String description;
    private String fromUid;
    private String msgId;
    private String oriMsgId;
    private String timestamp;
    private String toUid;
    private String vendor;
    private String version;

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

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
