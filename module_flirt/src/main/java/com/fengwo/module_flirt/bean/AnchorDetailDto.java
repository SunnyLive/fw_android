package com.fengwo.module_flirt.bean;

import java.util.List;

public class AnchorDetailDto {

    /**
     * age : string
     * anchorId : 0
     * audioPath : string
     * bstatus : 0
     * charm : 0
     * endLiveTime : 2020-04-24T07:36:53.610Z
     * headImg : string
     * id : 0
     * nickname : string
     * occuName : string
     * res : [{"anchorId":0,"id":0,"rPath":"string","rType":0}]
     * sex : 0
     * signature : string
     * tagName : string
     */

    private String age;
    private int anchorId;
    private String audioPath;
    private int bstatus;
    private int charm;
    private long endLiveTime;
    private String headImg;
    private int id;
    private String nickname;
    private String occuName;
    private int sex;
    private String signature;
    private String tagName;
    private List<ResBean> res;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getBstatus() {
        return bstatus;
    }

    public void setBstatus(int bstatus) {
        this.bstatus = bstatus;
    }

    public int getCharm() {
        return charm;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

    public long getEndLiveTime() {
        return endLiveTime;
    }

    public void setEndLiveTime(long endLiveTime) {
        this.endLiveTime = endLiveTime;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOccuName() {
        return occuName;
    }

    public void setOccuName(String occuName) {
        this.occuName = occuName;
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<ResBean> getRes() {
        return res;
    }

    public void setRes(List<ResBean> res) {
        this.res = res;
    }

    public static class ResBean {
        /**
         * anchorId : 0
         * id : 0
         * rPath : string
         * rType : 0
         */

        private int anchorId;
        private int id;
        private String rPath;
        private int rType;

        public int getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(int anchorId) {
            this.anchorId = anchorId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRPath() {
            return rPath;
        }

        public void setRPath(String rPath) {
            this.rPath = rPath;
        }

        public int getRType() {
            return rType;
        }

        public void setRType(int rType) {
            this.rType = rType;
        }
    }
}
