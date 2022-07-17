package com.fengwo.module_chat.mvp.ui.event;

public class GroupShowBubbleEvent {
    public String id;
    public String bubbleName;
    public String groupId;

    public GroupShowBubbleEvent(String id, String bubbleName, String groupId) {
        this.id = id;
        this.bubbleName = bubbleName;
        this.groupId = groupId;
    }
}
