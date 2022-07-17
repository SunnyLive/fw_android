package com.fengwo.module_live_vedio.helper.bean;

public class NoticeBean {

    public String eventId;
    public String toUid;
    public String groupId;
    public String msgId;
    public String description;
    public Boolean isGroupMsg;

    @Override
    public String toString() {
        return "NoticeBean{" +
                "eventId='" + eventId + '\'' +
                ", toUid='" + toUid + '\'' +
                ", groupId='" + groupId + '\'' +
                ", msgId='" + msgId + '\'' +
                ", description='" + description + '\'' +
                ", isGroupMsg=" + isGroupMsg +
                '}';
    }
}
