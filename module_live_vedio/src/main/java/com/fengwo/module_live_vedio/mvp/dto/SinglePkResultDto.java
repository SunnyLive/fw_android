package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/12
 */
public class SinglePkResultDto extends PkRankDto implements Serializable {

    /**
     * otherId : 0
     * otherResult : 0
     * userId : 0
     * userResult : 0
     * totalPoint
     * otherTotalPoint
     */

    private int otherId;
    private int otherResult;
    private int userResult;
    private long totalPoint;
    private long otherTotalPoint;

    public SinglePkResultDto() {
    }

    public List<PkRankMember> getContributionRank() {
        return contributionRank;
    }

    public void setContributionRank(List<PkRankMember> contributionRank) {
        this.contributionRank = contributionRank;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public long getOtherTotalPoint() {
        return otherTotalPoint;
    }

    public void setOtherTotalPoint(long otherTotalPoint) {
        this.otherTotalPoint = otherTotalPoint;
    }

    public int getOtherId() {
        return otherId;
    }

    public void setOtherId(int otherId) {
        this.otherId = otherId;
    }

    public int getOtherResult() {
        return otherResult;
    }

    public void setOtherResult(int otherResult) {
        this.otherResult = otherResult;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserResult() {
        return userResult;
    }

    public void setUserResult(int userResult) {
        this.userResult = userResult;
    }
}
