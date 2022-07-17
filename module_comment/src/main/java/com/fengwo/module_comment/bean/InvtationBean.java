package com.fengwo.module_comment.bean;

/**
 * @anchor Administrator
 * @date 2020/10/30
 */
public class InvtationBean {


    /**
     * fromUid : -1
     * toUid : 500758
     * msgId : 202010301825228881322122417717428226
     * version : 2.0
     * vendor : Im Server
     * timestamp : 1604053522888
     * busiEvent : notice
     * description : 达人邀请用户
     * data : {"action":"anchorInviteUser","room":{"roomId":"6122071604053190","roomTitle":"低血糖请补充点..我","anchorId":"612207"},"anchor":{"userId":"612207","nickname":"初禾","role":null,"headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/612207/images_1592422208_ug3hvRcoeu.jpeg"},"user":{"userId":"500758","nickname":"喵了个咪呀","role":null,"headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500758/images_1600262441_VlbhndU5kj.jpeg"},"content":{"type":"text","value":"邀请你加入ta的达人间"},"isPay":null,"gears":null,"expireTime":null,"totalOrderTime":null}
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
         * action : anchorInviteUser
         * room : {"roomId":"6122071604053190","roomTitle":"低血糖请补充点..我","anchorId":"612207"}
         * anchor : {"userId":"612207","nickname":"初禾","role":null,"headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/612207/images_1592422208_ug3hvRcoeu.jpeg"}
         * user : {"userId":"500758","nickname":"喵了个咪呀","role":null,"headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500758/images_1600262441_VlbhndU5kj.jpeg"}
         * content : {"type":"text","value":"邀请你加入ta的达人间"}
         * isPay : null
         * gears : null
         * expireTime : null
         * totalOrderTime : null
         */

        private String action;
        private RoomBean room;
        private AnchorBean anchor;
        private UserBean user;
        private ContentBean content;
        private Object isPay;
        private Object gears;
        private Object expireTime;
        private Object totalOrderTime;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public AnchorBean getAnchor() {
            return anchor;
        }

        public void setAnchor(AnchorBean anchor) {
            this.anchor = anchor;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public Object getIsPay() {
            return isPay;
        }

        public void setIsPay(Object isPay) {
            this.isPay = isPay;
        }

        public Object getGears() {
            return gears;
        }

        public void setGears(Object gears) {
            this.gears = gears;
        }

        public Object getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Object expireTime) {
            this.expireTime = expireTime;
        }

        public Object getTotalOrderTime() {
            return totalOrderTime;
        }

        public void setTotalOrderTime(Object totalOrderTime) {
            this.totalOrderTime = totalOrderTime;
        }

        public static class RoomBean {
            /**
             * roomId : 6122071604053190
             * roomTitle : 低血糖请补充点..我
             * anchorId : 612207
             */

            private String roomId;
            private String roomTitle;
            private String anchorId;

            public String getRoomId() {
                return roomId;
            }

            public void setRoomId(String roomId) {
                this.roomId = roomId;
            }

            public String getRoomTitle() {
                return roomTitle;
            }

            public void setRoomTitle(String roomTitle) {
                this.roomTitle = roomTitle;
            }

            public String getAnchorId() {
                return anchorId;
            }

            public void setAnchorId(String anchorId) {
                this.anchorId = anchorId;
            }
        }

        public static class AnchorBean {
            /**
             * userId : 612207
             * nickname : 初禾
             * role : null
             * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/612207/images_1592422208_ug3hvRcoeu.jpeg
             */

            private String userId;
            private String nickname;
            private Object role;
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

            public Object getRole() {
                return role;
            }

            public void setRole(Object role) {
                this.role = role;
            }

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }
        }

        public static class UserBean {
            /**
             * userId : 500758
             * nickname : 喵了个咪呀
             * role : null
             * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500758/images_1600262441_VlbhndU5kj.jpeg
             */

            private String userId;
            private String nickname;
            private Object role;
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

            public Object getRole() {
                return role;
            }

            public void setRole(Object role) {
                this.role = role;
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
             * value : 邀请你加入ta的达人间
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
