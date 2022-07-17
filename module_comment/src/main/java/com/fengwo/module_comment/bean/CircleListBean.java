package com.fengwo.module_comment.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class CircleListBean implements Serializable , MultiItemEntity {
    private static final long serialVersionUID = 1L;
    public String cover;
    public String excerpt;
    public String headImg;
    public String id;
    public String nickname;
    public String position;
    public String userId;
    public int likes;
    public int comments;  //评论
    public int isAd;
    public int cardStatus = -1;//状态：0审核中，1成功，2私密，3封禁，4草稿，5拒审
    public String adImage;
    public String adContentUrl;
    public String status;
    public int isLike;  //是否点赞   1：是 0：否
    public String roomId;
    public String topTime;
    public int powerStatus;// 0 所有人可见  1 仅自己可见  2 仅好友可见
    public String createTime;  //创建时间

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public String toString() {
        return "CircleListBean{" +
                "cover='" + cover + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", headImg='" + headImg + '\'' +
                ", id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", position='" + position + '\'' +
                ", userId='" + userId + '\'' +
                ", likes=" + likes +
                ", isAd=" + isAd +
                ", cardStatus=" + cardStatus +
                ", adImage='" + adImage + '\'' +
                ", adContentUrl='" + adContentUrl + '\'' +
                ", status='" + status + '\'' +
                ", roomId='" + roomId + '\'' +
                ", topTime='" + topTime + '\'' +
                '}';
    }
}
