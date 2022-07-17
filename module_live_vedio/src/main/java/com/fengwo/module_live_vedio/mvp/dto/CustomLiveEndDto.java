package com.fengwo.module_live_vedio.mvp.dto;

import java.time.Instant;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/4
 */
public class CustomLiveEndDto {

    private Integer channelId;
    /** 标题 */
    private String title;
    /** 直播封面 */
    private String thumb;
    /** 本次直播收益 */
    private Integer profit;
    /** 观看次数 */
    private Integer lookTimes;
    /** 虚拟人数 */
    private Integer virtualNums;
    /** 分享次数 */
    private Integer shareTimes;
    /** 直播时长 */
    private Integer liveTime;
    /** 设备信息 */
    private String deviceInfo;
    private int lookNums;
    private int liveTimes;

    public int getLookNums() {
        return lookNums;
    }

    public void setLookNums(int lookNums) {
        this.lookNums = lookNums;
    }

    public int getLiveTimes() {
        return liveTimes;
    }

    public void setLiveTimes(int liveTimes) {
        this.liveTimes = liveTimes;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getLookTimes() {
        return lookTimes;
    }

    public void setLookTimes(Integer lookTimes) {
        this.lookTimes = lookTimes;
    }

    public Integer getVirtualNums() {
        return virtualNums;
    }

    public void setVirtualNums(Integer virtualNums) {
        this.virtualNums = virtualNums;
    }

    public Integer getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(Integer shareTimes) {
        this.shareTimes = shareTimes;
    }

    public Integer getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(Integer liveTime) {
        this.liveTime = liveTime;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }


}
