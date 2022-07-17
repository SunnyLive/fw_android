package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/12
 */
public class PlaneAllDto {

    /**
     * anchorIsReach : true
     * icon : string
     * id : 0
     * name : string
     * planeOpenSecond : 0
     * score : 0
     * userIsOpen : true
     */

    private boolean anchorIsReach;
    private String icon;
    private int id;
    private String name;
    private int planeOpenSecond;
    private int score;
    private boolean userIsOpen;
    private boolean isChenck;

    public boolean isChenck() {
        return isChenck;
    }

    public void setChenck(boolean chenck) {
        isChenck = chenck;
    }

    public boolean isAnchorIsReach() {
        return anchorIsReach;
    }

    public void setAnchorIsReach(boolean anchorIsReach) {
        this.anchorIsReach = anchorIsReach;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaneOpenSecond() {
        return planeOpenSecond;
    }

    public void setPlaneOpenSecond(int planeOpenSecond) {
        this.planeOpenSecond = planeOpenSecond;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isUserIsOpen() {
        return userIsOpen;
    }

    public void setUserIsOpen(boolean userIsOpen) {
        this.userIsOpen = userIsOpen;
    }
}
