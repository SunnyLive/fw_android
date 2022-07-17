package com.fengwo.module_comment.event;



/**
 * 活动许愿广播消息
 *
 * @author yhf
 */

public class ActivityWishMessage {
    /**
     * 许愿用户id
     */
    private Integer userId;
    /**
     * 许愿用户昵称
     */
    private String userNickname;
    /**
     * 主播id
     */
    private Integer anchorId;
    /**
     * 主播昵称
     */
    private String anchorNickname;
    /**
     * 许愿内容
     */
    private String wishContent;
    //用户头像
    private String userHeadImg;

    public ActivityWishMessage(Integer userId, String userNickname, Integer anchorId, String anchorNickname, String wishContent,String userHeadImg) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.anchorId = anchorId;
        this.anchorNickname = anchorNickname;
        this.wishContent = wishContent;
        this.userHeadImg = userHeadImg;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Integer getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Integer anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorNickname() {
        return anchorNickname;
    }

    public void setAnchorNickname(String anchorNickname) {
        this.anchorNickname = anchorNickname;
    }

    public String getWishContent() {
        return wishContent;
    }

    public void setWishContent(String wishContent) {
        this.wishContent = wishContent;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }
}
