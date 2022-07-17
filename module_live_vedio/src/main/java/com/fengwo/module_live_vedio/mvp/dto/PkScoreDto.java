package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/11
 */
public class PkScoreDto implements Serializable {


    /**
     * groupPoint : 0
     * guildPoint : 0
     * otherGroupPoint : 0
     * otherGuildPoint : 0
     * otherId : 0
     * otherTotalPoint : 0
     * point : 0
     * totalPoint : 0
     * userId : 0
     */

    private int groupPoint;
    private int guildPoint;
    private int otherGroupPoint;
    private int otherGuildPoint;
    private int otherId;
    private int otherTotalPoint;
    private int point;
    private int totalPoint;
    private int userId;

    public int getGroupPoint() {
        return groupPoint;
    }

    public void setGroupPoint(int groupPoint) {
        this.groupPoint = groupPoint;
    }

    public int getGuildPoint() {
        return guildPoint;
    }

    public void setGuildPoint(int guildPoint) {
        this.guildPoint = guildPoint;
    }

    public int getOtherGroupPoint() {
        return otherGroupPoint;
    }

    public void setOtherGroupPoint(int otherGroupPoint) {
        this.otherGroupPoint = otherGroupPoint;
    }

    public int getOtherGuildPoint() {
        return otherGuildPoint;
    }

    public void setOtherGuildPoint(int otherGuildPoint) {
        this.otherGuildPoint = otherGuildPoint;
    }

    public int getOtherId() {
        return otherId;
    }

    public void setOtherId(int otherId) {
        this.otherId = otherId;
    }

    public int getOtherTotalPoint() {
        return otherTotalPoint;
    }

    public void setOtherTotalPoint(int otherTotalPoint) {
        this.otherTotalPoint = otherTotalPoint;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
