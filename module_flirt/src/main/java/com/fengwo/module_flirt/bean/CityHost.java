package com.fengwo.module_flirt.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fengwo.module_comment.Interfaces.ICardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityHost implements Serializable,MultiItemEntity {

    private static final long serialVersionUID = 1132513221312L;

    /**
     * id : 502209
     * anchorId : 502209  xxxx
     * tagName : 冷面杀手  xxx
     * audioPath : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/audios/513028/audios_1588056853_Cjpy5Wb4ZF.amr
     * audioLength : null
     * occuName : 11
     * charm : 33434
     * nickname : 用户502209  xxxxx
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/502209/images_1588833166_Ya49rv7xD0.jpeg   xxxx
     * sex : 1   xxx
     * location : 厦门  ----
     * age : 28       xxxx
     * signature : 这个人很懒，什么都没留下~
     * endLiveTime : 1586946685000
     * res : [{"id":118,"anchorId":502209,"coverImg":null,"rPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/10041/images_1578299291_hGyMqoAAPA.jpeg","rType":1},{"id":119,"anchorId":502209,"coverImg":null,"rPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/10041/images_1578305757_HDVTTbCFum.jpeg","rType":1},{"id":121,"anchorId":502209,"coverImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/513028/images_1588134427_eP2HXX1oLl.jpeg","rPath":"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","rType":2}]
     * roomTitle : null   xxxxx
     * fwId : 502209
     * vipLevel : 2
     * bstatus : 0  xxxxx
     */
    private List<AdvertListBean> advertList;
    private String cover;
    private int type;
    private int id;
    private int anchorId;
    private String tagName;
    private String audioPath;
    private long audioLength;
    private String occuName;
    private double charm;
    private String nickname;
    private String headImg;
    private int sex;
    private String location;
    private int age;
    private String signature;
    private double distance;
    private long endLiveTime;
    private String roomTitle;
    private String lastTime;
    private long fwId;
    private int vipLevel;
    private int userLevel;
    private int bstatus;    //文博直播状态  1开播 0/1未开播
    private int liveStatus; //秀场直播状态  2开播 0/1未开播

    private ArrayList<ResBean> res;
    private ArrayList<CoverDto> imgList;

    public int getId() {
        return id;
    }
    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void setId(int id) {
        this.id = id;
    }
    public long getLiveStatus() {
        return liveStatus;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public long getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(long audioLength) {
        this.audioLength = audioLength;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<AdvertListBean> getAdvertList() {
        return advertList;
    }

    public void setAdvertList(List<AdvertListBean> advertList) {
        this.advertList = advertList;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setFwId(long fwId) {
        this.fwId = fwId;
    }

    public String getDistance() {
        if (distance<0){
            return "未知位置";
        }else if (distance<100){
            return "<100m";
        }else if (distance<1000){
            return (int)distance+"m";
        }else {
            return   String.format("%.1f",distance / 1000) + "km";
        }
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getEndLiveTime() {
        return endLiveTime;
    }

    public void setEndLiveTime(long endLiveTime) {
        this.endLiveTime = endLiveTime;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public long getFwId() {
        return fwId;
    }

    public void setFwId(int fwId) {
        this.fwId = fwId;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getBstatus() {
        return bstatus;
    }

    public void setBstatus(int bstatus) {
        this.bstatus = bstatus;
    }

    public ArrayList<ResBean> getRes() {
        return res;
    }

    public void setRes(ArrayList<ResBean> res) {
        this.res = res;
    }

    public List<CoverDto> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<CoverDto> imgList) {
        this.imgList = imgList;
    }

    @Override
    public int getItemType() {
        return advertList == null || advertList.isEmpty() ? 1:0;
    }

    public static class ResBean implements Serializable, ICardType {
        /**
         * id : 118
         * anchorId : 502209
         * coverImg : null
         * rPath : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/10041/images_1578299291_hGyMqoAAPA.jpeg
         * rType : 1
         */

        private int id;
        private int anchorId;
        private String coverImg;
        private String rPath;
        private int rType;

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

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
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

        @Override
        public int getSourceType() {
            return getRPath().endsWith(".mp4")?2:1;
        }

        @Override
        public String getPoster() {
            return null;
        }

        @Override
        public String getUrl() {
            return getRPath();
        }
    }
    public static class AdvertListBean implements Serializable{
        /**
         * advertName : string
         * contentUrl : string
         * descrip : string
         * high : 0
         * id : 0
         * image : string
         * title : string
         * width : 0
         */

        private String advertName;
        private String contentUrl;
        private String descrip;
        private int high;
        private int id;
        private String image;
        private String title;
        private int width;

        public String getAdvertName() {
            return advertName;
        }

        public void setAdvertName(String advertName) {
            this.advertName = advertName;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getDescrip() {
            return descrip;
        }

        public void setDescrip(String descrip) {
            this.descrip = descrip;
        }

        public int getHigh() {
            return high;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
