package com.fengwo.module_websocket.bean;

/**
 * 缘分礼物
 */
public class StatFateMsg extends WebboBulletin{

    private boolean isPay;

    private int gears;//缘分等级

    private int totalOrderTime;//缘分总时长

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        this.gears = gears;
    }

    public int getTotalOrderTime() {
        return totalOrderTime;
    }

    public void setTotalOrderTime(int totalOrderTime) {
        this.totalOrderTime = totalOrderTime;
    }
}
