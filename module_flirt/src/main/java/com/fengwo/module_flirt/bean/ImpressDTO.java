package com.fengwo.module_flirt.bean;

import com.google.gson.annotations.SerializedName;

public class ImpressDTO {

    private int expireTime;     // 剩余时长
    private int gears;          //  缘分时段
    private int giftPrice;      // 礼物总价值
    private boolean isOrder;     // 是否开启缘分
    /**
     * hasLastOrder : true
     * lastCreateTime : 2020-10-29T14:01:19.616Z
     * lastDuration : string
     * lastGiftPrice : 0
     * lastOrderTime : 0
     */

    private LastOrderDTO lastOrder;     // 上次缘分记录
    private int orderTime;              // 缘分时长（毫秒）
    private String orderTimeFormat;     // 缘分时长（35:00)

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        this.gears = gears;
    }

    public int getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(int giftPrice) {
        this.giftPrice = giftPrice;
    }

    public boolean isIsOrder() {
        return isOrder;
    }

    public void setIsOrder(boolean isOrder) {
        this.isOrder = isOrder;
    }

    public LastOrderDTO getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(LastOrderDTO lastOrder) {
        this.lastOrder = lastOrder;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTimeFormat() {
        return orderTimeFormat;
    }

    public void setOrderTimeFormat(String orderTimeFormat) {
        this.orderTimeFormat = orderTimeFormat;
    }

    public static class LastOrderDTO {
        private boolean hasLastOrder;       // 是否有上次缘分记录
        private String lastCreateTime;      // 上次缘分时间
        private String lastDuration;        // 上次缘分时长
        private int lastGiftPrice;          // 上次礼物总价值
        private int lastOrderTime;          // 上次缘分时长

        public boolean isHasLastOrder() {
            return hasLastOrder;
        }

        public void setHasLastOrder(boolean hasLastOrder) {
            this.hasLastOrder = hasLastOrder;
        }

        public String getLastCreateTime() {
            return lastCreateTime;
        }

        public void setLastCreateTime(String lastCreateTime) {
            this.lastCreateTime = lastCreateTime;
        }

        public String getLastDuration() {
            return lastDuration;
        }

        public void setLastDuration(String lastDuration) {
            this.lastDuration = lastDuration;
        }

        public int getLastGiftPrice() {
            return lastGiftPrice;
        }

        public void setLastGiftPrice(int lastGiftPrice) {
            this.lastGiftPrice = lastGiftPrice;
        }

        public int getLastOrderTime() {
            return lastOrderTime;
        }

        public void setLastOrderTime(int lastOrderTime) {
            this.lastOrderTime = lastOrderTime;
        }
    }
}
