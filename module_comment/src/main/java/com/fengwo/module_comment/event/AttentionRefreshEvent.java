package com.fengwo.module_comment.event;

public class AttentionRefreshEvent {
    public int refreshUid;
    public int isAttention;

    public AttentionRefreshEvent(int refreshUid, int isAttention) {
        this.refreshUid = refreshUid;
        this.isAttention = isAttention;
    }
}
