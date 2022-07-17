package com.fengwo.module_flirt.bean;

public class XispBean {


    private int typeView;//类型 1 大文字  2 中文字  3 小文字  4 图片文字  5正在直播  6 用户
    private String title;//文字
    private String img;//图片
    private int backview;//背景本地资源图片
    private String id;//id  备用

    public XispBean(int typeView, String title, String img, int backview, String id) {
        this.typeView = typeView;
        this.title = title;
        this.img = img;
        this.backview = backview;
        this.id = id;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getBackview() {
        return backview;
    }

    public void setBackview(int backview) {
        this.backview = backview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
