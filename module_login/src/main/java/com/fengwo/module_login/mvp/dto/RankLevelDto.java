package com.fengwo.module_login.mvp.dto;

public class RankLevelDto {
    private long experience;
    private String headImg;
    private long highest;
    private String icon;
    private String id;
    private int level;
    private long lowest;
    private String name;

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public long getHighest() {
        return highest;
    }

    public void setHighest(long highest) {
        this.highest = highest;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getLowest() {
        return lowest;
    }

    public void setLowest(long lowest) {
        this.lowest = lowest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
