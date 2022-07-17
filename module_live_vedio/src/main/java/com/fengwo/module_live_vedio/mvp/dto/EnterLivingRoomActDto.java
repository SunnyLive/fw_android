package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/11
 */
public class EnterLivingRoomActDto {

    /**
     * actStatus : 0
     * activityId : 0
     * angelActivity : 0
     * backgroundImg : string
     * channelId : 0
     * guardNickname : string
     * guardType : 0
     * integral : 0
     * isLegend : 0
     * notice : string
     * sort : 0
     * userLives : [{"address":"string","factSpeed":0,"giftDayCount":0,"giftId":0,"giftName":"string","giftPrice":0,"giftSwf":"string","icon":"string","integral":0,"motorImg":"string","motorName":"string","pointUserId":0,"signDays":0,"signStatus":0,"sort":0,"todaySignStatus":0,"type":0,"wishSpeed":0,"wishStatus":0}]
     */

    private int actStatus;
    private int activityId;
    private int angelActivity;
    private String backgroundImg;
    private int channelId;
    private String guardNickname;
    private int guardType;
    private int integral;
    private int isLegend;
    private String notice;
    private int sort;
    private String address;
    public String sortInfo;//

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(String sortInfo) {
        this.sortInfo = sortInfo;
    }

    public String integralInfo;//

    public String getIntegralInfo() {
        return integralInfo;
    }

    public void setIntegralInfo(String integralInfo) {
        this.integralInfo = integralInfo;
    }

    private List<UserLivesBean> userLives;

    public int getActStatus() {
        return actStatus;
    }

    public void setActStatus(int actStatus) {
        this.actStatus = actStatus;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getAngelActivity() {
        return angelActivity;
    }

    public void setAngelActivity(int angelActivity) {
        this.angelActivity = angelActivity;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getGuardNickname() {
        return guardNickname;
    }

    public void setGuardNickname(String guardNickname) {
        this.guardNickname = guardNickname;
    }

    public int getGuardType() {
        return guardType;
    }

    public void setGuardType(int guardType) {
        this.guardType = guardType;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getIsLegend() {
        return isLegend;
    }

    public void setIsLegend(int isLegend) {
        this.isLegend = isLegend;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<UserLivesBean> getUserLives() {
        return userLives;
    }

    public void setUserLives(List<UserLivesBean> userLives) {
        this.userLives = userLives;
    }

    public static class UserLivesBean {
        /**
         * address : string
         * factSpeed : 0
         * giftDayCount : 0
         * giftId : 0
         * giftName : string
         * giftPrice : 0
         * giftSwf : string
         * icon : string
         * integral : 0
         * motorImg : string
         * motorName : string
         * pointUserId : 0
         * signDays : 0
         * signStatus : 0
         * sort : 0
         * todaySignStatus : 0
         * type : 0
         * wishSpeed : 0
         * wishStatus : 0
         */

        private String address;
        private int factSpeed;
        private int giftDayCount;
        private int giftId;
        private String giftName;
        private int giftPrice;
        private String giftSwf;
        private String icon;
        private int integral;
        private String motorImg;
        private String motorName;
        private int pointUserId;
        private int signDays;
        private int signStatus;
        private int sort;
        private int todaySignStatus;
        private int type;
        private int wishSpeed;
        private int wishStatus;
        public String integralInfo;//
        public String sortInfo;//
        public String backgroundImg;

        public String getIntegralInfo() {
            return integralInfo;
        }

        public void setIntegralInfo(String integralInfo) {
            this.integralInfo = integralInfo;
        }

        public String getSortInfo() {
            return sortInfo;
        }

        public void setSortInfo(String sortInfo) {
            this.sortInfo = sortInfo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getFactSpeed() {
            return factSpeed;
        }

        public void setFactSpeed(int factSpeed) {
            this.factSpeed = factSpeed;
        }

        public int getGiftDayCount() {
            return giftDayCount;
        }

        public void setGiftDayCount(int giftDayCount) {
            this.giftDayCount = giftDayCount;
        }

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
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

        public String getGiftSwf() {
            return giftSwf;
        }

        public void setGiftSwf(String giftSwf) {
            this.giftSwf = giftSwf;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public String getMotorImg() {
            return motorImg;
        }

        public void setMotorImg(String motorImg) {
            this.motorImg = motorImg;
        }

        public String getMotorName() {
            return motorName;
        }

        public void setMotorName(String motorName) {
            this.motorName = motorName;
        }

        public int getPointUserId() {
            return pointUserId;
        }

        public void setPointUserId(int pointUserId) {
            this.pointUserId = pointUserId;
        }

        public int getSignDays() {
            return signDays;
        }

        public void setSignDays(int signDays) {
            this.signDays = signDays;
        }

        public int getSignStatus() {
            return signStatus;
        }

        public void setSignStatus(int signStatus) {
            this.signStatus = signStatus;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getTodaySignStatus() {
            return todaySignStatus;
        }

        public void setTodaySignStatus(int todaySignStatus) {
            this.todaySignStatus = todaySignStatus;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getWishSpeed() {
            return wishSpeed;
        }

        public void setWishSpeed(int wishSpeed) {
            this.wishSpeed = wishSpeed;
        }

        public int getWishStatus() {
            return wishStatus;
        }

        public void setWishStatus(int wishStatus) {
            this.wishStatus = wishStatus;
        }
    }
}
