package com.fengwo.module_vedio.mvp.dto;


import android.text.TextUtils;

public class RecordsDto {

    /**
     * albumId : 0
     * auditStatus : 0
     * comments : 0
     * cover : string
     * createTime : 2019-12-27T03:50:02.061Z
     * duration : 0
     * favorites : 0
     * gifts : 0
     * headImg : string
     * id : 0
     * intro : string
     * isLike : true
     * isPrivacy : 0
     * latitude : 0
     * likes : 0
     * longitude : 0
     * movieTitle : string
     * newViews : 0
     * remark : string
     * shares : 0
     * status : 0
     * upViews : 0
     * updateTime : 2019-12-27T03:50:02.061Z
     * url : string
     * userId : 0
     * userName : string
     * views : 0
     */

    private String albumId;
    private String cover;
    private int duration;
    private String movieTitle;
    private String videoTitle;

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    private String userName;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private String updateTime;

}