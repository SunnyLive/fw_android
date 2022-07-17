package com.fengwo.module_live_vedio.mvp.dto;

import java.io.Serializable;

public class MapPoiDto implements Serializable {

    /**
     * id : 8
     * x : 1
     * y : 4
     * guildId : 1
     * cityLevel : 1
     * cityName : 蜂窝城
     * cityIcon : wweasdasd
     * cityRemark : 蜂窝平台公会xxxxxxx
     * state : 1
     */

    private int id;
    private int x;
    private int y;
    private int guildId;
    private String guildIcon;
    private String guildName;
    private int cityLevel;
    private int cityState;
    private String cityName;
    private String cityIcon;
    private String cityRemark;
    private String teamName;
    private int state;
    private boolean isSelected;

    public String getGuildIcon() {
        return guildIcon;
    }

    public void setGuildIcon(String guildIcon) {
        this.guildIcon = guildIcon;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public int getCityState() {
        return cityState;
    }

    public void setCityState(int cityState) {
        this.cityState = cityState;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGuildId() {
        return guildId;
    }

    public void setGuildId(int guildId) {
        this.guildId = guildId;
    }

    public int getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(int cityLevel) {
        this.cityLevel = cityLevel;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityIcon() {
        return cityIcon;
    }

    public void setCityIcon(String cityIcon) {
        this.cityIcon = cityIcon;
    }

    public String getCityRemark() {
        return cityRemark;
    }

    public void setCityRemark(String cityRemark) {
        this.cityRemark = cityRemark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
