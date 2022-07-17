package com.fengwo.module_flirt.bean;

import java.util.List;

public class ZipLabelDto {
    private int age;
    private int anchorId;
    private String headImg;
    private String nickname;
    private int sex;
    private int bstatus;
    private String tagName;

    //星球背景资源
    private int starBgRes;
    //是否为主播
    private boolean isAnchor;
    //星球大小等级 0：默认头像， 1 2 3（大->小）
    private int starGrade;

    public ZipLabelDto(String tagName) {
        this.tagName = tagName;
        this.isAnchor = false;
    }

    public ZipLabelDto(int starBgRes, String tagName, int starGrade) {
        this.starBgRes = starBgRes;
        this.tagName = tagName;
        this.starGrade = starGrade;
        this.isAnchor = false;
    }

    public ZipLabelDto(int age, int anchorId, String headImg, String nickname, int sex, int bstatus) {
        this.age = age;
        this.anchorId = anchorId;
        this.headImg = headImg;
        this.nickname = nickname;
        this.sex = sex;
        this.bstatus = bstatus;
        this.isAnchor = true;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getBstatus() {
        return bstatus;
    }

    public void setBstatus(int bstatus) {
        this.bstatus = bstatus;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getStarBgRes() {
        return starBgRes;
    }

    public boolean isAnchor() {
        return isAnchor;
    }

    public int getStarGrade() {
        return starGrade;
    }

    //星球大小等级 0：默认头像， 1 2 3（大->小）
    public interface StarGrade {
        int normalHead = 0;
        int grade1 = 1;
        int grade2 = 2;
        int grade3 = 3;
    }
}
