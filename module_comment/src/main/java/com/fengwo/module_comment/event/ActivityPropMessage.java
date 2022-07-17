package com.fengwo.module_comment.event;



/**
 * 活动赠送道具提醒消息
 *
 * @author yhf
 */

public class ActivityPropMessage {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户昵称
     */
    private String userNickname;
    /**
     * 用户头像
     */
    private String userHeadImg;
    /**
     * 道具id
     */
    private Integer propId;
    /**
     * 道具名称
     */
    private String propName;
    /**
     * 道具icon
     */
    private String propIcon;
    /**
     * 道具数量
     */
    private Integer propQuantity;

    public ActivityPropMessage() {
    }

    public ActivityPropMessage(Integer userId, String userNickname, String userHeadImg, Integer propId, String propName, String propIcon, Integer propQuantity) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userHeadImg = userHeadImg;
        this.propId = propId;
        this.propName = propName;
        this.propIcon = propIcon;
        this.propQuantity = propQuantity;
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

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropIcon() {
        return propIcon;
    }

    public void setPropIcon(String propIcon) {
        this.propIcon = propIcon;
    }

    public Integer getPropQuantity() {
        return propQuantity;
    }

    public void setPropQuantity(Integer propQuantity) {
        this.propQuantity = propQuantity;
    }
}
