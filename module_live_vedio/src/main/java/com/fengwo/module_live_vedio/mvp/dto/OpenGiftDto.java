package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/13
 */
public class OpenGiftDto {

    /**
     * boxGiftIcon : string
     * boxGiftName : string
     * boxGiftValue : 0
     * boxType : 0
     * giftMotorId : 0
     * giftMotorName : string
     * giftType : 0
     * id : 0
     * probability : 0
     * treasureBoxId : 0
     */

    private String boxGiftIcon;
    private String boxGiftName;
    private int boxGiftValue;
    private int boxType;
    private int giftMotorId;
    private String giftMotorName;
    private String giftShowName;
    private String giftMotoringSwf;
    private int giftType;
    private int id;
    private int probability;
    private int treasureBoxId;
    private int giftMotorLevel;//礼物等级，-1低，0相同，1高

    public String getGiftMotoringSwf() {
        return giftMotoringSwf;
    }

    public void setGiftMotoringSwf(String giftMotoringSwf) {
        this.giftMotoringSwf = giftMotoringSwf;
    }

    public int getGiftMotorLevel() {
        return giftMotorLevel;
    }

    public void setGiftMotorLevel(int giftMotorLevel) {
        this.giftMotorLevel = giftMotorLevel;
    }

    public String getGiftShowName() {
        return giftShowName;
    }

    public void setGiftShowName(String giftShowName) {
        this.giftShowName = giftShowName;
    }

    public String getBoxGiftIcon() {
        return boxGiftIcon;
    }

    public void setBoxGiftIcon(String boxGiftIcon) {
        this.boxGiftIcon = boxGiftIcon;
    }

    public String getBoxGiftName() {
        return boxGiftName;
    }

    public void setBoxGiftName(String boxGiftName) {
        this.boxGiftName = boxGiftName;
    }

    public int getBoxGiftValue() {
        return boxGiftValue;
    }

    public void setBoxGiftValue(int boxGiftValue) {
        this.boxGiftValue = boxGiftValue;
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }

    public int getGiftMotorId() {
        return giftMotorId;
    }

    public void setGiftMotorId(int giftMotorId) {
        this.giftMotorId = giftMotorId;
    }

    public String getGiftMotorName() {
        return giftMotorName;
    }

    public void setGiftMotorName(String giftMotorName) {
        this.giftMotorName = giftMotorName;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public int getTreasureBoxId() {
        return treasureBoxId;
    }

    public void setTreasureBoxId(int treasureBoxId) {
        this.treasureBoxId = treasureBoxId;
    }
}
