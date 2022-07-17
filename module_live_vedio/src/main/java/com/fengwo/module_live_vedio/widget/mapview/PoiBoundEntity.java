package com.fengwo.module_live_vedio.widget.mapview;

/**
 * 存储poi点的触发范围
 */

public class PoiBoundEntity {
    private int left;
    private int top;
    private int right;
    private int bootom;
    private int id;
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBootom() {
        return bootom;
    }

    public void setBootom(int bootom) {
        this.bootom = bootom;
    }

    public void setId(int id) {
        this.id = id;
    }


}
