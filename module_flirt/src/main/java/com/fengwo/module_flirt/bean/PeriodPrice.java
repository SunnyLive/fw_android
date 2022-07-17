package com.fengwo.module_flirt.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @Author BLCS
 * @Time 2020/4/29 10:26
 */
public class PeriodPrice implements MultiItemEntity {


    /**
     * anchorId : 0
     * createTime : 2020-04-29T08:04:43.220Z
     * id : 0
     * isAppointment : 0
     * periodTime : string
     * tprice : 0
     */

    private int anchorId;
    private String createTime;
    private int id;
    private int isAppointment;
    private String periodTime;


    private String day;
    private int tprice;
    private int itemType;

    public PeriodPrice(int itemType,String day) {
        this.itemType = itemType;
        this.day = day;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
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

    public int getIsAppointment() {
        return isAppointment;
    }

    public void setIsAppointment(int isAppointment) {
        this.isAppointment = isAppointment;
    }

    public String getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }

    public int getTprice() {
        return tprice;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    public void setTprice(int tprice) {
        this.tprice = tprice;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
    public void setItemType(int type){
        itemType = type;
    }
}
