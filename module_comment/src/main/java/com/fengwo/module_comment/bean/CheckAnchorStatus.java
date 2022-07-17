package com.fengwo.module_comment.bean;

/**
 * @Author BLCS
 * @Time 2020/5/7 20:19
 */
public class CheckAnchorStatus {

    /**
     * bstatus : 0
     * hasAppointment : true
     */

    private int bstatus;
    private boolean hasAppointment;

    public int getBstatus() {
        return bstatus;
    }

    public void setBstatus(int bstatus) {
        this.bstatus = bstatus;
    }

    public boolean isHasAppointment() {
        return hasAppointment;
    }

    public void setHasAppointment(boolean hasAppointment) {
        this.hasAppointment = hasAppointment;
    }
}
