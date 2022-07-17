package com.fengwo.module_chat.mvp.model.bean;

public class ChatAllPeopleBean {
    private int id;
    private boolean isSelected = false;
    private String imgUrl;
    private String name;

    public boolean isSelected() {
        return isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
