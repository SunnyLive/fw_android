package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/10
 */
public class PlaneGiftDto {
    /**
     * boxId : 0
     * boxName : string
     * conditionQuantity : 0
     * factQuantity : 0
     * giftList : [{"giftIcon":"string","giftId":0,"giftName":"string","giftQuantity":0}]
     * rewardIntegral : 0
     */

    private int boxId;
    private String boxName;
    private int conditionQuantity;
    private int factQuantity;
    private int rewardIntegral;
    private List<GiftListBean> giftList;

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public int getConditionQuantity() {
        return conditionQuantity;
    }

    public void setConditionQuantity(int conditionQuantity) {
        this.conditionQuantity = conditionQuantity;
    }

    public int getFactQuantity() {
        return factQuantity;
    }

    public void setFactQuantity(int factQuantity) {
        this.factQuantity = factQuantity;
    }

    public int getRewardIntegral() {
        return rewardIntegral;
    }

    public void setRewardIntegral(int rewardIntegral) {
        this.rewardIntegral = rewardIntegral;
    }

    public List<GiftListBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListBean> giftList) {
        this.giftList = giftList;
    }

    public static class GiftListBean {
        /**
         * giftIcon : string
         * giftId : 0
         * giftName : string
         * giftQuantity : 0
         */

        private String giftIcon;
        private int giftId;
        private String giftName;
        private int giftQuantity;

        public String getGiftIcon() {
            return giftIcon;
        }

        public void setGiftIcon(String giftIcon) {
            this.giftIcon = giftIcon;
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

        public int getGiftQuantity() {
            return giftQuantity;
        }

        public void setGiftQuantity(int giftQuantity) {
            this.giftQuantity = giftQuantity;
        }
    }
}
