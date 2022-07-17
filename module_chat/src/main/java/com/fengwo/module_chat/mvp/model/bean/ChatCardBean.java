package com.fengwo.module_chat.mvp.model.bean;

import java.util.List;

public class ChatCardBean {
    @Override
    public String toString() {
        return "ChatCardBean{" +
                "circleId='" + circleId + '\'' +
                ", circleName='" + circleName + '\'' +
                ", comments='" + comments + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", favorites=" + favorites +
                ", groupId='" + groupId + '\'' +
                ", headImg='" + headImg + '\'' +
                ", id='" + id + '\'' +
                ", isAttention='" + isAttention + '\'' +
                ", isLike='" + isLike + '\'' +
                ", latitude='" + latitude + '\'' +
                ", likes='" + likes + '\'' +
                ", longitude='" + longitude + '\'' +
                ", nickname='" + nickname + '\'' +
                ", shares='" + shares + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", views='" + views + '\'' +
                ", imgContent=" + imgContent +
                ", member=" + member +
                '}';
    }

    public String circleId;
    public String circleName;
    public String comments;
    public String excerpt;
    public int favorites;
    public String groupId;
    public String headImg;
    public String id;
    public String isAttention;
    public String isLike;
    public String latitude;
    public String likes;
    public String longitude;
    public String nickname;
    public String shares;
    public String status;
    public String type;
    public String userId;
    public String views;
    public String covers;
    public List<ChatCardImgBean> imgContent;
    public int cardStatus;  //0审核中，1成功，2私密，3封禁，4草稿，5拒审
    private int powerStatus;// 0 所有人可见  1 仅自己可见  2 仅好友可见
    private String topTime;

    public String getTopTime() {
        return topTime;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public int getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(int powerStatus) {
        this.powerStatus = powerStatus;
    }

    // 自定义数据，非接口返回
    public CardMemberModel member;

    public boolean isVedio() {
        return type.equals("2");
    }
}
