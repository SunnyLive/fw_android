package com.fengwo.module_live_vedio.eventbus;

public class BuyGuardSuccessEvent {
    private String icon;
    private String name;
    private String experience;

    public BuyGuardSuccessEvent(String guardIconX, String itemNameX,String experience) {
        icon = guardIconX;
        name = itemNameX;
        this.experience = experience;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
