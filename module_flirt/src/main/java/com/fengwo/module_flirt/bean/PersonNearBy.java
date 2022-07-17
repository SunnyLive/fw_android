package com.fengwo.module_flirt.bean;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/24 15:08
 */
public class PersonNearBy {

    /**
     * age : string
     * bstatus : string  //
     * distance : string
     * fwId : 0
     * headImg : string
     * imgList : [{}]
     * latitude : string
     * longitude : string
     * nickname : string
     * sex : string
     * signature : string
     */

    private String age;
    private String bstatus;  // 0 未开播 1 开播
    private String distance;
    private int fwId;
    private String headImg;
    private String latitude;
    private String longitude;
    private String nickname;
    private String sex; //0：保密，1：男；2：女',
    private String signature;
    private List<ImgListBean> imgList;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBstatus() {
        return bstatus;
    }

    public void setBstatus(String bstatus) {
        this.bstatus = bstatus;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getFwId() {
        return fwId;
    }

    public void setFwId(int fwId) {
        this.fwId = fwId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }

    public static class ImgListBean {
        private String cardId;
        private String high;
        private String imageUrl;
        private String type;
        private String width;

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }
}
