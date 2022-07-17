package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/10
 */
    public class MatchSingleResult extends PkRankDto implements Serializable {

    protected int endPkTime = 60000;
    protected Integer endPunishTime = 60000;
    protected long startTime;
    protected PkScoreDto pkPoint;

    public MatchSingleResult() {
    }

    public int getEndPkTime() {
        return endPkTime;
    }

    public void setEndPkTime(int endPkTime) {
        this.endPkTime = endPkTime;
    }

    public Integer getEndPunishTime() {
        return endPunishTime;
    }

    public void setEndPunishTime(Integer endPunishTime) {
        this.endPunishTime = endPunishTime;
    }

    public PkScoreDto getPkPoint() {
        return pkPoint;
    }

    public void setPkPoint(PkScoreDto pkPoint) {
        this.pkPoint = pkPoint;
    }


    public int getPkTime() {
        return endPkTime;
    }

    public void setPkTime(int pkTime) {
        this.endPkTime = pkTime;
    }

    public int getPunishTime() {
        return endPunishTime;
    }

    public void setPunishTime(int punishTime) {
        this.endPunishTime = punishTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


}
