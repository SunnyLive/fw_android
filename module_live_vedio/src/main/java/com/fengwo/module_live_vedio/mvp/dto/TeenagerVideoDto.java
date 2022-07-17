/**
 * Copyright 2020 bejson.com
 */
package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_comment.Interfaces.ICardType;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-12-09 14:2:10
 * 青少年模式-视频数据
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TeenagerVideoDto {

    private int current;
    private int pages;
    private List<Records> records;
    private boolean searchCount;
    private int size;
    private int total;

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public boolean getSearchCount() {
        return searchCount;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }


    public static class Records implements ICardType, Serializable {

        private String createTime;
        private int id;
        private int isDeleted;
        private String keyword;
        private int sort;
        private String updateTime;
        private String videoCategory;
        private String videoThumb;
        private String videoTitle;
        private String videoUrl;
        private int viewCount;

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setIsDeleted(int isDeleted) {
            this.isDeleted = isDeleted;
        }

        public int getIsDeleted() {
            return isDeleted;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getSort() {
            return sort;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setVideoCategory(String videoCategory) {
            this.videoCategory = videoCategory;
        }

        public String getVideoCategory() {
            return videoCategory;
        }

        public void setVideoThumb(String videoThumb) {
            this.videoThumb = videoThumb;
        }

        public String getVideoThumb() {
            return videoThumb;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public int getViewCount() {
            return viewCount;
        }

        @Override
        public int getSourceType() {
            return 2;
        }

        @Override
        public String getPoster() {
            return getVideoThumb();
        }

        @Override
        public String getUrl() {
            return getVideoUrl();
        }
    }

}