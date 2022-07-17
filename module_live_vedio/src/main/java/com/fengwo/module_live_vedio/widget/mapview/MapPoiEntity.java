package com.fengwo.module_live_vedio.widget.mapview;



public class MapPoiEntity {
    private String name;
    //poi点的状态
    private int status;
    //百分比位置
    private float precentageTop;
    private float precentageLeft;
    private int i;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getPrecentageTop() {
        return precentageTop;
    }

    public void setPrecentageTop(float precentageTop) {
        this.precentageTop = precentageTop;
    }

    public float getPrecentageLeft() {
        return precentageLeft;
    }

    public void setPrecentageLeft(float precentageLeft) {
        this.precentageLeft = precentageLeft;
    }
}
