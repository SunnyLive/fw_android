package com.fengwo.module_login.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ReportLabelDto {

    /**
     * createTime : 2019-11-01T11:46:19.114Z
     * id : 0
     * name : string
     * sort : 0
     */

    private String createTime;
    private int id;
    private String name;
    private int sort;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
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
}
