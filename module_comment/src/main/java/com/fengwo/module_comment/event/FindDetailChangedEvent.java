package com.fengwo.module_comment.event;

public class FindDetailChangedEvent {
    private int likeCount;//点赞数量
    private int isLike;//自身是否点赞
    private int commentsCount;//评论个数
    private int shares;


    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public FindDetailChangedEvent() {
    }

    public FindDetailChangedEvent(int likeCount, int shares, int commentsCount,int isLike) {
        this.likeCount = likeCount;
        this.shares = shares;
        this.commentsCount = commentsCount;
        this.isLike = isLike;
    }
}
