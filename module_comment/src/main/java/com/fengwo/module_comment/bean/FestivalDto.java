package com.fengwo.module_comment.bean;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/9/21
 */
public class FestivalDto {

    /**
     * activityCenterImg : string
     * activityGifts : [{"broadcast":0,"continuous":0,"frameRate":0,"giftGrade":0,"giftIcon":"string","giftIcon25":"string","giftLevel":0,"giftName":"string","giftOriginalPrice":0,"giftPrice":0,"giftSwf":"string","giftType":0,"giftTypeText":"string","id":0,"quantityGrad":"string","sort":0,"status":0,"swfPlay":0,"swfTime":0}]
     */

    private String activityCenterImg;
    private List<ActivityGiftsBean> activityGifts;
    private boolean activityStatus;

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

    public List<ActivityGiftsBean> getActivityGifts() {
        return activityGifts;
    }

    public void setActivityGifts(List<ActivityGiftsBean> activityGifts) {
        this.activityGifts = activityGifts;
    }

    public static class ActivityGiftsBean {
        /**
         * broadcast : 0
         * continuous : 0
         * frameRate : 0
         * giftGrade : 0
         * giftIcon : string
         * giftIcon25 : string
         * giftLevel : 0
         * giftName : string
         * giftOriginalPrice : 0
         * giftPrice : 0
         * giftSwf : string
         * giftType : 0
         * giftTypeText : string
         * id : 0
         * quantityGrad : string
         * sort : 0
         * status : 0
         * swfPlay : 0
         * swfTime : 0
         */

        private int broadcast;
        private int continuous;
        private int frameRate;
        private int giftGrade;
        private String giftIcon;
        private String giftIcon25;
        private int giftLevel;
        private String giftName;
        private int giftOriginalPrice;
        private int giftPrice;
        private String giftSwf;
        private int giftType;
        private String giftTypeText;
        private int id;
        private String quantityGrad;
        private int sort;
        private int status;
        private int swfPlay;
        private int swfTime;

        public int getBroadcast() {
            return broadcast;
        }

        public void setBroadcast(int broadcast) {
            this.broadcast = broadcast;
        }

        public int getContinuous() {
            return continuous;
        }

        public void setContinuous(int continuous) {
            this.continuous = continuous;
        }

        public int getFrameRate() {
            return frameRate;
        }

        public void setFrameRate(int frameRate) {
            this.frameRate = frameRate;
        }

        public int getGiftGrade() {
            return giftGrade;
        }

        public void setGiftGrade(int giftGrade) {
            this.giftGrade = giftGrade;
        }

        public String getGiftIcon() {
            return giftIcon;
        }

        public void setGiftIcon(String giftIcon) {
            this.giftIcon = giftIcon;
        }

        public String getGiftIcon25() {
            return giftIcon25;
        }

        public void setGiftIcon25(String giftIcon25) {
            this.giftIcon25 = giftIcon25;
        }

        public int getGiftLevel() {
            return giftLevel;
        }

        public void setGiftLevel(int giftLevel) {
            this.giftLevel = giftLevel;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public int getGiftOriginalPrice() {
            return giftOriginalPrice;
        }

        public void setGiftOriginalPrice(int giftOriginalPrice) {
            this.giftOriginalPrice = giftOriginalPrice;
        }

        public int getGiftPrice() {
            return giftPrice;
        }

        public void setGiftPrice(int giftPrice) {
            this.giftPrice = giftPrice;
        }

        public String getGiftSwf() {
            return giftSwf;
        }

        public void setGiftSwf(String giftSwf) {
            this.giftSwf = giftSwf;
        }

        public int getGiftType() {
            return giftType;
        }

        public void setGiftType(int giftType) {
            this.giftType = giftType;
        }

        public String getGiftTypeText() {
            return giftTypeText;
        }

        public void setGiftTypeText(String giftTypeText) {
            this.giftTypeText = giftTypeText;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuantityGrad() {
            return quantityGrad;
        }

        public void setQuantityGrad(String quantityGrad) {
            this.quantityGrad = quantityGrad;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSwfPlay() {
            return swfPlay;
        }

        public void setSwfPlay(int swfPlay) {
            this.swfPlay = swfPlay;
        }

        public int getSwfTime() {
            return swfTime;
        }

        public void setSwfTime(int swfTime) {
            this.swfTime = swfTime;
        }
    }
}
