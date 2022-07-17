package com.fengwo.module_live_vedio.mvp;

/**
 * @anchor Administrator
 * @date 2020/12/8
 */
public class UserGoodBean {
    /**
     * userId : 880088
     * goodsId : 1
     * numb : 0
     * goodsIcon :
     * lottoSockPropCount : 18
     */

    private int userId;
    private int goodsId;
    private int numb;
    private String goodsIcon;
    private int lottoSockPropCount;
    private String h5Url;


    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public int getLottoSockPropCount() {
        return lottoSockPropCount;
    }

    public void setLottoSockPropCount(int lottoSockPropCount) {
        this.lottoSockPropCount = lottoSockPropCount;
    }
}