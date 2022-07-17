package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.bean.FestivalDto;
import com.fengwo.module_comment.bean.UserMedalBean;

import java.util.ArrayList;
import java.util.List;

public class EnterLivingRoomDto {
    public int channelId;
    public int lookNums;
    public int consumNums;
    public String headImg;
    public String nickname;
    public int pkStatus;//大于0 就是在pk
    public int roomManage;//是否是房管,0不是，1是
    public Integer calorific;//热度值
    public int isAttention;//0 未关注  1关注
    public int isShutUp;//0 未禁言  1禁言
    public long remainTime;//禁言截止时间点（时间戳格式）
    public int isGuard;
    public List<String> notices;
    public isAnchorGuardVO isAnchorGuardVO;
    public ColorInfo colorInfo;
    public String pullUrlFLV;
    public String hostLevel;
    public int type;//2 pc 开播

    private ActivityCpInfoBean activityCpInfo;

    public ActivityCpInfoBean getActivityCpInfoBean() {
        return activityCpInfo;
    }

    public void setActivityCpInfoBean(ActivityCpInfoBean activityCpInfoBean) {
        this.activityCpInfo = activityCpInfoBean;
    }

    public MatchTeamResult channelPkInfo;

    public EnterLivingRoomPkActivityDto pkActivityDto;//pk活动的信息

    public EnterLivingRoomActDto activityDto;//普通活动的信息

    public UserMedalBean userMedalBean;

    public List<AnchorWishBean> wishInfo;

    public NewActivityDto newActivityDto;


    public List<Integer> medalId = new ArrayList<>();

    /**
     * 是否禁言
     *
     * @return
     */
    public boolean isBanned(long forbiddenWords) {
        long realBannedTime = remainTime >= forbiddenWords ? remainTime : forbiddenWords;
        if (isShutUp == 0 && realBannedTime <= 0) {
            return false;
        } else {
            return realBannedTime > System.currentTimeMillis() ? true : false;
        }
    }

    public static class ColorInfo {

        /**
         * user_color : #ffffff
         * system_message : #cd5c5c
         * bullet_screen : #ff34b3
         * channel_nickname_color : #00bfff
         * nickname_bg_color : #327acf
         * nickname_color : #afeeee
         * bullet_nickname_screen : #ffc125
         * channel_bg_color : #327acf
         */

        private String user_color;
        private String system_message;
        private String bullet_screen;
        private String channel_nickname_color;
        private String nickname_bg_color;
        private String nickname_color;
        private String bullet_nickname_screen;
        private String channel_bg_color;
        private String system_notice_color;

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
    }

    public static class isAnchorGuardVO {
        public String authBgColor;
        public long authChatNumber;
        public int authConnect;
        public String authContentColor;
        public int authInvisible;
        public String authNicenameColor;
        public int authPrevent;
        public int authShotOut;
        public int authShutUp;
        public String guardDeadline;
        public String guardIcon;
        public long guardId;
        public int isGuard;
        public int level;
        public Privileges privileges;
    }

    public static class Privileges {
        public String motoringName;
        public String motoringSwf;
        public int motoringSwfRate;
    }
}
