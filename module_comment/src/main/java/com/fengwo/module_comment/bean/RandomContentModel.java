package com.fengwo.module_comment.bean;

/**
 * @Author BLCS
 * @Time 2020/8/19 17:59
 */
public class RandomContentModel {


    /**
     * createTime : 2020-08-19T09:58:43.452Z
     * id : 0
     * status : 0
     * title : string
     */

    private String createTime;
    private int id;
    private int status;
    private String title;

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
}
