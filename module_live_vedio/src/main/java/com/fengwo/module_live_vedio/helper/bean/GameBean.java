package com.fengwo.module_live_vedio.helper.bean;

import com.google.gson.annotations.SerializedName;

public class GameBean extends NoticeBean {

    public Data data;

    public static class Data {
        @SerializedName("toUid")
        public String toUidX;
        public String giftName;
        public Integer giftType;
        public String headerurl;
        public Integer userId;
        public Integer giftTotal;
        public Integer giftId;
        public String giftIcon;
        public Integer broadCast;
        public Integer headTimes;
        public String gameName;
        public String background;
        public String anchorNickname;
        public String nickname;
        public Integer anchorUserId;
        public Integer sendNum;
        public String anchorHeadImg;
        public Integer timeLeft;
    }
}
