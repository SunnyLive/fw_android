package com.fengwo.module_vedio.eventbus;

public class ShowCommentInputEvent {
    public int videoId;
    public int commentId;
    public String userName;
    public int userId;

    public ShowCommentInputEvent() {
    }

    public ShowCommentInputEvent(int videoId) {
        this.videoId = videoId;
    }
    public ShowCommentInputEvent(int videoId, int commentId, String userName, int userId) {
        this.videoId = videoId;
        this.commentId = commentId;
        this.userName = userName;
        this.userId = userId;
    }
}
