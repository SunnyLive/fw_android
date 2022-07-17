package com.fengwo.module_chat.mvp.ui.event;

public class ChatGroupResRefreshEvent {
    public String id;
    public String imgUrl;
    public String linkUrl;
    public String name;

    public ChatGroupResRefreshEvent(String id, String imgUrl, String linkUrl, String name) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.linkUrl = linkUrl;
        this.name = name;
    }
}
