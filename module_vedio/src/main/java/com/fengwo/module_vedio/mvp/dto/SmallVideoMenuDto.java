package com.fengwo.module_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/9
 */
public class SmallVideoMenuDto {

    /**
     * createTime : 2020-01-09T09:14:24.714Z
     * id : 0
     * name : string
     * updateTime : 2020-01-09T09:14:24.714Z
     */

    private String createTime;
    private int id;
    private String name;
    private String updateTime;

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
