package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/4/28 17:24
 */
public class ChatMessageBack {

    /**
     * busiEvent : ack
     * description : 消息回执
     * fromUid : -1
     * msgId : 202004281719530571255064179632816130
     * oriMsgId : 202004119171955438000050085369716
     * timestamp : 1588065593057
     * vendor : IM Server
     * version : 2.0
     */

    private String busiEvent;
    private String description;
    private String fromUid;
    private String msgId;
    private String oriMsgId;
    private String timestamp;
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
