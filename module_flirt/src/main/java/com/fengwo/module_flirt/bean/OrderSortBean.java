package com.fengwo.module_flirt.bean;

public class OrderSortBean {

    private int order;//排序 0：距离,1：活跃时间,2:添加好友时间,3:首字母
    private boolean check;
    public String title;

    public OrderSortBean(int order, String title, boolean check) {
        this.order = order;
        this.check = check;
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
