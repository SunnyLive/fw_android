package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @anchor Administrator
 * @date 2020/12/11
 */
public class ActivityCpInfoBean implements Parcelable {

    /**
     * cpUserId : 986761
     * cpRank : 5
     * cpHeadImg : null
     * cpNickname : 用户986761
     */

    private String cpUserId;
    private String cpRank;
    private String cpHeadImg;
    private String cpNickname;

    protected ActivityCpInfoBean(Parcel in) {
        this.cpUserId = in.readString();
        this.cpRank = in.readString();
        this.cpHeadImg = in.readString();
        this.cpNickname = in.readString();
    }

    public static final Creator<ActivityCpInfoBean> CREATOR = new Creator<ActivityCpInfoBean>() {
        @Override
        public ActivityCpInfoBean createFromParcel(Parcel in) {
            return new ActivityCpInfoBean(in);
        }

        @Override
        public ActivityCpInfoBean[] newArray(int size) {
            return new ActivityCpInfoBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cpUserId);
        dest.writeString(this.cpRank);
        dest.writeString(this.cpHeadImg);
        dest.writeString(this.cpNickname);
    }
    public String getCpUserId() {
        return cpUserId;
    }

    public void setCpUserId(String cpUserId) {
        this.cpUserId = cpUserId;
    }

    public String getCpRank() {
        return cpRank;
    }

    public void setCpRank(String cpRank) {
        this.cpRank = cpRank;
    }

    public Object getCpHeadImg() {
        return cpHeadImg;
    }

    public void setCpHeadImg(String cpHeadImg) {
        this.cpHeadImg = cpHeadImg;
    }

    public String getCpNickname() {
        return cpNickname;
    }

    public void setCpNickname(String cpNickname) {
        this.cpNickname = cpNickname;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
