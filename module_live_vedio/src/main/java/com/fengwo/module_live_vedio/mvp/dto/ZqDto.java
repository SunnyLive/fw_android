package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/9/21
 */
public class ZqDto {

    /**
     * activityStatus : true
     * activityCenterImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activity/room_center_zq.png
     * activityGifts : [{"id":184,"giftName":"中秋佳节","giftPrice":8888,"sort":999,"giftGrade":1,"giftType":7,"giftTypeText":null,"giftIcon25":"","giftIcon":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1600156237000*gift3217652908.jpg","giftSwf":"","quantityGrad":"1,2,3,4","swfTime":0,"swfPlay":0,"frameRate":10,"giftLevel":1,"continuous":0,"status":1,"broadcast":1,"giftOriginalPrice":11110,"roomCenterOtherImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activity/center_other_icon.png"}]
     */

    private boolean activityStatus;
    private String activityCenterImg;
    public List<GiftDto> activityGifts;

    public boolean isActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityCenterImg() {
        return activityCenterImg;
    }

    public void setActivityCenterImg(String activityCenterImg) {
        this.activityCenterImg = activityCenterImg;
    }



    public static class ActivityGiftsBean {
        /**
         * id : 184
         * giftName : 中秋佳节
         * giftPrice : 8888
         * sort : 999
         * giftGrade : 1
         * giftType : 7
         * giftTypeText : null
         * giftIcon25 :
         * giftIcon : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1600156237000*gift3217652908.jpg
         * giftSwf :
         * quantityGrad : 1,2,3,4
         * swfTime : 0
         * swfPlay : 0
         * frameRate : 10
         * giftLevel : 1
         * continuous : 0
         * status : 1
         * broadcast : 1
         * giftOriginalPrice : 11110
         * roomCenterOtherImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activity/center_other_icon.png
         */

        public int id;
        public String giftName;
        public int giftPrice;
        public int sort;
        public int giftGrade;
        public int giftType;
        public Object giftTypeText;
        public String giftIcon25;
        public String giftIcon;
        public String giftSwf;
        public String quantityGrad;
        public int swfTime;
        public int swfPlay;
        public int frameRate;
        public int giftLevel;
        public int continuous;
        public int status;
        public int broadcast;
        public int giftOriginalPrice;
        private String roomCenterOtherImg;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public int getGiftPrice() {
            return giftPrice;
        }

        public void setGiftPrice(int giftPrice) {
            this.giftPrice = giftPrice;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getGiftGrade() {
            return giftGrade;
        }

        public void setGiftGrade(int giftGrade) {
            this.giftGrade = giftGrade;
        }

        public int getGiftType() {
            return giftType;
        }

        public void setGiftType(int giftType) {
            this.giftType = giftType;
        }

        public Object getGiftTypeText() {
            return giftTypeText;
        }

        public void setGiftTypeText(Object giftTypeText) {
            this.giftTypeText = giftTypeText;
        }

        public String getGiftIcon25() {
            return giftIcon25;
        }

        public void setGiftIcon25(String giftIcon25) {
            this.giftIcon25 = giftIcon25;
        }

        public String getGiftIcon() {
            return giftIcon;
        }

        public void setGiftIcon(String giftIcon) {
            this.giftIcon = giftIcon;
        }

        public String getGiftSwf() {
            return giftSwf;
        }

        public void setGiftSwf(String giftSwf) {
            this.giftSwf = giftSwf;
        }

        public String getQuantityGrad() {
            return quantityGrad;
        }

        public void setQuantityGrad(String quantityGrad) {
            this.quantityGrad = quantityGrad;
        }

        public int getSwfTime() {
            return swfTime;
        }

        public void setSwfTime(int swfTime) {
            this.swfTime = swfTime;
        }

        public int getSwfPlay() {
            return swfPlay;
        }

        public void setSwfPlay(int swfPlay) {
            this.swfPlay = swfPlay;
        }

        public int getFrameRate() {
            return frameRate;
        }

        public void setFrameRate(int frameRate) {
            this.frameRate = frameRate;
        }

        public int getGiftLevel() {
            return giftLevel;
        }

        public void setGiftLevel(int giftLevel) {
            this.giftLevel = giftLevel;
        }

        public int getContinuous() {
            return continuous;
        }

        public void setContinuous(int continuous) {
            this.continuous = continuous;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getBroadcast() {
            return broadcast;
        }

        public void setBroadcast(int broadcast) {
            this.broadcast = broadcast;
        }

        public int getGiftOriginalPrice() {
            return giftOriginalPrice;
        }

        public void setGiftOriginalPrice(int giftOriginalPrice) {
            this.giftOriginalPrice = giftOriginalPrice;
        }

        public String getRoomCenterOtherImg() {
            return roomCenterOtherImg;
        }

        public void setRoomCenterOtherImg(String roomCenterOtherImg) {
            this.roomCenterOtherImg = roomCenterOtherImg;
        }
    }
}
