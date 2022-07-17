package com.fengwo.module_vedio.eventbus;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/26
 */
public class PlayShortVideoEvent {
    public String url;
    public int movieId;
    public String movieTitle;
    public String movieDes;
    public int comments;
    public int userId;

    public PlayShortVideoEvent(String url, int movieId) {
        this.url = url;
        this.movieId = movieId;
    }

    public PlayShortVideoEvent(String url, int movieId, String movieTitle, String movieDes,int comments,int userId) {
        this.url = url;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieDes = movieDes;
        this.comments = comments;
        this.userId = userId;
    }
}
