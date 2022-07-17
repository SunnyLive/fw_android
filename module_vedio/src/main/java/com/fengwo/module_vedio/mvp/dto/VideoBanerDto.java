package com.fengwo.module_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/29
 */
public class VideoBanerDto {

    /**
     * current : 0
     * pages : 0
     * records : [{"advertName":"string","contentType":0,"contentUrl":"string","createTime":"2019-11-29T09:03:28.886Z","descrip":"string","endTime":"2019-11-29T09:03:28.886Z","id":0,"image":"string","modelType":0,"objectType":0,"pageType":0,"parentId":0,"position":0,"priority":0,"sort":0,"startTime":"2019-11-29T09:03:28.886Z","status":0,"surviveTimes":0,"title":"string"}]
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
         * advertName : string
         * contentType : 0
         * contentUrl : string
         * createTime : 2019-11-29T09:03:28.886Z
         * descrip : string
         * endTime : 2019-11-29T09:03:28.886Z
         * id : 0
         * image : string
         * modelType : 0
         * objectType : 0
         * pageType : 0
         * parentId : 0
         * position : 0
         * priority : 0
         * sort : 0
         * startTime : 2019-11-29T09:03:28.886Z
         * status : 0
         * surviveTimes : 0
         * title : string
         */

        private String advertName;
        private int contentType;
        private String contentUrl;
        private String createTime;
        private String descrip;
        private String endTime;
        private int id;
        private String image;
        private int modelType;
        private int objectType;
        private int pageType;
        private int parentId;
        private int position;
        private int priority;
        private int sort;
        private String startTime;
        private int status;
        private int surviveTimes;
        private String title;


        public RecordsBean(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getAdvertName() {
            return advertName;
        }

        public void setAdvertName(String advertName) {
            this.advertName = advertName;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescrip() {
            return descrip;
        }

        public void setDescrip(String descrip) {
            this.descrip = descrip;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getModelType() {
            return modelType;
        }

        public void setModelType(int modelType) {
            this.modelType = modelType;
        }

        public int getObjectType() {
            return objectType;
        }

        public void setObjectType(int objectType) {
            this.objectType = objectType;
        }

        public int getPageType() {
            return pageType;
        }

        public void setPageType(int pageType) {
            this.pageType = pageType;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSurviveTimes() {
            return surviveTimes;
        }

        public void setSurviveTimes(int surviveTimes) {
            this.surviveTimes = surviveTimes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
