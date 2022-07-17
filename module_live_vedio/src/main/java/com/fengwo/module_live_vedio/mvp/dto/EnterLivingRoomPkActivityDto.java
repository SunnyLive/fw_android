package com.fengwo.module_live_vedio.mvp.dto;

public class EnterLivingRoomPkActivityDto {

    /**
     * activityId : 0
     * actStatus : 0
     * address : string
     * backgroundImg : string
     * channelId : 0
     * combatScore : 0
     * isLegend : 0
     * levelIcon : string
     * levelId : 0
     * levelName : string
     * losingStreakNum : 0
     * winningStreakNum : 0
     */

    private int actStatus;
    private int activityId;
    private String address;
    private String notice;//进场公告
    private String backgroundImg;
    private int channelId;
    private int combatScore;
    private int isLegend;
    private String levelIcon;
    private int levelId;
    private String levelName;
    private int losingStreakNum;
    private int winningStreakNum;
    private String channelFrame;//直播间内边框
    private String channelInnerLable;//直播间内标签

    public int getActStatus() {
        return actStatus;
    }

    public void setActStatus(int actStatus) {
        this.actStatus = actStatus;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getCombatScore() {
        return combatScore;
    }

    public void setCombatScore(int combatScore) {
        this.combatScore = combatScore;
    }

    public int getIsLegend() {
        return isLegend;
    }

    public void setIsLegend(int isLegend) {
        this.isLegend = isLegend;
    }

    public String getLevelIcon() {
        return levelIcon;
    }

    public void setLevelIcon(String levelIcon) {
        this.levelIcon = levelIcon;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLosingStreakNum() {
        return losingStreakNum;
    }

    public void setLosingStreakNum(int losingStreakNum) {
        this.losingStreakNum = losingStreakNum;
    }

    public int getWinningStreakNum() {
        return winningStreakNum;
    }

    public void setWinningStreakNum(int winningStreakNum) {
        this.winningStreakNum = winningStreakNum;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getChannelFrame() {
        return channelFrame;
    }

    public void setChannelFrame(String channelFrame) {
        this.channelFrame = channelFrame;
    }

    public String getChannelInnerLable() {
        return channelInnerLable;
    }

    public void setChannelInnerLable(String channelInnerLable) {
        this.channelInnerLable = channelInnerLable;
    }

    @Override
    public String toString() {
        return "EnterLivingRoomActivityDto{" +
                "actStatus=" + actStatus +
                ", activityId=" + activityId +
                ", address='" + address + '\'' +
                ", notice='" + notice + '\'' +
                ", backgroundImg='" + backgroundImg + '\'' +
                ", channelId=" + channelId +
                ", combatScore=" + combatScore +
                ", isLegend=" + isLegend +
                ", levelIcon='" + levelIcon + '\'' +
                ", levelId=" + levelId +
                ", levelName='" + levelName + '\'' +
                ", losingStreakNum=" + losingStreakNum +
                ", winningStreakNum=" + winningStreakNum +
                ", channelFrame='" + channelFrame + '\'' +
                ", channelInnerLable='" + channelInnerLable + '\'' +
                '}';
    }
}
