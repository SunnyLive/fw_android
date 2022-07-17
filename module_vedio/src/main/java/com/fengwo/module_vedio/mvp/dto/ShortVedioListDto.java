package com.fengwo.module_vedio.mvp.dto;

import java.io.Serializable;
import java.util.List;

public class ShortVedioListDto {

    public List<ShortVedio> records;

    public class ShortVedio implements Serializable {
        public int id;
        public String movieTitle;
        public String url;
        public int duration;
        public int favorites;
        public int comments;
        public int views;
        public int shares;
        public boolean isFavorite;
        public String cover;
    }
}
