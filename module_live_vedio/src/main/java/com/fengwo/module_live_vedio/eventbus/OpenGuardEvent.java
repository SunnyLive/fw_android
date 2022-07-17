package com.fengwo.module_live_vedio.eventbus;

public class OpenGuardEvent {
    private String icon;
    private String name;
    private int id;

    public OpenGuardEvent(String guardIconX, String itemNameX,int id) {
        icon = guardIconX;
        name = itemNameX;
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
