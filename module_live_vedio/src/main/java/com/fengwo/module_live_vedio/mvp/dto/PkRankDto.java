package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/11
 */
public class PkRankDto implements Serializable {

    public int userId;
    public int objectId;
    public PkUserInfo userInfo;
    public PkUserInfo objectInfo;
    public List<PkRankMember> contributionRank; //我方版单
    public List<PkRankMember>objectContributionRank;//对方版单
    public int memberPopularity; //我方热度
    public int objectMemberPopularity;//对方热度


    public PkRankDto() {
    }

    public PkUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(PkUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public PkUserInfo getObjectInfo() {
        return objectInfo;
    }

    public void setObjectInfo(PkUserInfo objectInfo) {
        this.objectInfo = objectInfo;
    }

}
