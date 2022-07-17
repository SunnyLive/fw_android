package com.fengwo.module_live_vedio.mvp.dto;

public class SearchDto {

    /**
     * attention : 0
     * headImg : string
     * hostLevel : 0
     * id : 0
     * nickname : string
     * sex : 0
     * signature : string
     * userLevel : 0
     */

    private int attention;
    private String headImg;
    private int hostLevel;
    private int id;
    private String nickname;
    private int sex;
    private int age;
    private String signature;
    private String userRole;
    private int userLevel;
    private int channelStatus;
    private int liveStatus;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getChannelStatus() {
        return channelStatus;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public void setChannelStatus(int channelStatus) {
        this.channelStatus = channelStatus;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getHostLevel() {
        return hostLevel;
    }

    public void setHostLevel(int hostLevel) {
        this.hostLevel = hostLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
}
