package com.fengwo.module_comment.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ZhuboDto implements Serializable, Cloneable {
    public int activityId;//活动的Id 1:告白天使，2：唤醒蜂后 3：pk排位赛
    public int activityStatus;//活动状态:0活动未开始或结束，1Top1，2Top2,3Top3,4top4,5告白女神，6活动进行中(非top123主播)
    public String lastChannelFrame;//上个活动的背景边框
    public String lastChannelLable;//上个活动的标签
    public int channelId;
    public int enablePk;//0未开启，1开启;
    public String nickname;
    public String thumb;
    public String title;
    public String roomId;
    public String createTime;
    public int channelLevel;
    public String pullUrlRTMP;
    public String pullUrlFLV;
    public String pullUrlHLS;
    public String headImg;
    public List<String> menus;
    public int lookNums;
    public int pkStatus;
    public int outOrHouse;
    public long weekProfit;
    public int type;
    public int status;//2 跳转直播间 其他的都个人中心
    public String levelName;//段位名称
    //    public String channelInnerLable;//直播间内标签
//    public String channelFrame;//直播间内边框
    public String channelOuterLable;//首页标签
    public int isHaveRed;//是否有红包：1-有;其他-没有

    public boolean isHouse() {
        return outOrHouse == 1;
    }

    @NonNull
    @Override
    public ZhuboDto clone() throws CloneNotSupportedException {
        return (ZhuboDto) super.clone();
    }

    /**
     * 更新数据
     *
     * @param z
     */
    public void update(ZhuboDto z) {
        this.activityId = z.activityId;
        this.activityStatus = z.activityStatus;
        this.lastChannelFrame = z.lastChannelFrame;
        this.lastChannelLable = z.lastChannelLable;
        this.channelId = z.channelId;
        this.enablePk = z.enablePk;
        this.nickname = z.nickname;
        this.thumb = z.thumb;
        this.title = z.title;
        this.roomId = z.roomId;
        this.createTime = z.createTime;
        this.channelLevel = z.channelLevel;
        this.pullUrlRTMP = z.pullUrlRTMP;
        this.pullUrlFLV = z.pullUrlFLV;
        this.pullUrlHLS = z.pullUrlHLS;
        this.headImg = z.headImg;
        this.menus = z.menus;
        this.lookNums = z.lookNums;
        this.pkStatus = z.pkStatus;
        this.outOrHouse = z.outOrHouse;
        this.weekProfit = z.weekProfit;
        this.type = z.type;
        this.status = z.status;
        this.levelName = z.levelName;
        this.channelOuterLable = z.channelOuterLable;
        this.isHaveRed = z.isHaveRed;
    }

    @Override
    public String toString() {
        return "ZhuboDto{" +
                "activityId=" + activityId +
                ", activityStatus=" + activityStatus +
                ", lastChannelFrame='" + lastChannelFrame + '\'' +
                ", lastChannelLable='" + lastChannelLable + '\'' +
                ", channelId=" + channelId +
                ", enablePk=" + enablePk +
                ", nickname='" + nickname + '\'' +
                ", thumb='" + thumb + '\'' +
                ", title='" + title + '\'' +
                ", roomId='" + roomId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", channelLevel=" + channelLevel +
                ", pullUrlRTMP='" + pullUrlRTMP + '\'' +
                ", pullUrlFLV='" + pullUrlFLV + '\'' +
                ", pullUrlHLS='" + pullUrlHLS + '\'' +
                ", headImg='" + headImg + '\'' +
                ", menus=" + menus +
                ", lookNums=" + lookNums +
                ", pkStatus=" + pkStatus +
                ", outOrHouse=" + outOrHouse +
                ", weekProfit=" + weekProfit +
                ", type=" + type +
                ", status=" + status +
                ", levelName='" + levelName + '\'' +
                ", channelOuterLable='" + channelOuterLable + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZhuboDto zhuboDto = (ZhuboDto) o;
        return activityId == zhuboDto.activityId &&
                activityStatus == zhuboDto.activityStatus &&
                channelId == zhuboDto.channelId &&
                enablePk == zhuboDto.enablePk &&
                channelLevel == zhuboDto.channelLevel &&
                lookNums == zhuboDto.lookNums &&
                pkStatus == zhuboDto.pkStatus &&
                outOrHouse == zhuboDto.outOrHouse &&
                weekProfit == zhuboDto.weekProfit &&
                type == zhuboDto.type &&
                status == zhuboDto.status &&
                isHaveRed == zhuboDto.isHaveRed &&
                Objects.equals(lastChannelFrame, zhuboDto.lastChannelFrame) &&
                Objects.equals(lastChannelLable, zhuboDto.lastChannelLable) &&
                Objects.equals(nickname, zhuboDto.nickname) &&
                Objects.equals(thumb, zhuboDto.thumb) &&
                Objects.equals(title, zhuboDto.title) &&
                Objects.equals(roomId, zhuboDto.roomId) &&
                Objects.equals(createTime, zhuboDto.createTime) &&
                Objects.equals(pullUrlRTMP, zhuboDto.pullUrlRTMP) &&
                Objects.equals(pullUrlFLV, zhuboDto.pullUrlFLV) &&
                Objects.equals(pullUrlHLS, zhuboDto.pullUrlHLS) &&
                Objects.equals(headImg, zhuboDto.headImg) &&
                Objects.equals(menus, zhuboDto.menus) &&
                Objects.equals(levelName, zhuboDto.levelName) &&
                Objects.equals(channelOuterLable, zhuboDto.channelOuterLable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, activityStatus, lastChannelFrame, lastChannelLable, channelId, enablePk, nickname, thumb, title, roomId, createTime, channelLevel, pullUrlRTMP, pullUrlFLV, pullUrlHLS, headImg, menus, lookNums, pkStatus, outOrHouse, weekProfit, type, status, levelName, channelOuterLable, isHaveRed);
    }
}
