package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @anchor Administrator
 * @date 2020/9/7
 */
public class StickersDto {

    /**
     * id : 0
     * stickerId : 0
     * stickerLocation : string
     * stickerText : string
     * stickerUrl : string
     */

    private int id;
    private int stickerId;
    private String stickerLocation;
    private String stickerText;
    private String stickerUrl;
    private int stickerType;
    private String textColor;

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getStickerType() {
        return stickerType;
    }

    public void setStickerType(int stickerType) {
        this.stickerType = stickerType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStickerId() {
        return stickerId;
    }

    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerLocation() {
        return stickerLocation;
    }

    public void setStickerLocation(String stickerLocation) {
        this.stickerLocation = stickerLocation;
    }

    public String getStickerText() {
        return stickerText;
    }

    public void setStickerText(String stickerText) {
        this.stickerText = stickerText;
    }

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }
}
