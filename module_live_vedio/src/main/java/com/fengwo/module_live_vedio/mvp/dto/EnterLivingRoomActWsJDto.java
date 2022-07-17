package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_comment.bean.RefreshProcessesBean;
import com.fengwo.module_live_vedio.mvp.UserGoodBean;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/11
 */
public class EnterLivingRoomActWsJDto {

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
    public UserGoodBean userGood;
    private List<RefreshProcessesBean> refreshProcesses;


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
