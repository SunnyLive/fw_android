package com.fengwo.module_flirt.bean;

import com.fengwo.module_comment.Interfaces.ICardType;

import java.io.Serializable;

/**
 * @Author BLCS
 * @Time 2020/8/12 14:40
 */
public class CoverDto implements Serializable, ICardType {
    public int high;
    public String imageUrl;
    public int type;
    public int width;
    public int typeNew;
    public String videoImgUrl;
    public String cardId;

    @Override
    public int getSourceType() {
        return imageUrl.endsWith(".mp4") ? 2 : 1;
    }

    @Override
    public String getPoster() {
        return videoImgUrl;
    }

    @Override
    public String getUrl() {
        return imageUrl;
    }
}
