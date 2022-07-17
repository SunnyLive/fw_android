package com.fengwo.module_comment.bean;

import java.io.Serializable;

/**
 * @author Zachary
 * @date 2019/12/25
 */
public class VideoHomeShortModel implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    public int userId;
    public int albumId;
    public int auditStatus;
    public String movieTitle;
    public String videoTitle;
    public String albumName;
    public String cover;
    public String url;
    public String createTime;
    public String updateTime;
    public int duration;
    public int shares;
    public int likes;
    public int favorites;
    public int comments;
    public int views;
    public int newViews;
    public int upViews;
    public String intro;
    public String userName;
    public String userUrl;
    public int isPrivacy;
    public String remark;
    public double latitude;
    public double longitude;
    public String isFavorite;
    public boolean isLike;
    public int status;
    public int gifts;
    public String headImg;
    public String description;
    public String topic;
    public int bgmId;
    public int menuId;
    public int isAttention;

    public boolean isPlay = false;

    @Override
    public String toString() {
        return "VideoHomeShortModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", albumId=" + albumId +
                ", auditStatus=" + auditStatus +
                ", movieTitle='" + movieTitle + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", albumName='" + albumName + '\'' +
                ", cover='" + cover + '\'' +
                ", url='" + url + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", duration=" + duration +
                ", shares=" + shares +
                ", likes=" + likes +
                ", favorites=" + favorites +
                ", comments=" + comments +
                ", views=" + views +
                ", newViews=" + newViews +
                ", upViews=" + upViews +
                ", intro='" + intro + '\'' +
                ", userName='" + userName + '\'' +
                ", userUrl='" + userUrl + '\'' +
                ", isPrivacy=" + isPrivacy +
                ", remark='" + remark + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isFavorite='" + isFavorite + '\'' +
                ", isLike=" + isLike +
                ", status=" + status +
                ", gifts=" + gifts +
                ", headImg='" + headImg + '\'' +
                ", description='" + description + '\'' +
                ", topic='" + topic + '\'' +
                ", bgmId=" + bgmId +
                ", menuId=" + menuId +
                ", isAttention=" + isAttention +
                ", isPlay=" + isPlay +
                '}';
    }
}
