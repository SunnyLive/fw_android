package com.fengwo.module_flirt.bean;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/5/4
 */
public class GiftRecordBean {

    /**
     * bigImgPath : string
     * conNum : 0
     * conPrice : 0
     * conType : 0
     * giftName : string
     * giftPrice : string
     * smallImgPath : string
     * targetId : 0
     */

    private String bigImgPath;
    private int conNum;
    private int conPrice;
    private int conType;
    private String giftName;
    private String giftPrice;
    private String smallImgPath;
    private int targetId;

    public String getBigImgPath() {
        return bigImgPath;
    }

    public void setBigImgPath(String bigImgPath) {
        this.bigImgPath = bigImgPath;
    }

    public int getConNum() {
        return conNum;
    }

    public void setConNum(int conNum) {
        this.conNum = conNum;
    }

    public int getConPrice() {
        return conPrice;
    }

    public void setConPrice(int conPrice) {
        this.conPrice = conPrice;
    }

    public int getConType() {
        return conType;
    }

    public void setConType(int conType) {
        this.conType = conType;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }

    public String getSmallImgPath() {
        return smallImgPath;
    }

    public void setSmallImgPath(String smallImgPath) {
        this.smallImgPath = smallImgPath;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}
