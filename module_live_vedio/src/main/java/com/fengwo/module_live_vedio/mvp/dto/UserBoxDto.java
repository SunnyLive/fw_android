package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/14
 */
public class UserBoxDto {

    /**
     * boxOpenMin : 0
     * boxOpenSecond : 0
     * boxStatus : 0
     * giftShowName : string
     * giftShowValue : 0
     * icon : string
     * id : 0
     * name : string
     * url : string
     */

    private int boxOpenMin;
    private int boxOpenSecond;
    private int boxStatus;
    private String giftShowName;
    private int giftShowValue;
    private String icon;
    private String levelIcon;
    private int id;
    private String name;
    private String url;

    public String getLevelIcon() {
        return levelIcon;
    }

    public void setLevelIcon(String levelIcon) {
        this.levelIcon = levelIcon;
    }

    public int getBoxOpenMin() {
        return boxOpenMin;
    }

    public void setBoxOpenMin(int boxOpenMin) {
        this.boxOpenMin = boxOpenMin;
    }

    public int getBoxOpenSecond() {
        return boxOpenSecond;
    }

    public void setBoxOpenSecond(int boxOpenSecond) {
        this.boxOpenSecond = boxOpenSecond;
    }

    public int getBoxStatus() {
        return boxStatus;
    }

    public void setBoxStatus(int boxStatus) {
        this.boxStatus = boxStatus;
    }

    public String getGiftShowName() {
        return giftShowName;
    }

    public void setGiftShowName(String giftShowName) {
        this.giftShowName = giftShowName;
    }

    public int getGiftShowValue() {
        return giftShowValue;
    }

    public void setGiftShowValue(int giftShowValue) {
        this.giftShowValue = giftShowValue;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
