package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/11
 */
public class PkTimeDto {
    /**
     * endPkTime : 0
     * endPunishTime : 0
     * startTime : 0
     */

    private int endPkTime;
    private int endPunishTime;
    private double startTime;

    public PkTimeDto(int endPkTime, int endPunishTime) {
        this.endPkTime = endPkTime;
        this.endPunishTime = endPunishTime;
    }

    public int getEndPkTime() {
        return endPkTime;
    }

    public void setEndPkTime(int endPkTime) {
        this.endPkTime = endPkTime;
    }

    public int getEndPunishTime() {
        return endPunishTime;
    }

    public void setEndPunishTime(int endPunishTime) {
        this.endPunishTime = endPunishTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
