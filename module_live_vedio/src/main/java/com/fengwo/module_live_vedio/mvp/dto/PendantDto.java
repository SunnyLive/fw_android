package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @anchor Administrator
 * @date 2020/9/5
 */
public class PendantDto {


    /**
     * id : 86
     * stickerId : 17
     * stickerUrl : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/pendant/1599222856000*pendant3907678566.png
     * stickerText :
     * stickerLocation : 290,664
     */

    private int id;
    private int stickerId;
    private String stickerUrl;
    private String stickerText;
    private String stickerLocation;

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

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }

    public String getStickerText() {
        return stickerText;
    }

    public void setStickerText(String stickerText) {
        this.stickerText = stickerText;
    }

    public String getStickerLocation() {
        return stickerLocation;
    }

    public void setStickerLocation(String stickerLocation) {
        this.stickerLocation = stickerLocation;
    }
}
