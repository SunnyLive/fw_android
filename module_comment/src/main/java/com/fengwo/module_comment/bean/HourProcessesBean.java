package com.fengwo.module_comment.bean;

/**
 * @anchor Administrator
 * @date 2020/12/10
 */
public class HourProcessesBean {

    /**
     * userName :
     * anchroName :
     * integral : 0
     * dateHour : 2020121018
     * begin : 12.10 18:01
     * end : 12.10 20:00
     * endTime : 1607601600000
     * icon :
     * status : true
     */

    private String userName;
    private String anchroName;
    private int integral;
    private String dateHour;
    private String begin;
    private String end;
    private String endTime;
    private String icon;
    private boolean status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnchroName() {
        return anchroName;
    }

    public void setAnchroName(String anchroName) {
        this.anchroName = anchroName;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getDateHour() {
        return dateHour;
    }

    public void setDateHour(String dateHour) {
        this.dateHour = dateHour;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
