package com.fengwo.module_flirt.bean;

import com.fengwo.module_chat.utils.chat_new.FingerprintUtils;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;

/**
 * @Author BLCS
 * @Time 2020/4/28 15:50
 */
public class PremiereNotice {

    /**
     * fromUid : -1
     * toUid : 5000
     * msgId : 202004231425558571253208463557091322
     * version : 2.0
     * vendor : Im Server
     * timestamp : 15846568956
     * busiEvent : notice
     * description : 主播开播通知
     * data : {"action":"liveStart","anchor":{"userId":"60000","nickname":"主播昵称"},"content":{"type":"text","value":"主播开播提示内容"}}
     */

    private String fromUid;
    private String toUid;
    private String msgId;
    private String version;
    private String vendor;
    private String timestamp;
    private String busiEvent;
    private String description;
    private DataBean data;



    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBusiEvent() {
        return busiEvent;
    }

    public void setBusiEvent(String busiEvent) {
        this.busiEvent = busiEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * action : liveStart
         * anchor : {"userId":"60000","nickname":"主播昵称"}
         * content : {"type":"text","value":"主播开播提示内容"}
         */

        private String action;
        private AnchorBean anchor;
        private ContentBean content;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
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

        public static class AnchorBean {
            /**
             * userId : 60000
             * nickname : 主播昵称
             */

            private String userId;
            private String nickname;

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
        }

        public static class ContentBean {
            /**
             * type : text
             * value : 主播开播提示内容
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
}
