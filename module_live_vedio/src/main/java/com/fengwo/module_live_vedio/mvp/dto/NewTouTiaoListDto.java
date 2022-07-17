package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

public class NewTouTiaoListDto {

    /**
     * contain : ["string"]
     * createTime : 2019-12-18T08:18:46.854Z
     * frameRate : 0
     * giftId : string
     * giftPackageIcon : string
     * giftPackageName : string
     * giftPackagePrice : 0
     * giftPackageSwf : string
     * id : 0
     * quantity : string
     * sort : 0
     * status : 0
     * swfPlay : 0
     * swfTime : 0
     * updateTime : 2019-12-18T08:18:46.854Z
     */

    private String createTime;
    private int frameRate;
    private String giftId;
    private String giftPackageIcon;
    private String giftPackageName;
    private int giftPackagePrice;
    private String giftPackageSwf;
    private int id;
    private String quantity;
    private int sort;
    private int status;
    private int swfPlay;
    private int swfTime;
    private String updateTime;
    private List<String> contain;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftPackageIcon() {
        return giftPackageIcon;
    }

    public void setGiftPackageIcon(String giftPackageIcon) {
        this.giftPackageIcon = giftPackageIcon;
    }

    public String getGiftPackageName() {
        return giftPackageName;
    }

    public void setGiftPackageName(String giftPackageName) {
        this.giftPackageName = giftPackageName;
    }

    public int getGiftPackagePrice() {
        return giftPackagePrice;
    }

    public void setGiftPackagePrice(int giftPackagePrice) {
        this.giftPackagePrice = giftPackagePrice;
    }

    public String getGiftPackageSwf() {
        return giftPackageSwf;
    }

    public void setGiftPackageSwf(String giftPackageSwf) {
        this.giftPackageSwf = giftPackageSwf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getContain() {
        return contain;
    }

    public void setContain(List<String> contain) {
        this.contain = contain;
    }
}
