package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/29
 */
public class CloseLiveDto implements Parcelable {

    /**
     * id : 5622
     * channelId : 508637
     * title : fbv
     * thumb : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1599474649572.jpg
     * nickname : 安慕兮🥰
     * familyName : null
     * profit : 0
     * lookTimes : 0
     * virtualNums : 26
     * shareTimes : null
     * startTime : 2020-09-07T10:30:57.477Z
     * liveTime : 8
     * fansNums : 0
     * liveTimes : null
     * endTime : 2020-09-07T10:31:05.415Z
     * deviceInfo : HUAWEI ELE-AL00
     * shareUrl : http://appdev.fengwohuyu.com/register?id=508637
     * sex : 2
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1583138449941.jpg
     * receiveGite : null
     * createTime : null
     * updateTime : null
     */

    private int id;
    private int channelId;
    private String title;
    private String thumb;
    private String nickname;
    private String familyName;
    private int profit;
    private int lookTimes;
    private int virtualNums;
    private String shareTimes;
    private String startTime;
    private int liveTime;
    private int fansNums;
    private String liveTimes;
    private String endTime;
    private String deviceInfo;
    private String shareUrl;
    private int sex;
    private String headImg;
    private List<GiftDto> receiveGite;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getLookTimes() {
        return lookTimes;
    }

    public void setLookTimes(int lookTimes) {
        this.lookTimes = lookTimes;
    }

    public int getVirtualNums() {
        return virtualNums;
    }

    public void setVirtualNums(int virtualNums) {
        this.virtualNums = virtualNums;
    }

    public String getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(String shareTimes) {
        this.shareTimes = shareTimes;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    public int getFansNums() {
        return fansNums;
    }

    public void setFansNums(int fansNums) {
        this.fansNums = fansNums;
    }

    public String getLiveTimes() {
        return liveTimes;
    }

    public void setLiveTimes(String liveTimes) {
        this.liveTimes = liveTimes;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }



    public List<GiftDto> getReceiveGite() {
        return receiveGite;
    }

    public void setReceiveGite(List<GiftDto> receiveGite) {
        this.receiveGite = receiveGite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.channelId);
        dest.writeString(this.title);
        dest.writeString(this.thumb);
        dest.writeString(this.nickname);
        dest.writeString(this.familyName);
        dest.writeInt(this.profit);
        dest.writeInt(this.lookTimes);
        dest.writeInt(this.virtualNums);
        dest.writeString(this.shareTimes);
        dest.writeString(this.startTime);
        dest.writeInt(this.liveTime);
        dest.writeInt(this.fansNums);
        dest.writeString(this.liveTimes);
        dest.writeString(this.endTime);
        dest.writeString(this.deviceInfo);
        dest.writeString(this.shareUrl);
        dest.writeInt(this.sex);
        dest.writeString(this.headImg);
        dest.writeTypedList(this.receiveGite);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
    }

    public CloseLiveDto() {
    }

    protected CloseLiveDto(Parcel in) {
        this.id = in.readInt();
        this.channelId = in.readInt();
        this.title = in.readString();
        this.thumb = in.readString();
        this.nickname = in.readString();
        this.familyName = in.readString();
        this.profit = in.readInt();
        this.lookTimes = in.readInt();
        this.virtualNums = in.readInt();
        this.shareTimes = in.readString();
        this.startTime = in.readString();
        this.liveTime = in.readInt();
        this.fansNums = in.readInt();
        this.liveTimes = in.readString();
        this.endTime = in.readString();
        this.deviceInfo = in.readString();
        this.shareUrl = in.readString();
        this.sex = in.readInt();
        this.headImg = in.readString();
        this.receiveGite = in.createTypedArrayList(GiftDto.CREATOR);
        this.createTime = in.readString();
        this.updateTime = in.readString();
    }

    public static final Creator<CloseLiveDto> CREATOR = new Creator<CloseLiveDto>() {
        @Override
        public CloseLiveDto createFromParcel(Parcel source) {
            return new CloseLiveDto(source);
        }

        @Override
        public CloseLiveDto[] newArray(int size) {
            return new CloseLiveDto[size];
        }
    };
}
