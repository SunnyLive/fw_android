package com.fengwo.module_live_vedio.eventbus;

public class ChangeRoomEvent {
    public String channelId;
    public String headImage;

    public ChangeRoomEvent(String id) {
        channelId = id;
    }

    public ChangeRoomEvent(String channelId, String headImage) {
        this.channelId = channelId;
        this.headImage = headImage;
    }

    public String getChannelId() {
        return channelId;
    }
}
