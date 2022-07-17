package com.fengwo.module_live_vedio.mvp.dto;

import java.io.Serializable;

public class ShareInfoDto implements Serializable {

    /**
     * icon :
     * link : http://app.fengwohuyu.com/register?id=357
     * title : 蜂窝互娱,我的网红生活
     * decribe : 蜂窝互娱，分享我的网红生活
     */

    private String icon;
    private String link;
    private String title;
    private String decribe;
    private String poster;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecribe() {
        return decribe;
    }

    public void setDecribe(String decribe) {
        this.decribe = decribe;
    }
}
