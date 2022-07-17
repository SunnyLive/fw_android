package com.fengwo.module_flirt.bean;

import com.fengwo.module_comment.Interfaces.ICardType;

import java.io.Serializable;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/9/24
 */
public class FindDetailBean implements Serializable {

    /**
     * id : 3032
     * circleId : 4
     * userId : 500946
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500946/images_1594252429_m0l17USodR.jpeg
     * nickname : 小野马.🐎
     * age : null
     * myIsCard : 1
     * likes : 0
     * shares : 0
     * comments : 0
     * cover : [{"imageUrl":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500946/images_1597719416_Xj0W5IwW9K.jpeg","high":580,"width":326,"type":1,"typeNew":1,"videoImgUrl":null},{"imageUrl":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/500946/mp4_1597719416_hU0wQjqPlf.mp4","high":580,"width":326,"type":0,"typeNew":2,"videoImgUrl":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500946/images_1597719416_Xj0W5IwW9K.jpeg"}]
     * excerpt : 工地里的砖你一块不搬，网络里的毒鸡汤你一碗碗的干！整天郁郁寡欢，爱情与你无关，你就是个铁憨憨！
     * type : 2
     * time : 30天前
     * position : 厦门市
     * liveStatus : 1
     * wenboLiveStatus : 0
     * isAttention : 0
     * isLike : 0
     * cardStatus : 1
     * remark : 1
     * isBannedComment : false
     */

    private int id;
    private int circleId;
    private int userId;
    private String headImg;
    private String nickname;
    private Object age;
    private int myIsCard;
    private int likes;
    private int shares;
    private int comments;
    private String excerpt;
    private int type;
    private String time;
    private String position;
    private String distance;
    private int liveStatus;
    private int wenboLiveStatus;
    private int isAttention;
    private int isLike;
    private int cardStatus;
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

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
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

    public static class CoverBean implements Serializable, ICardType {
        /**
         * imageUrl : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500946/images_1597719416_Xj0W5IwW9K.jpeg
         * high : 580
         * width : 326
         * type : 1
         * typeNew : 1
         * videoImgUrl : null
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

        @Override
        public String getUrl() {
            return imageUrl;
        }

        @Override
        public int getSourceType() {
            return getImageUrl().endsWith(".mp4") ? 2 : 1;
        }

        @Override
        public String getPoster() {
            return videoImgUrl;
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

        public Object getVideoImgUrl() {
            return videoImgUrl;
        }

        public void setVideoImgUrl(String videoImgUrl) {
            this.videoImgUrl = videoImgUrl;
        }
    }
}
