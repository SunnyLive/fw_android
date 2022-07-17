package com.fengwo.module_websocket.bean;

public class WenboAddTimeMsg {

    /**
     * action : userAddTime
     * addTimeresult : {"accept":1,"expireTime":7191229}
     * anchor : {"userId":"7008212","nickname":"好难好难","headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1588942366786.jpg"}
     * content : {"type":"text","value":"主播已接单"}
     */

    private String action;
    private AddTimeresultBean addTimeresult;
    private AnchorBean anchor;
    private ContentBean content;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public AddTimeresultBean getAddTimeresult() {
        return addTimeresult;
    }

    public void setAddTimeresult(AddTimeresultBean addTimeresult) {
        this.addTimeresult = addTimeresult;
    }

    public AnchorBean getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorBean anchor) {
        this.anchor = anchor;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class AddTimeresultBean {
        /**
         * accept : 1
         * expireTime : 7191229
         */

        private int accept;
        private long expireTime;

        public int getAccept() {
            return accept;
        }

        public void setAccept(int accept) {
            this.accept = accept;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }
    }

    public static class AnchorBean {
        /**
         * userId : 7008212
         * nickname : 好难好难
         * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1588942366786.jpg
         */

        private String userId;
        private String nickname;
        private String headImg;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }

    public static class ContentBean {
        /**
         * type : text
         * value : 主播已接单
         */

        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
