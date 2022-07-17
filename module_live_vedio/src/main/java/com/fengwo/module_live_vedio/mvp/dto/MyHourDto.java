package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class MyHourDto implements Parcelable {
    /**
     * "rank": 0,
     * "score": 0,
     * "userId": 0,
     * "userLevel": "string"
     */
    private String anchorLevel;
    private String headImg;
    private String nickname;
    private int rank;
    private int score;
    private int userId;
    private String userLevel;

    public static final Creator<MyHourDto> CREATOR = new Creator<MyHourDto>() {
        @Override
        public MyHourDto createFromParcel(Parcel in) {
            return new MyHourDto(in);
        }

        @Override
        public MyHourDto[] newArray(int size) {
            return new MyHourDto[size];
        }
    };

    public String getAnchorLevel() {
        return anchorLevel;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public int getUserId() {
        return userId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserLevel() {
        return userLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.anchorLevel);
        dest.writeString(this.headImg);
        dest.writeString(this.nickname);
        dest.writeInt(this.rank);
        dest.writeInt(this.score);
        dest.writeInt(this.userId);
        dest.writeString(this.userLevel);

    }

    protected MyHourDto(Parcel in) {
        anchorLevel = in.readString();
        headImg = in.readString();
        nickname = in.readString();
        rank = in.readInt();
        score = in.readInt();
        userId = in.readInt();
        userLevel = in.readString();


    }
}

