package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/4
 */
public class StartLivePushDto implements Parcelable {

    /**
     * roomId : fengwo1091
     * channelId : 1091
     * title : 好
     * thumb : http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/header1574675687553.jpg
     * headImg : null
     * nickname : 用户1091
     * createTime : 2019-12-04T04:26:33.255Z
     * beforeProfit : 1775
     * currentProfit : 0
     * lookNums : 0
     * virtualNums : 47
     * pushUrl : rtmp://push.yinkehuyu.cn/test/1091?txSecret=d7549c5db9bd0f81b9630cb1802eff28&txTime=5DF06FF9
     * longitude : null
     * latitude : null
     * pullUrlRTMP : rtmp://play.yinkehuyu.cn/test/1091
     * deviceInfo : null
     * cityName : null
     * pullUrlFLV : http://play.yinkehuyu.cn/test/1091.flv
     * pullUrlHLS : http://play.yinkehuyu.cn/test/1091.m3u8
     * notices : ["欢迎进入蜂窝互娱","蜂窝倡导绿色直播环境，任何违法违规、低俗不良行为将被封禁。传播正能量，从你我做起。"]
     * times : 21
     * duration : 12525
     * pkStatus : 0
     * enablePk : 1
     * enableInteract : 1
     * menuIds : 27,28
     * menus : ["颜值","二次元"]
     * colorInfo : {"user_color":"#ffffff","system_message":"#98CC31","bullet_screen":"#ff34b3","channel_nickname_color":"#00bfff","nickname_bg_color":"","nickname_color":"#afeeee","bullet_nickname_screen":"#ffc125","channel_bg_color":"#327acf"}
     */

    private String roomId;
    private int channelId;
    private String title;
    private String thumb;
    private String headImg;
    private String nickname;
    private String createTime;
    private int beforeProfit;
    private int currentProfit;
    private int lookNums;
    private int virtualNums;
    private String pushUrl;
    private double longitude;
    private double latitude;
    private String pullUrlRTMP;
    private String deviceInfo;
    private String cityName;
    private String pullUrlFLV;
    private String pullUrlHLS;
    private int times;
    private int duration;
    private int pkStatus;
    private int enablePk;
    private int enableInteract;
    private String menuIds;

    private ColorInfoBean colorInfo;
    private List<String> notices;
    private List<String> menus;
    private MatchTeamResult channelPkInfo;
    private ActivityCpInfoBean activityCpInfo;

    public ActivityCpInfoBean getActivityCpInfoBean() {
        return activityCpInfo;
    }

    public void setActivityCpInfoBean(ActivityCpInfoBean activityCpInfoBean) {
        this.activityCpInfo = activityCpInfoBean;
    }

    public MatchTeamResult getChannelPkInfo() {
        return channelPkInfo;
    }

