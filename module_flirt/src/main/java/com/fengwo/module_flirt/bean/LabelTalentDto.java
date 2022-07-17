package com.fengwo.module_flirt.bean;

public class LabelTalentDto {

    /**
     * id : 23
     * anchorId : 723611
     * roomTitle : 点击领取小可爱
     * canRefuse : 1
     * bannedEndTime : {"epochSecond":1598889599,"nano":0}
     * tagName : null
     * audioPath : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/audios/anchor/1594113802000*anchor3659089005.mp3
     * occuName : 学生
     * charm : 826.0
     * duration : 839237
     * nickname : 妮妮在呢
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/723611/images_1594973387_KLOB3CPJrL.jpeg
     * sex : 2
     * age : 20
     * userLevel : 1
     * createTime : 1594109735000
     * bstatus : 1
     * btimes : 126
     */

    private int id;
    private int anchorId;
    private String roomTitle;
    private int canRefuse;
    private BannedEndTimeBean bannedEndTime;
    private Object tagName;
    private String audioPath;
    private String occuName;
    private double charm;
    private int duration;
    private String nickname;
    private String headImg;
    private int sex;
    private int age;
    private String userLevel;
    private long createTime;
    private int bstatus;
    private int btimes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public int getCanRefuse() {
        return canRefuse;
    }

    public void setCanRefuse(int canRefuse) {
        this.canRefuse = canRefuse;
    }

    public BannedEndTimeBean getBannedEndTime() {
        return bannedEndTime;
    }

    public void setBannedEndTime(BannedEndTimeBean bannedEndTime) {
        this.bannedEndTime = bannedEndTime;
    }

    public Object getTagName() {
        return tagName;
    }

    public void setTagName(Object tagName) {
        this.tagName = tagName;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getOccuName() {
        return occuName;
    }

    public void setOccuName(String occuName) {
        this.occuName = occuName;
    }

    public double getCharm() {
        return charm;
    }

    public void setCharm(double charm) {
        this.charm = charm;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBstatus() {
        return bstatus;
    }

    public void setBstatus(int bstatus) {
        this.bstatus = bstatus;
    }

    public int getBtimes() {
        return btimes;
    }

    public void setBtimes(int btimes) {
        this.btimes = btimes;
    }

    public static class BannedEndTimeBean {
        /**
         * epochSecond : 1598889599
         * nano : 0
         */

        private long epochSecond;
        private int nano;

        public long getEpochSecond() {
            return epochSecond;
        }

        public void setEpochSecond(long epochSecond) {
            this.epochSecond = epochSecond;
        }

        public int getNano() {
            return nano;
        }

        public void setNano(int nano) {
            this.nano = nano;
        }
    }
}
