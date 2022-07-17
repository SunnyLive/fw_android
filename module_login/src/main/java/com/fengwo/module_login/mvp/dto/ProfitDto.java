package com.fengwo.module_login.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/18
 */
public class ProfitDto {

    /**
     * inviteCount : 3
     * inviteProfitCount : 0
     * superiorId : 69
     * superiorDivide : 12.8
     * superiorGiftTotal : 0
     * prevSuperiorId : 56
     * prevSuperiorDivide : 0.0
     * familyId : 1
     * path : 56,69,80
     */

    private int inviteCount;
    private int inviteProfitCount;
    private int superiorId;
    private double superiorDivide;
    private int superiorGiftTotal;
    private int prevSuperiorId;
    private double prevSuperiorDivide;
    private int familyId;
    private String path;

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public int getInviteProfitCount() {
        return inviteProfitCount;
    }

    public void setInviteProfitCount(int inviteProfitCount) {
        this.inviteProfitCount = inviteProfitCount;
    }

    public int getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(int superiorId) {
        this.superiorId = superiorId;
    }

    public double getSuperiorDivide() {
        return superiorDivide;
    }

    public void setSuperiorDivide(double superiorDivide) {
        this.superiorDivide = superiorDivide;
    }

    public int getSuperiorGiftTotal() {
        return superiorGiftTotal;
    }

    public void setSuperiorGiftTotal(int superiorGiftTotal) {
        this.superiorGiftTotal = superiorGiftTotal;
    }

    public int getPrevSuperiorId() {
        return prevSuperiorId;
    }

    public void setPrevSuperiorId(int prevSuperiorId) {
        this.prevSuperiorId = prevSuperiorId;
    }

    public double getPrevSuperiorDivide() {
        return prevSuperiorDivide;
    }

    public void setPrevSuperiorDivide(double prevSuperiorDivide) {
        this.prevSuperiorDivide = prevSuperiorDivide;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
