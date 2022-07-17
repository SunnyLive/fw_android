package com.fengwo.module_websocket.bean;

public class WebboBulletin {


    /**
     * action : enterRoomNotice
     * user : {"userId":"","nickname":"","headImg":""}
     * anchor : {"userId":"","nickname":"xxx","headImg":""}
     * room : {"roomId":"","roomTitle":""}
     * content : {"type":"text","value":"????xxx?????"}
     * expireTime : 25
     */

    private String action;
    private UserBean user;
    private AnchorBean anchor;
    private RoomBean room;
    private ContentBean content;
    private int expireTime;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public AnchorBean getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorBean anchor) {
        this.anchor = anchor;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public static class UserBean {
        /**
         * userId :
         * nickname :
         * headImg :
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

    public static class AnchorBean {
        /**
         * userId :
         * nickname : xxx
         * headImg :
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

    public static class RoomBean {
        /**
         * roomId :
         * roomTitle :
         */

        private String roomId;
        private String roomTitle;

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
    }

    public static class ContentBean {
        /**
         * type : text
         * value : ????xxx?????
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
