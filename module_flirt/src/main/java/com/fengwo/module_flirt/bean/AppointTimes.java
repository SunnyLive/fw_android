package com.fengwo.module_flirt.bean;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/29 10:26
 */
public class AppointTimes {

    private List<PeriodPrice> tomorrow;
    private List<PeriodPrice> today;

    public List<PeriodPrice> getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(List<PeriodPrice> tomorrow) {
        this.tomorrow = tomorrow;
    }

    public List<PeriodPrice> getToday() {
        return today;
    }

    public void setToday(List<PeriodPrice> today) {
        this.today = today;
    }

}
