package com.fengwo.module_chat.mvp.model.bean;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/6
 */
public class CardDetailBean {
    /**
     * id : 3908
     * circleId : 0
     * circleName : null
     * userId : 1683186
     * headImg : null
     * nickname : 用户1683186
     * onlineStatus : null
     * age : null
     * myIsCard : 0
     * likes : 0
     * shares : 0
     * comments : 0
     * cover : [{"imageUrl":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1602858045642717.jpeg","high":2280,"width":1080,"type":0,"typeNew":0,"videoImgUrl":""}]
     * excerpt : 更何况
     * type : 1
     * createTime : 2020-10-20T10:43:37Z
     * time : 0秒前
     * position : 
     * liveStatus : 1
     * wenboLiveStatus : 0
     * isAttention : 0
     * isLike : 0
     * distance : 1.2456488E7
     * cardStatus : 0
     * powerStatus : 0
     * topTime : null
     * formType : null
     * remark : 
     * isBannedComment : false
     */
    private String tagIds;

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    private int id;
    private int circleId;
    private String circleName;
    private int userId;
    private String headImg;
    private String nickname;
    private String onlineStatus;
    private String age;
    private int myIsCard;
    private int likes;
    private int shares;
    private int comments;
    private String excerpt;
    private int type;
    private String createTime;
    private String time;
    private String position;
    private int liveStatus;
    private int wenboLiveStatus;
    private int isAttention;
    private int isLike;
    private String distance;
    private int cardStatus;
    private int powerStatus;
    private String topTime;
    private String formType;
    private String remark;
    private boolean isBannedComment;
    private List<CoverBean> cover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getMyIsCard() {
        return myIsCard;
    }

    public void setMyIsCard(int myIsCard) {
        this.myIsCard = myIsCard;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getWenboLiveStatus() {
        return wenboLiveStatus;
    }

    public void setWenboLiveStatus(int wenboLiveStatus) {
        this.wenboLiveStatus = wenboLiveStatus;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public int getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(int powerStatus) {
        this.powerStatus = powerStatus;
    }

    public String getTopTime() {
        return topTime;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isIsBannedComment() {
        return isBannedComment;
    }

    public void setIsBannedComment(boolean isBannedComment) {
        this.isBannedComment = isBannedComment;
    }

    public List<CoverBean> getCover() {
        return cover;
    }

    public void setCover(List<CoverBean> cover) {
        this.cover = cover;
    }

    public static class CoverBean {
        /**
         * imageUrl : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1602858045642717.jpeg
         * high : 2280
         * width : 1080
         * type : 0
         * typeNew : 0
         * videoImgUrl : 
         */

        private String imageUrl;
        private int high;
        private int width;
        private int type;
        private int typeNew;
        private String videoImgUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getHigh() {
            return high;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTypeNew() {
            return typeNew;
        }

        public void setTypeNew(int typeNew) {
            this.typeNew = typeNew;
        }

        public String getVideoImgUrl() {
            return videoImgUrl;
        }

        public void setVideoImgUrl(String videoImgUrl) {
            this.videoImgUrl = videoImgUrl;
        }
    }


    /**
     * id : 3293
     * circleId : 4
     * circleName : 夜生活
     * tagIds :
     * cover : [{"imageUrl":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/VID20201016213024.mp4","high":1080,"width":1920,"type":0,"typeNew":2,"videoImgUrl":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/1602928783118.jpg"}]
     * excerpt : dfgg
     * longitude : 113.953398
     * latitude : 22.533961
     * position : 深圳市
     * type : 2
     * isDraft : 1
     * formType : 1
     *//*

    private int id;
    private int circleId;
    private String circleName;
    private String tagIds;
    private String excerpt;
    private String longitude;
    private String latitude;
    private String position;
    private int type;
    private int isDraft;
    private int formType;
    private List<CoverBean> cover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(int isDraft) {
        this.isDraft = isDraft;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    public List<CoverBean> getCover() {
        return cover;
    }

    public void setCover(List<CoverBean> cover) {
        this.cover = cover;
    }

    public static class CoverBean {
        *//**
         * imageUrl : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/VID20201016213024.mp4
         * high : 1080
         * width : 1920
         * type : 0
         * typeNew : 2
         * videoImgUrl : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/1602928783118.jpg
         *//*

        private String imageUrl;
        private int high;
        private int width;
        private int type;
        private int typeNew;
        private String videoImgUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getHigh() {
            return high;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTypeNew() {
            return typeNew;
        }

        public void setTypeNew(int typeNew) {
            this.typeNew = typeNew;
        }

        public String getVideoImgUrl() {
            return videoImgUrl;
        }

        public void setVideoImgUrl(String videoImgUrl) {
            this.videoImgUrl = videoImgUrl;
        }
    }*/
    
    
    
    
    
    
    
    
}
