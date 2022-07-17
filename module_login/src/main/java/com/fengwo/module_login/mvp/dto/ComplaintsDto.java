package com.fengwo.module_login.mvp.dto;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/16
 */
public class ComplaintsDto implements Serializable {

    /**
     * content : string
     * createTime : 2020-01-16T03:00:52.936Z
     * id : 0
     * image : string
     * nickname : string
     * replyContent : string
     * status : 0
     * userId : 0
     */

    private String content;
    private String createTime;
    private int id;
    private String image;
    private String nickname;
    private String replyContent;
    private int status;
    private int userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
