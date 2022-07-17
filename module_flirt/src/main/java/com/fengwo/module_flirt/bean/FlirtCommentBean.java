package com.fengwo.module_flirt.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlirtCommentBean {

    /**
     * current : 0
     * pages : 0
     * records : [{"anchorId":0,"createTime":"2020-10-31T07:09:53.215Z","evTypes":"string","id":0,"periodTime":"string","startLevel":0,"updateTime":"2020-10-31T07:09:53.215Z","userHeadImg":"string","userId":0,"userNickname":"string"}]
     * searchCount : true
     * size : 0
     * total : 0
     */

    @SerializedName("current")
    private int current;
    @SerializedName("pages")
    private int pages;
    @SerializedName("searchCount")
    private boolean searchCount;
    @SerializedName("size")
    private int size;
    @SerializedName("total")
    private int total;
    /**
     * anchorId : 0
     * createTime : 2020-10-31T07:09:53.215Z
     * evTypes : string
     * id : 0
     * periodTime : string
     * startLevel : 0
     * updateTime : 2020-10-31T07:09:53.215Z
     * userHeadImg : string
     * userId : 0
     * userNickname : string
     */

    @SerializedName("records")
    private List<RecordsDTO> records;

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

    public List<RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsDTO> records) {
        this.records = records;
    }

    public static class RecordsDTO {
        @SerializedName("anchorId")
        private int anchorId;       // 主播id
        @SerializedName("createTime")
        private String createTime;  // 评价时间
        @SerializedName("evTypes")
        private String evTypes;     // 评价标签,隔开
        @SerializedName("id")
        private int id;             //
        @SerializedName("periodTime")
        private String periodTime;  // 时间段
        @SerializedName("startLevel")
        private int startLevel;     // 星级
        @SerializedName("updateTime")
        private String updateTime;  // 更新时间
        @SerializedName("userHeadImg")
        private String userHeadImg; // 用户头像
        @SerializedName("userId")
        private int userId;         // 用户id
        @SerializedName("userNickname")
        private String userNickname;    // 用户昵称

        public int getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(int anchorId) {
            this.anchorId = anchorId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEvTypes() {
            return evTypes;
        }

        public void setEvTypes(String evTypes) {
            this.evTypes = evTypes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPeriodTime() {
            return periodTime;
        }

        public void setPeriodTime(String periodTime) {
            this.periodTime = periodTime;
        }

        public int getStartLevel() {
            return startLevel;
        }

        public void setStartLevel(int startLevel) {
            this.startLevel = startLevel;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserHeadImg() {
            return userHeadImg;
        }

        public void setUserHeadImg(String userHeadImg) {
            this.userHeadImg = userHeadImg;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }
    }
}
