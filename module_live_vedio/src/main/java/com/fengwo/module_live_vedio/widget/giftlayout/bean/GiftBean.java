package com.fengwo.module_live_vedio.widget.giftlayout.bean;

import com.fengwo.module_live_vedio.mvp.dto.GiftDto;

import java.io.Serializable;

public class GiftBean implements Serializable, Comparable<GiftBean> {

    public String userid;
    public int channelId;
    public int id;
    public String giftName;
    public String giftImage;
    public int userAvatar;
    public String userName;
    public int group;
    public long sortNum;
    public int groupNum;
    public String userLevel;
    public String userShouHuLevel;
    public String userVipLevel;
    public String vip_invisible;
    public String gifttype;
    public String bigName;
    public boolean isCar = false;
    public Integer frameRate = 80;
    public String hederUrl = "";
    public String carDes;
    public int price;
    public String swf;
    public int weight = 1;
    public boolean isPlay = false;
    public boolean isMine = false;

    public boolean isToutiao;

    public static String getSwfName(String giftSwf) {
        int index = giftSwf.lastIndexOf("/");
        int length = giftSwf.length();
        if (index > 0) {
            String swfTemp = giftSwf.substring(index + 1, length);
            return swfTemp.split("\\.")[0];
        }
        return "";
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getGifttype() {
        return gifttype;
    }

    public void setGifttype(String gifttype) {
        this.gifttype = gifttype;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserShouHuLevel() {
        return userShouHuLevel;
    }

    public void setUserShouHuLevel(String userShouHuLevel) {
        this.userShouHuLevel = userShouHuLevel;
    }

    public String getUserVipLevel() {
        return userVipLevel;
    }

    public void setUserVipLevel(String userVipLevel) {
        this.userVipLevel = userVipLevel;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public int getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public long getSortNum() {
        return sortNum;
    }

    public void setSortNum(long sortNum) {
        this.sortNum = sortNum;
    }

    public String getVip_invisible() {
        return vip_invisible;
    }

    public void setVip_invisible(String vip_invisible) {
        this.vip_invisible = vip_invisible;
    }

    @Override
    public int compareTo(GiftBean bean) {
        return this.weight - bean.weight;
    }
}