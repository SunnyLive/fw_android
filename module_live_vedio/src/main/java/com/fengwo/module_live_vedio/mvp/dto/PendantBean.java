package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @anchor Administrator
 * @date 2020/8/31
 */
public class PendantBean {
//    {
//        "id": 0,
//            "stickerType": 0,
//            "stickerUrl": "string",
//            "textLength": 0
//    }

    //标题类型
    public static final int TITLE_TYPE=1;
    //挂件类型
    public static final int PENDANT_TYPE=0;

    private int outType;

    private int id ;
    private int stickerType;
    private String stickerUrl;
    private int textLength;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStickerType() {
        return stickerType;
    }

    public void setStickerType(int stickerType) {
        this.stickerType = stickerType;
    }

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }
}
