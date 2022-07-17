package com.fengwo.module_live_vedio.helper.bean;

import com.google.gson.annotations.SerializedName;

public class RedPackNotice extends NoticeBean{

    public Data data;

    public static class Data {
        @SerializedName("toUid")
        public String toUidX;
        public Integer type;
        public Entity entity;

        public static class Entity {
            public Integer refRoomId;
            public Integer refUserId;
            public String refAnchorUserName;
            public String refUserName;
            public String serialNo;
            public String refUserHeadImg;
            public String background;
            public Integer leftCount;
            public Integer redpacketChannel;
            public String createDatetime;
            public Long createDatetimestamp;
            public Long currentTimestamp;
            public Integer totalAmount;
            public Integer redPacketNum;
            public Integer redpacketType;
            public Integer refAnchorUserId;
            public Integer leftAmount;
            public Integer id;
            public Integer status;
        }
    }
}
