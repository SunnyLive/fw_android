package com.fengwo.module_chat.mvp.model.bean;

public class PostCardBean {
    public int high;
    public String imageUrl;
    public String videoImgUrl;
    public int typeNew;
    public int type;
    public int width;

    //high	integer($int32)
    //高
    //
    //imageUrl	string
    //图片
    //
    //typeNew	integer($int32)
    //类型：0图片，1封面 2视频
    //
    //videoImgUrl	string
    //类型为2视频的封面图片
    //
    //width	integer($int32)
    //


    public PostCardBean(int high, String imageUrl, String videoImgUrl, int typeNew, int type, int width) {
        this.high = high;
        this.imageUrl = imageUrl;
        this.videoImgUrl = videoImgUrl;
        this.typeNew = typeNew;
        this.type = type;
        this.width = width;
    }
}
