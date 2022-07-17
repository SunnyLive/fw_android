package com.fengwo.module_login.mvp.dto;

import android.text.TextUtils;

import com.fengwo.module_comment.Interfaces.ICardType;

import java.io.Serializable;
import java.util.List;

public class MineCardDetailDto {

    /**
     * age : 0
     * cardStatus : 0
     * circleId : 0
     * circleName : string
     * comments : 0
     * cover : [{"high":0,"imageUrl":"string","typeNew":0,"videoImgUrl":"string","width":0}]
     * createTime : 2020-10-15T15:23:58.152Z
     * distance : 0
     * excerpt : string
     * formType : 0
     * headImg : string
     * id : 0
     * isAttention : 0
     * isBannedComment : true
     * isLike : 0
     * likes : 0
     * liveStatus : 0
     * myIsCard : 0
     * nickname : string
     * position : string
     * remark : string
     * shares : 0
     * time : string
     * type : 0
     * userId : 0
     * wenboLiveStatus : 0
     */

    private int age;
    private int cardStatus;
    private int circleId;
    private String circleName;
    private int comments;
    private String createTime;
    private String topTime;
    private int distance;
    private String excerpt;
    private int formType;
    private String headImg;
    private int id;
    private int isAttention;
    private boolean isBannedComment;
    private int isLike;
    private int likes;
    private int liveStatus;
    private int myIsCard;
    private String nickname;
    private String position;
    private String remark;
    private int shares;
    private String time;
    private int type;
    private int userId;
    private int wenboLiveStatus;
    private List<CoverBean> cover;

    public String getTopTime() {
        return topTime;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
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

    public boolean isIsBannedComment() {
        return isBannedComment;
    }

    public void setIsBannedComment(boolean isBannedComment) {
        this.isBannedComment = isBannedComment;
    }

    public boolean getIsLike() {
        return  isLike == 1;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getMyIsCard() {
        return myIsCard;
    }

    public void setMyIsCard(int myIsCard) {
        this.myIsCard = myIsCard;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWenboLiveStatus() {
        return wenboLiveStatus;
    }

    public void setWenboLiveStatus(int wenboLiveStatus) {
        this.wenboLiveStatus = wenboLiveStatus;
    }

    public List<CoverBean> getCover() {
        return cover;
    }

    public void setCover(List<CoverBean> cover) {
        this.cover = cover;
    }

    public static class CoverBean implements Serializable, ICardType {
        /**
         * high : 0
         * imageUrl : string
         * typeNew : 0
         * videoImgUrl : string
         * width : 0
         */

        private int high;
        private String imageUrl;
        private int typeNew;
        private String videoImgUrl;
        private int width;

        public int getHigh() {
            return high;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public int getSourceType() {
            return getImageUrl().endsWith(".mp4") ? 2 : 1;
        }

        @Override
        public String getPoster() {
            return videoImgUrl;
        }

        @Override
        public String getUrl() {
            return imageUrl;
        }
    }
}
