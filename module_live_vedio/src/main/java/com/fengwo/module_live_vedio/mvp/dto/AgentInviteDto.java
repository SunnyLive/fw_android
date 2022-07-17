package com.fengwo.module_live_vedio.mvp.dto;

public class AgentInviteDto {


    /**
     * createTime : 2020-01-20T08:52:01.521Z
     * userHeadImg : string
     * userId : 0
     * userNickname : string
     */

    private String createTime;
    private String userHeadImg;
    private int userId;
    private String userNickname;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
