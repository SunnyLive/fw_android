package com.fengwo.module_login.mvp.dto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/16
 */
public class MyCarDto {

    /**
     * current : 0
     * pages : 0
     * records : [{"duration":0,"expireTime":"2019-10-16T03:57:29.056Z","isOpened":0,"motoringId":0,"motoringName":"string","motoringThumb":"string"}]
     * searchCount : true
     * size : 0
     * total : 0
     */

    private int current;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private List<RecordsBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * duration : 0
         * expireTime : 2019-10-16T03:57:29.056Z
         * isOpened : 0
         * motoringId : 0
         * motoringName : string
         * motoringThumb : string
         */

        private int duration;
        private String expireTime;
        private int isOpened;
        private int motoringId;
        private int motoringType;//类型 0 显示，其他  不显示续费按钮
        private String motoringName;
        private String motoringThumb;
        private String motoringSwf;

        public int getMotoringType() {
            return motoringType;
        }

        public void setMotoringType(int motoringType) {
            this.motoringType = motoringType;
        }

        public String getMotoringSwf() {
            return motoringSwf;
        }

        public void setMotoringSwf(String motoringSwf) {
            this.motoringSwf = motoringSwf;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getIsOpened() {
            return isOpened;
        }

        public void setIsOpened(int isOpened) {
            this.isOpened = isOpened;
        }

        public int getMotoringId() {
            return motoringId;
        }

        public void setMotoringId(int motoringId) {
            this.motoringId = motoringId;
        }

        public String getMotoringName() {
            return motoringName;
        }

        public void setMotoringName(String motoringName) {
            this.motoringName = motoringName;
        }

        public String getMotoringThumb() {
            return motoringThumb;
        }

        public void setMotoringThumb(String motoringThumb) {
            this.motoringThumb = motoringThumb;
        }
    }
}
