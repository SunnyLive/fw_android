package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/4/30 14:26
 */
public class EnterRoomBean {


    /**
     * roomId : 7236111597834647
     * roomTile : 点击领取小可爱
     * anchorId : 723611
     * nickname : 妮妮在呢
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/723611/images_1594973387_KLOB3CPJrL.jpeg
     * expireTime : 0
     * streamPull : https://play.yinkehuyu.cn/prod/prod_wenbo_723611.flv
     * orderType : 0
     * orderId : 0
     * isPay : false
     * livingRoomUserId : 5420
     * lastOrder : {"hasLastOrder":true,"lastTime":{"epochSecond":1602659176,"nano":0},"lastDuration":"02:10"}
     */

    private String roomId;
    private String roomTile;
    private int anchorId;
    private String nickname;
    private String headImg;
    private Long expireTime;
    private String streamPull;
    private int orderType;
    private int orderId;
    private boolean isPay;
    private int livingRoomUserId;
    private boolean isEvaluated;
    private LastOrderBean lastOrder;
    private long duration;           // 已经持续时间

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomTile() {
        return roomTile;
    }

    public void setRoomTile(String roomTile) {
        this.roomTile = roomTile;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getStreamPull() {
        return streamPull;
    }

    public void setStreamPull(String streamPull) {
        this.streamPull = streamPull;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isIsPay() {
        return isPay;
    }

    public void setIsPay(boolean isPay) {
        this.isPay = isPay;
    }

    public int getLivingRoomUserId() {
        return livingRoomUserId;
    }

    public void setLivingRoomUserId(int livingRoomUserId) {
        this.livingRoomUserId = livingRoomUserId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LastOrderBean getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(LastOrderBean lastOrder) {
        this.lastOrder = lastOrder;
    }

    public static class LastOrderBean {
        /**
         * hasLastOrder : true
         * lastTime : {"epochSecond":1602659176,"nano":0}
         * lastDuration : 02:10
         */

        private boolean hasLastOrder;
        private LastTimeBean lastTime;
        private String lastDuration;

        public boolean isHasLastOrder() {
            return hasLastOrder;
        }

        public void setHasLastOrder(boolean hasLastOrder) {
            this.hasLastOrder = hasLastOrder;
        }

        public LastTimeBean getLastTime() {
            return lastTime;
        }

        public void setLastTime(LastTimeBean lastTime) {
            this.lastTime = lastTime;
        }

        public String getLastDuration() {
            return lastDuration;
        }

        public void setLastDuration(String lastDuration) {
            this.lastDuration = lastDuration;
        }

        public static class LastTimeBean {
            /**
             * epochSecond : 1602659176
             * nano : 0
             */

            private int epochSecond;
            private int nano;

            public int getEpochSecond() {
                return epochSecond;
            }

            public void setEpochSecond(int epochSecond) {
                this.epochSecond = epochSecond;
            }

            public int getNano() {
                return nano;
            }

            public void setNano(int nano) {
                this.nano = nano;
            }
        }
    }
}
