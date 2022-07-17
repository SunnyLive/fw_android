package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_comment.bean.HourProcessesBean;
import com.fengwo.module_comment.bean.RefreshProcessesBean;
import com.fengwo.module_comment.bean.ResidueProcessesBean;
import com.fengwo.module_live_vedio.mvp.UserGoodBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/10/23
 */
public class NewActivityDto {




    /**
     * activityId : 8
     * actStatus : 1
     * channelId : 880088
     * nickname : 小辣椒😉
     * noticeBgImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/activity/christmas/notice_bg.png
     * sort : 1
     * notice : 我正在参加“冰雪奇缘双旦Party · 圣诞”活动，快来为甜蜜加冕~
     * castleSort : 2
     * integral : 2999
     * planeOpenSecond : null
     * isFinish : false
     * address : /activeChristmas
     * userGood : {"userId":880088,"goodsId":1,"numb":0,"goodsIcon":"","lottoSockPropCount":18}
     * refreshProcesses : [{"giftId":202,"giftName":"甜心平安果","customsValue":10,"customsTotalValue":10,"icon":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1606999269000*gift3480222758.png"},{"giftId":203,"giftName":"神秘礼袜","customsValue":20,"customsTotalValue":20,"icon":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1606999307000*gift9077721563.png"},{"giftId":204,"giftName":"圣诞女神","customsValue":10,"customsTotalValue":30,"icon":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1606999338000*gift8221887037.png"}]
     */

    private int activityId;
    private int actStatus;
    private int channelId;
    private String nickname;
    private String noticeBgImg;
    private int sort;
    private String notice;
    private int castleSort;
    private int integral;
    private int planeOpenSecond;
    private boolean isFinish;
    private String address;
    private boolean isChance;
    private UserGoodBean userGood;
    private long newYearCountingDown;

    private List<RefreshProcessesBean> refreshProcesses;
//    private List<HourProcessesBean> hourProcesses;
//    private List<ResidueProcessesBean> residueProcesses;

    public boolean isChance() {
        return isChance;
    }

    public void setChance(boolean chance) {
        isChance = chance;
    }


//    public List<HourProcessesBean> getHourProcesses() {
//        return hourProcesses;
//    }
//
//    public void setHourProcesses(List<HourProcessesBean> hourProcesses) {
//        this.hourProcesses = hourProcesses;
//    }
//
//    public List<ResidueProcessesBean> getResidueProcesses() {
//        return residueProcesses;
//    }
//
//    public void setResidueProcesses(List<ResidueProcessesBean> residueProcesses) {
//        this.residueProcesses = residueProcesses;
//    }

    public long getNewYearCountingDown() {
        return newYearCountingDown;
    }

    public void setNewYearCountingDown(long newYearCountingDown) {
        this.newYearCountingDown = newYearCountingDown;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public List<RefreshProcessesBean> getRefreshProcesses() {
        return refreshProcesses;
    }

    public void setRefreshProcesses(List<RefreshProcessesBean> refreshProcesses) {
        this.refreshProcesses = refreshProcesses;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActStatus() {
        return actStatus;
    }

    public void setActStatus(int actStatus) {
        this.actStatus = actStatus;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNoticeBgImg() {
        return noticeBgImg;
    }

    public void setNoticeBgImg(String noticeBgImg) {
        this.noticeBgImg = noticeBgImg;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getCastleSort() {
        return castleSort;
    }

    public void setCastleSort(int castleSort) {
        this.castleSort = castleSort;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getPlaneOpenSecond() {
        return planeOpenSecond;
    }

    public void setPlaneOpenSecond(int planeOpenSecond) {
        this.planeOpenSecond = planeOpenSecond;
    }

    public boolean isIsFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserGoodBean getUserGood() {
        return userGood;
    }

    public void setUserGood(UserGoodBean userGood) {
        this.userGood = userGood;
    }







}
