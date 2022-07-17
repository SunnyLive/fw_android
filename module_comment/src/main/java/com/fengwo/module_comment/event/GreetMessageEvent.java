package com.fengwo.module_comment.event;

public class GreetMessageEvent {
    public String belongUserId;
    public String talkUserId;
    public String talkUserName;
    public String talkUserAvatar;
    public String text;
    public String headerImg;
    public long time;

    public GreetMessageEvent(String belongUserId, String talkUserId, String talkUserName, String talkUserAvatar, String text, String headerImg, long time) {
        this.belongUserId = belongUserId;
        this.talkUserId = talkUserId;
        this.talkUserName = talkUserName;
        this.talkUserAvatar = talkUserAvatar;
        this.text = text;
        this.headerImg = headerImg;
        this.time = time;
    }
}
