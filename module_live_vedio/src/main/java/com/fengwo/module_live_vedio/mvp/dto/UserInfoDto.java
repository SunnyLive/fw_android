package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/24
 */
public class UserInfoDto {

    /**
     * account : string
     * attention : 0
     * fans : 0
     * fwId : string
     * headImg : string
     * id : 0
     * isAttention : 0
     * likeVideoCount : 0
     * liveLabel : string
     * liveLevel : 0
     * liveLevelExperience : 0
     * liveLevelHighest : 0
     * liveLevelLowest : 0
     * location : string
     * mobile : string
     * movieCount : 0
     * nickname : string
     * sex : 0
     * signature : string
     * videoCount : 0
     * userVipLevel : 0
     */

    private String account;
    private int attention;
    private int fans;
    private String fwId;
    private String headImg;
    private int id;
    private int isAttention;
    private int likeVideoCount;
    private String liveLabel;
    private int liveLevel;
    private int liveLevelExperience;
    private int liveLevelHighest;
    private int liveLevelLowest;
    private String location;
    private String mobile;
    private int movieCount;
    private String nickname;
    private int sex;
    private String signature;
    private int videoCount;
    private int vipLevel;
    private long sendGiftTotal;
    private long receive;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getFwId() {
        return fwId;
    }

    public void setFwId(String fwId) {
        this.fwId = fwId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getLikeVideoCount() {
        return likeVideoCount;
    }

    public void setLikeVideoCount(int likeVideoCount) {
        this.likeVideoCount = likeVideoCount;
    }

    public String getLiveLabel() {
        return liveLabel;
    }

    public void setLiveLabel(String liveLabel) {
        this.liveLabel = liveLabel;
    }

    public int getLiveLevel() {
        if (liveLevel < 1) {
            liveLevel = 1;
        }
        return liveLevel;
    }

    public void setLiveLevel(int liveLevel) {
        this.liveLevel = liveLevel;
    }

    public int getLiveLevelExperience() {
        return liveLevelExperience;
    }

    public void setLiveLevelExperience(int liveLevelExperience) {
        this.liveLevelExperience = liveLevelExperience;
    }

    public int getLiveLevelHighest() {
        return liveLevelHighest;
    }

    public void setLiveLevelHighest(int liveLevelHighest) {
        this.liveLevelHighest = liveLevelHighest;
    }

    public int getLiveLevelLowest() {
        return liveLevelLowest;
    }

    public void setLiveLevelLowest(int liveLevelLowest) {
        this.liveLevelLowest = liveLevelLowest;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getVipLevel() {
        if (vipLevel < 1) {
            vipLevel = 1;
        }
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public long getSendGiftTotal() {
        return sendGiftTotal;
    }

    public void setSendGiftTotal(long sendGiftTotal) {
        this.sendGiftTotal = sendGiftTotal;
    }

    public long getReceive() {
        return receive;
    }

    public void setReceive(long receive) {
        this.receive = receive;
    }
}
