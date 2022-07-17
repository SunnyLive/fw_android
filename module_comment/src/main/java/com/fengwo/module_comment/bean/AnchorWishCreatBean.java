package com.fengwo.module_comment.bean;

/**
 * @Author BLCS
 * @Time 2020/7/8 11:17
 */
public class AnchorWishCreatBean {

    public AnchorWishCreatBean(int anchorId, int giftId, int repay, int wishQuantity, int wishType) {
        this.anchorId = anchorId;
        this.giftId = giftId;
        this.repay = repay;
        this.wishQuantity = wishQuantity;
        this.wishType = wishType;
    }

    @Override
    public String toString() {
        return "AnchorWishCreatBean{" +
                "anchorId=" + anchorId +
                ", giftId=" + giftId +
                ", repay=" + repay +
                ", wishQuantity=" + wishQuantity +
                ", wishType=" + wishType +
                '}';
    }

    /**
     * anchorId : 0
     * giftId : 0
     * repay : 0
     * wishQuantity : 0
     * wishType : 0
     */

    private int anchorId;
    private int giftId;
    private int repay;
    private int wishQuantity;
    private int wishType;

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getRepay() {
        return repay;
    }

    public void setRepay(int repay) {
        this.repay = repay;
    }

    public int getWishQuantity() {
        return wishQuantity;
    }

    public void setWishQuantity(int wishQuantity) {
        this.wishQuantity = wishQuantity;
    }

    public int getWishType() {
        return wishType;
    }

    public void setWishType(int wishType) {
        this.wishType = wishType;
    }
}
