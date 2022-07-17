package com.fengwo.module_flirt.bean;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/8/12 11:51
 */
public class FindListDto {

    /**
     * age : 0
     * circleId : 0
     * cover : [{"high":0,"imageUrl":"string","type":0,"width":0}]
     * excerpt : string
     * headImg : string
     * id : 0
     * isAttention : 0
     * likes : 0
     * comments:0
     * liveStatus : 0
     * myIsCard : 0
     * nickname : string
     * position : string
     * shares : 0
     * time : string
     * type : 0
     * userId : 0
     * wenboLiveStatus : 0
     */
    private int cardStatus;
    private int age;
    private int circleId;
    private String excerpt;
    private String headImg;
    private int id;
    private int isAttention;
    private int isLike;
    private int likes;
    private int comments;
    private int liveStatus;
    private int myIsCard;
    private String nickname;
    private String position;//定位
    private String distance;//距离
    private int shares;
    private String time;
    private int type;
    private int userId;
    private int wenboLiveStatus;
    private List<CoverDto> cover;
    private boolean isBannedComment;//是否违规

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
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

    public int getLikes() {
        return likes;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
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

    public List<CoverDto> getCover() {
        return cover;
    }

    public void setCover(List<CoverDto> cover) {
        this.cover = cover;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isBannedComment() {
        return isBannedComment;
    }

    public void setBannedComment(boolean bannedComment) {
        isBannedComment = bannedComment;
    }
}
