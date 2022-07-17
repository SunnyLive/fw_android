package com.fengwo.module_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/6
 */
public class AlbumDto {

    /**
     * cover : string
     * createTime : 2020-01-06T03:06:49.341Z
     * id : 0
     * name : string
     * sort : 0
     * updateTime : 2020-01-06T03:06:49.341Z
     * userId : 0
     */

    private String cover;
    private String createTime;
    private int id;
    private String name;
    private int sort;
    private String updateTime;
    private int userId;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