    public void setChannelPkInfo(MatchTeamResult channelPkInfo) {
        this.channelPkInfo = channelPkInfo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBeforeProfit() {
        return beforeProfit;
    }

    public void setBeforeProfit(int beforeProfit) {
        this.beforeProfit = beforeProfit;
    }

    public int getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(int currentProfit) {
        this.currentProfit = currentProfit;
    }

    public int getLookNums() {
        return lookNums;
    }

    public void setLookNums(int lookNums) {
        this.lookNums = lookNums;
    }

    public int getVirtualNums() {
        return virtualNums;
    }

    public void setVirtualNums(int virtualNums) {
        this.virtualNums = virtualNums;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }


    public String getPullUrlRTMP() {
        return pullUrlRTMP;
    }

    public void setPullUrlRTMP(String pullUrlRTMP) {
        this.pullUrlRTMP = pullUrlRTMP;
    }


    public String getPullUrlFLV() {
        return pullUrlFLV;
    }

    public void setPullUrlFLV(String pullUrlFLV) {
        this.pullUrlFLV = pullUrlFLV;
    }

    public String getPullUrlHLS() {
        return pullUrlHLS;
    }

    public void setPullUrlHLS(String pullUrlHLS) {
        this.pullUrlHLS = pullUrlHLS;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPkStatus() {
        return pkStatus;
    }

    public void setPkStatus(int pkStatus) {
        this.pkStatus = pkStatus;
    }

    public int getEnablePk() {
        return enablePk;
    }

    public void setEnablePk(int enablePk) {
        this.enablePk = enablePk;
    }

    public int getEnableInteract() {
        return enableInteract;
    }

    public void setEnableInteract(int enableInteract) {
        this.enableInteract = enableInteract;
    }

    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    public ColorInfoBean getColorInfo() {
        return colorInfo;
    }

    public void setColorInfo(ColorInfoBean colorInfo) {
        this.colorInfo = colorInfo;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public List<String> getMenus() {
        return menus;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }


    public static class ColorInfoBean implements Parcelable {
        /**
         * user_color : #ffffff
         * system_message : #98CC31
         * bullet_screen : #ff34b3
         * channel_nickname_color : #00bfff
         * nickname_bg_color :
         * nickname_color : #afeeee
         * bullet_nickname_screen : #ffc125
         * channel_bg_color : #327acf
         */

        private String user_color;
        private String system_message;
        private String system_notice_color;
        private String bullet_screen;
        private String channel_nickname_color;
        private String nickname_bg_color;
        private String nickname_color;
        private String bullet_nickname_screen;
        private String channel_bg_color;

        public String getSystem_notice_color() {
            return system_notice_color;
        }

        public void setSystem_notice_color(String system_notice_color) {
            this.system_notice_color = system_notice_color;
        }

        public String getUser_color() {
            return user_color;
        }

        public void setUser_color(String user_color) {
            this.user_color = user_color;
        }

        public String getSystem_message() {
            return system_message;
        }

        public void setSystem_message(String system_message) {
            this.system_message = system_message;
        }

        public String getBullet_screen() {
            return bullet_screen;
        }

        public void setBullet_screen(String bullet_screen) {
            this.bullet_screen = bullet_screen;
        }

        public String getChannel_nickname_color() {
            return channel_nickname_color;
        }

        public void setChannel_nickname_color(String channel_nickname_color) {
            this.channel_nickname_color = channel_nickname_color;
        }

        public String getNickname_bg_color() {
            return nickname_bg_color;
        }

        public void setNickname_bg_color(String nickname_bg_color) {
            this.nickname_bg_color = nickname_bg_color;
        }

        public String getNickname_color() {
            return nickname_color;
        }

        public void setNickname_color(String nickname_color) {
            this.nickname_color = nickname_color;
        }

        public String getBullet_nickname_screen() {
            return bullet_nickname_screen;
        }

        public void setBullet_nickname_screen(String bullet_nickname_screen) {
            this.bullet_nickname_screen = bullet_nickname_screen;
        }

        public String getChannel_bg_color() {
            return channel_bg_color;
        }

        public void setChannel_bg_color(String channel_bg_color) {
            this.channel_bg_color = channel_bg_color;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.user_color);
            dest.writeString(this.system_message);
            dest.writeString(this.system_notice_color);
            dest.writeString(this.bullet_screen);
            dest.writeString(this.channel_nickname_color);
            dest.writeString(this.nickname_bg_color);
            dest.writeString(this.nickname_color);
            dest.writeString(this.bullet_nickname_screen);
            dest.writeString(this.channel_bg_color);
        }

        public ColorInfoBean() {
        }

        protected ColorInfoBean(Parcel in) {
            this.user_color = in.readString();
            this.system_message = in.readString();
            this.system_notice_color = in.readString();
            this.bullet_screen = in.readString();
            this.channel_nickname_color = in.readString();
            this.nickname_bg_color = in.readString();
            this.nickname_color = in.readString();
            this.bullet_nickname_screen = in.readString();
            this.channel_bg_color = in.readString();
        }

        public static final Creator<ColorInfoBean> CREATOR = new Creator<ColorInfoBean>() {
            @Override
            public ColorInfoBean createFromParcel(Parcel source) {
                return new ColorInfoBean(source);
            }

            @Override
            public ColorInfoBean[] newArray(int size) {
                return new ColorInfoBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roomId);
        dest.writeInt(this.channelId);
        dest.writeString(this.title);
        dest.writeString(this.thumb);
        dest.writeString(this.headImg);
        dest.writeString(this.nickname);
        dest.writeString(this.createTime);
        dest.writeInt(this.beforeProfit);
        dest.writeInt(this.currentProfit);
        dest.writeInt(this.lookNums);
        dest.writeInt(this.virtualNums);
        dest.writeString(this.pushUrl);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.pullUrlRTMP);
        dest.writeString(this.deviceInfo);
        dest.writeString(this.cityName);
        dest.writeString(this.pullUrlFLV);
        dest.writeString(this.pullUrlHLS);
        dest.writeInt(this.times);
        dest.writeInt(this.duration);
        dest.writeInt(this.pkStatus);
        dest.writeInt(this.enablePk);
        dest.writeInt(this.enableInteract);
        dest.writeString(this.menuIds);
        dest.writeParcelable(this.colorInfo, flags);
        dest.writeParcelable(this.activityCpInfo, flags);
        dest.writeStringList(this.notices);
        dest.writeStringList(this.menus);

    }

    public StartLivePushDto() {
    }

    protected StartLivePushDto(Parcel in) {
        this.roomId = in.readString();
        this.channelId = in.readInt();
        this.title = in.readString();
        this.thumb = in.readString();
        this.headImg = in.readString();
        this.nickname = in.readString();
        this.createTime = in.readString();
        this.beforeProfit = in.readInt();
        this.currentProfit = in.readInt();
        this.lookNums = in.readInt();
        this.virtualNums = in.readInt();
        this.pushUrl = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.pullUrlRTMP = in.readString();
        this.deviceInfo = in.readString();
        this.cityName = in.readString();
        this.pullUrlFLV = in.readString();
        this.pullUrlHLS = in.readString();
        this.times = in.readInt();
        this.duration = in.readInt();
        this.pkStatus = in.readInt();
        this.enablePk = in.readInt();
        this.enableInteract = in.readInt();
        this.menuIds = in.readString();
        this.colorInfo = in.readParcelable(ColorInfoBean.class.getClassLoader());
        this.activityCpInfo = in.readParcelable(ActivityCpInfoBean.class.getClassLoader());
        this.notices = in.createStringArrayList();
        this.menus = in.createStringArrayList();
    }

    public static final Creator<StartLivePushDto> CREATOR = new Creator<StartLivePushDto>() {
        @Override
        public StartLivePushDto createFromParcel(Parcel source) {
            return new StartLivePushDto(source);
        }

        @Override
        public StartLivePushDto[] newArray(int size) {
            return new StartLivePushDto[size];
        }
    };
}
