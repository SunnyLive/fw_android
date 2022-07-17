package com.fengwo.module_flirt.bean;

/**
 * @anchor Administrator
 * @date 2020/12/4
 */
public class officialBean {

    /**
     * busiEvent : official_news
     * data : {"content":"欢迎下载蜂窝互娱APP","createTime":"2020-11-16T16:20:59","eventId":100722,"expireTime":"2021-05-15T16:21:05","id":6,"isCreateUrl":0,"sendTime":"2020-11-16T16:21:05","sendType":1,"status":2,"subTitle":"子标题","title":"消息1","type":1,"updateTime":"2020-12-04T11:07:00","url":""}
     * fromUid : system
     * msgId : 202012041416548441334743464146841601
     * timestamp : 1607062614844
     * toUid : 935964
     * vendor : server
     * version : 2.0
     */

    private String busiEvent;
    private DataBean data;
    private String fromUid;
    private String msgId;
    private String timestamp;
    private String toUid;
    private String vendor;
    private String version;

    public String getBusiEvent() {
        return busiEvent;
    }

    public void setBusiEvent(String busiEvent) {
        this.busiEvent = busiEvent;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {
        /**
         * content : 欢迎下载蜂窝互娱APP
         * createTime : 2020-11-16T16:20:59
         * eventId : 100722
         * expireTime : 2021-05-15T16:21:05
         * id : 6
         * isCreateUrl : 0
         * sendTime : 2020-11-16T16:21:05
         * sendType : 1
         * status : 2
         * subTitle : 子标题
         * title : 消息1
         * type : 1
         * updateTime : 2020-12-04T11:07:00
         * url :
         */

        private String content;
        private String createTime;
        private int eventId;
        private String expireTime;
        private int id;
        private int isCreateUrl;
        private String sendTime;
        private int sendType;
        private int status;
        private String subTitle;
        private String title;
        private int type;
        private String updateTime;
        private String url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsCreateUrl() {
            return isCreateUrl;
        }

        public void setIsCreateUrl(int isCreateUrl) {
            this.isCreateUrl = isCreateUrl;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public int getSendType() {
            return sendType;
        }

        public void setSendType(int sendType) {
            this.sendType = sendType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
