package com.fengwo.module_comment.bean;

import java.util.List;

public class BackpackDto {


    public List<GiftsBean> gifts;
    public List<GiftsBean> motors;
    public List<GiftsBean> nobilities;
    public List<GiftsBean> props;
    public int totalCount;

    public static class GiftsBean {
        /**
         * activityId	    integer($int32)         活动id
         * goodsCount	    integer($int32)         物品数量
         * goodsDuration    integer($int32)         物品时长（天）
         * goodsIcon	    string                  物品图标
         * goodsId	        integer($int32)         物品id
         * goodsName	    string                  物品名称
         * goodsType	    integer($int32)         物品类型：1礼物 2座驾 3贵族 4道具
         * goodsStatus      integer($int32)         物品状态：1未使用 2使用中 3已到期
         * remainCount	    integer($int32)         剩余数量
         * remainDays       integer($int32)         剩余时长（天）
         * remark           string                  备注
         * isOpened         boolean                 座驾 是否开启
         * isMaxLevel       boolean                 贵族 是否最高等级
         */


        public String activityId;
        public int goodsCount;
        public String goodsDuration;
        public String goodsIcon;
        public String goodsId;
        public String goodsName;
        public int goodsType;
        public int goodsStatus;
        public int remainCount;
        public String remainDays;
        public String remark;
        public boolean isOpened;
        public boolean isMaxLevel;
        public int broadcast;
        public int frameRate;
        public int giftType;
        public int giftPrice;
        public int remainValidDays;
        public int validType;
        public long validTime;
        public String giftSwf;
        public String quantityGrad;

    }

}
