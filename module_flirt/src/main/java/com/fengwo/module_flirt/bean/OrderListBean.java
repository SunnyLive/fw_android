package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/4/29 15:15
 */
public class OrderListBean {

    /**
     * anchorId : 0
     * id : 0
     * teimPrice : 0
     * timeLost : 0
     */

    private int anchorId;
    private int id;
    private int teimPrice;
    private int timeLost;

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeimPrice() {
        return teimPrice;
    }

    public void setTeimPrice(int teimPrice) {
        this.teimPrice = teimPrice;
    }

    public int getTimeLost() {
        return timeLost;
    }

    public void setTimeLost(int timeLost) {
        this.timeLost = timeLost;
    }
}
