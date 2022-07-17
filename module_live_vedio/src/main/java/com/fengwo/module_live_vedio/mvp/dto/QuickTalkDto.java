package com.fengwo.module_live_vedio.mvp.dto;

public class QuickTalkDto {

    /**
     * content : string
     * createTime : 2019-10-25T08:17:23.942Z
     * id : 0
     * status : 0
     * type : 0
     */

    private String content;
    private String createTime;
    private int id;
    private int status;
    private int type;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
