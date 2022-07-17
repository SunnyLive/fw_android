package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/7/27 17:26
 */
public class CommentWordDto {
    /**
     * anchorPort : 0
     * createTime : 2020-07-27T09:21:39.304Z
     * id : 0
     * status : 0
     * title : string
     * userPort : 0
     */

    private int anchorPort;
    private String createTime;
    private int id;
    private int status;
    private String title;
    private int userPort;

    public int getAnchorPort() {
        return anchorPort;
    }

    public void setAnchorPort(int anchorPort) {
        this.anchorPort = anchorPort;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserPort() {
        return userPort;
    }

    public void setUserPort(int userPort) {
        this.userPort = userPort;
    }
}
