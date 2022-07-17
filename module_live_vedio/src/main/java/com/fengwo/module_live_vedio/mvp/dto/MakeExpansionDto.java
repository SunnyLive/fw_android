package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @anchor Administrator
 * @date 2020/9/2
 */
public class MakeExpansionDto {
    private int ivImg;
    private String title;
    public MakeExpansionDto(int ivImg, String title) {
        this.ivImg = ivImg;
        this.title = title;

    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIvImg() {
        return ivImg;
    }

    public void setIvImg(int ivImg) {
        this.ivImg = ivImg;
    }
}
