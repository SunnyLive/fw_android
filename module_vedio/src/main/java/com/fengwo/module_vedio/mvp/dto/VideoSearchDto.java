package com.fengwo.module_vedio.mvp.dto;

import com.fengwo.module_comment.bean.VideoHomeShortModel;

import java.util.List;

public  class VideoSearchDto {
    /**
     * movieInfoArr : {"current":0,"pages":0,"records":[{"albumId":0,"auditStatus":0,"comments":0,"cover":"string","createTime":"2019-12-27T03:50:02.061Z","duration":0,"favorites":0,"gifts":0,"headImg":"string","id":0,"intro":"string","isLike":true,"isPrivacy":0,"latitude":0,"likes":0,"longitude":0,"movieTitle":"string","newViews":0,"remark":"string","shares":0,"status":0,"upViews":0,"updateTime":"2019-12-27T03:50:02.061Z","url":"string","userId":0,"userName":"string","views":0}],"searchCount":true,"size":0,"total":0}
     * videoInfoArr : {"current":0,"pages":0,"records":[{"auditStatus":0,"bgmId":0,"comments":0,"cover":"string","createTime":"2019-12-27T03:50:02.061Z","description":"string","duration":0,"favorites":0,"gifts":0,"headImg":"string","id":0,"isLike":true,"isPrivacy":0,"latitude":0,"likes":0,"longitude":0,"menuId":0,"remark":"string","shares":0,"status":0,"topic":"string","updateTime":"2019-12-27T03:50:02.061Z","url":"string","userId":0,"userName":"string","videoTitle":"string","views":0}],"searchCount":true,"size":0,"total":0}
     */

    private MovieInfoArrBean movieInfoArr;
    private VideoInfoArrBean videoInfoArr;

    public MovieInfoArrBean getMovieInfoArr() {
        return movieInfoArr;
    }

    public void setMovieInfoArr(MovieInfoArrBean movieInfoArr) {
        this.movieInfoArr = movieInfoArr;
    }

    public VideoInfoArrBean getVideoInfoArr() {
        return videoInfoArr;
    }

    public void setVideoInfoArr(VideoInfoArrBean videoInfoArr) {
        this.videoInfoArr = videoInfoArr;
    }

    public static class MovieInfoArrBean {
        /**
         * current : 0
         * pages : 0
         * records : [{"albumId":0,"auditStatus":0,"comments":0,"cover":"string","createTime":"2019-12-27T03:50:02.061Z","duration":0,"favorites":0,"gifts":0,"headImg":"string","id":0,"intro":"string","isLike":true,"isPrivacy":0,"latitude":0,"likes":0,"longitude":0,"movieTitle":"string","newViews":0,"remark":"string","shares":0,"status":0,"upViews":0,"updateTime":"2019-12-27T03:50:02.061Z","url":"string","userId":0,"userName":"string","views":0}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<VideoHomeShortModel> records;

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

        public List<VideoHomeShortModel> getRecords() {
            return records;
        }

        public void setRecords(List<VideoHomeShortModel> records) {
            this.records = records;
        }

    }

    public static class VideoInfoArrBean {
        /**
         * current : 0
         * pages : 0
         * records : [{"auditStatus":0,"bgmId":0,"comments":0,"cover":"string","createTime":"2019-12-27T03:50:02.061Z","description":"string","duration":0,"favorites":0,"gifts":0,"headImg":"string","id":0,"isLike":true,"isPrivacy":0,"latitude":0,"likes":0,"longitude":0,"menuId":0,"remark":"string","shares":0,"status":0,"topic":"string","updateTime":"2019-12-27T03:50:02.061Z","url":"string","userId":0,"userName":"string","videoTitle":"string","views":0}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<VideoHomeShortModel> records;

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

        public List<VideoHomeShortModel> getRecords() {
            return records;
        }

        public void setRecords(List<VideoHomeShortModel> records) {
            this.records = records;
        }

    }
}
