package com.fengwo.module_comment.Interfaces;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/9/24
 */
public interface ICardType {
    //1:图片，2：视频
    int getSourceType();
    String getPoster();
    String getUrl();
}
