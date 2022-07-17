package com.fengwo.module_chat.mvp.ui.event;

public class GroupForbiddenEvent {
    public String groupId;
    public boolean forbidden;

    public GroupForbiddenEvent(String groupId, boolean forbidden) {
        this.groupId = groupId;
        this.forbidden = forbidden;
    }
}
