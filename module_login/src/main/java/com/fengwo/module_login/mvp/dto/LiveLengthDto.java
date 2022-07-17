package com.fengwo.module_login.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/30
 */
public class LiveLengthDto {
    /**
     * current : 0
     * pages : 0
     * records : [{"channelId":0,"createTime":"2019-10-30T01:38:46.017Z","deviceInfo":"string","endTime":"2019-10-30T01:38:46.017Z","id":0,"liveTime":0,"lookTimes":0,"nickname":"string","profit":0,"shareTimes":0,"startTime":"2019-10-30T01:38:46.017Z","thumb":"string","title":"string","updateTime":"2019-10-30T01:38:46.017Z","virtualNums":0}]
     * searchCount : true
     * size : 0
     * total : 0
     */

    private int current;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private String channelTimes;
    private String liveTimes;
    private List<RecordsBean> records;

    public String getLiveTimes() {
        return liveTimes;
    }

    public void setLiveTimes(String liveTimes) {
        this.liveTimes = liveTimes;
    }

    public String getChannelTimes() {
        return channelTimes;
    }

    public void setChannelTimes(String channelTimes) {
        this.channelTimes = channelTimes;
    }

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
         * channelId : 0
         * createTime : 2019-10-30T01:38:46.017Z
         * deviceInfo : string
         * endTime : 2019-10-30T01:38:46.017Z
         * id : 0
         * liveTime : 0
         * lookTimes : 0
         * nickname : string
         * profit : 0
         * shareTimes : 0
         * startTime : 2019-10-30T01:38:46.017Z
         * thumb : string
         * title : string
         * updateTime : 2019-10-30T01:38:46.017Z
         * virtualNums : 0
         */

        private int channelId;
        private String createTime;
        private String deviceInfo;
        private String endTime;
        private int id;
        private int liveTime;
        private int lookTimes;
        private String nickname;
        private int profit;
        private int shareTimes;
        private String startTime;
        private String thumb;
        private String title;
        private String updateTime;
        private String liveTimes;
        private int virtualNums;

        public String getLiveTimes() {
            return liveTimes;
        }

        public void setLiveTimes(String liveTimes) {
            this.liveTimes = liveTimes;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
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

        public int getLiveTime() {
            return liveTime;
        }

        public void setLiveTime(int liveTime) {
            this.liveTime = liveTime;
        }

        public int getLookTimes() {
            return lookTimes;
        }

        public void setLookTimes(int lookTimes) {
            this.lookTimes = lookTimes;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getProfit() {
            return profit;
        }

        public void setProfit(int profit) {
            this.profit = profit;
        }

        public int getShareTimes() {
            return shareTimes;
        }

        public void setShareTimes(int shareTimes) {
            this.shareTimes = shareTimes;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getVirtualNums() {
            return virtualNums;
        }

        public void setVirtualNums(int virtualNums) {
            this.virtualNums = virtualNums;
        }
    }
}
