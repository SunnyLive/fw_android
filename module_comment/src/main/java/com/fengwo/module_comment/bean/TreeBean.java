package com.fengwo.module_comment.bean;

/**
 * @anchor Administrator
 * @date 2020/12/8
 */
public class TreeBean {


    /**
     * goodsDuration : 0
     * goodsIcon : string
     * goodsId : 0
     * goodsName : string
     * goodsType : 0
     * hasAward : true
     */

    private int goodsDuration;
    private String goodsIcon;
    private int goodsId;
    private String goodsName;
    private int goodsType;
    private boolean hasAward;

    public int getGoodsDuration() {
        return goodsDuration;
    }

    public void setGoodsDuration(int goodsDuration) {
        this.goodsDuration = goodsDuration;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public boolean isHasAward() {
        return hasAward;
    }

    public void setHasAward(boolean hasAward) {
        this.hasAward = hasAward;
    }
}
