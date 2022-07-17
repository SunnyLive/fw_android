package com.fengwo.module_login.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/22
 */
public class GuardDto {
    /**
     * guardDeadline : 2019-10-22T03:23:21.710Z
     * guardIcon : string
     * guardId : 0
     * guardName : string
     * guardUserHeadImg : string
     * guardUserId : 0
     * guardUserNickname : string
     * level : 0
     */

    private String guardDeadline;
    private String guardIcon;
    private int guardId;
    private String guardName;
    private String guardUserHeadImg;
    private int guardUserId;
    private String guardUserNickname;
    private int level;
    private int userId;
    private String levelIcon;

    public String getLevelIcon() {
        return levelIcon;
    }

    public void setLevelIcon(String l) {
        levelIcon = l;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGuardDeadline() {
        return guardDeadline;
    }

    public void setGuardDeadline(String guardDeadline) {
        this.guardDeadline = guardDeadline;
    }

    public String getGuardIcon() {
        return guardIcon;
    }

    public void setGuardIcon(String guardIcon) {
        this.guardIcon = guardIcon;
    }

    public int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }

    public String getGuardUserHeadImg() {
        return guardUserHeadImg;
    }

    public void setGuardUserHeadImg(String guardUserHeadImg) {
        this.guardUserHeadImg = guardUserHeadImg;
    }

    public int getGuardUserId() {
        return guardUserId;
    }

    public void setGuardUserId(int guardUserId) {
        this.guardUserId = guardUserId;
    }

    public String getGuardUserNickname() {
        return guardUserNickname;
    }

    public void setGuardUserNickname(String guardUserNickname) {
        this.guardUserNickname = guardUserNickname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
