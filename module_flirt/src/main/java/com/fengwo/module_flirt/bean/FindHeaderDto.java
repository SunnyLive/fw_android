package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/8/12 11:23
 */
public class FindHeaderDto {
    /**
     * headImg : string
     * userId : 0
     */
    private String headImg;
    private int userId;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
