package com.fengwo.module_vedio.mvp.dto;

import java.io.Serializable;
import java.util.List;

public class SmallVedioListDto {
    public int current;
    public int pages;
//    public Order orders;
    public List<Record> records;
    public boolean searchCount;
    public int size;
    public int total;

    public class Record implements Serializable {
        public String bgmDuration;
        public int bgmId;
        public String bgmName;
        public String bgmUrl;
        public int comments;
        public String cover;
        public String coverId;
        public String createTime;
        public int duration;
        public int favorites;
        public int height;
        public int id;
        public boolean isFavorite;
        public boolean isLike;
        public int isPrivacy;
        public String key;
        public int latitude;
        public int likes;
        public int longitude;
        public int menuId;
        public int shares;
        public int status;
        public String topic;
        public String updateTime;
        public String url;
        public String videoTitle;
        public int userId;
        public int views;
        public int width;
    }

    public class Order {
        public boolean asc;
        public String column;
    }
}
