package com.fengwo.module_live_vedio.mvp.dto;

public class BrokerRankDto {


    /**
     * id : 498
     * userId : 498
     * userNickname : 用户498
     * userHeadImg : null
     * isAttension : 0
     * inviteCount : 1
     * superiorId : 363
     * superiorNickname : null
     * superiorDivide : 0
     * prevSuperiorId : 0
     * prevSuperiorNickname : null
     * prevSuperiorDivide : 0
     * familyId : 0
     * familyName : null
     * path :
     * createTime : 2019-11-02T04:04:40Z
     */

    private int id;
    private int userId;
    private String userNickname;
    private String userHeadImg;
    private int isAttension;
    private int inviteCount;
    private int superiorId;
    private String superiorNickname;
    private String prevSuperiorNickname;
//    private int prevSuperiorDivide;
    private int familyId;
    private String familyName;
    private String path;
    private String createTime;
    private String totalDivide;

    public String getTotalDivide() {
        return totalDivide;
    }

    public void setTotalDivide(String totalDivide) {
        this.totalDivide = totalDivide;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public int getIsAttension() {
        return isAttension;
    }

    public void setIsAttension(int isAttension) {
        this.isAttension = isAttension;
    }

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public int getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(int superiorId) {
        this.superiorId = superiorId;
    }

    public String getSuperiorNickname() {
        return superiorNickname;
    }

    public void setSuperiorNickname(String superiorNickname) {
        this.superiorNickname = superiorNickname;
    }

//    public String getSuperiorDivide() {
//        return superiorDivide;
//    }
//
//    public void setSuperiorDivide(String superiorDivide) {
//        this.superiorDivide = superiorDivide;
//    }
//
//    public String getPrevSuperiorId() {
//        return prevSuperiorId;
//    }
//
//    public void setPrevSuperiorId(String prevSuperiorId) {
//        this.prevSuperiorId = prevSuperiorId;
//    }

    public String getPrevSuperiorNickname() {
        return prevSuperiorNickname;
    }

    public void setPrevSuperiorNickname(String prevSuperiorNickname) {
        this.prevSuperiorNickname = prevSuperiorNickname;
    }

//    public int getPrevSuperiorDivide() {
//        return prevSuperiorDivide;
//    }
//
//    public void setPrevSuperiorDivide(int prevSuperiorDivide) {
//        this.prevSuperiorDivide = prevSuperiorDivide;
//    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
