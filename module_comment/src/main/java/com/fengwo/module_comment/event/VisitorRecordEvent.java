package com.fengwo.module_comment.event;

public class VisitorRecordEvent {
    public int beUserId;
    public long stayTime;

    public VisitorRecordEvent(int beUserId, long stayTime) {
        this.beUserId = beUserId;
        this.stayTime = stayTime;
    }
}
