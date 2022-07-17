package com.fengwo.module_websocket.bean;

/**
 * @Author BLCS
 * @Time 2020/4/28 14:49
 */
public class WenboWsGameDataBean {
    /**
     * action : chat
     * fromUser : {"userId":"50000","nickname":"user1","role":"ROLE_USER","headImg":"http://headImg.jpg"}
     * toUser : {"userId":"50001","nickname":"user2","role":"ROLE_ANCHOR","headImg":"http://headImg.jpg"}
     * room : {"roomId":"123456","roomTitle":"房间标题"}
     * content : {"type":"text/voice/images","value":"message/url"}
     */
    private String action;
    private FromUserBean fromUser;
    private ToUserBean toUser;
    private RoomBean room;
    private ContentBean content;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public FromUserBean getFromUser() {
        return fromUser;
    }

    public void setFromUser(FromUserBean fromUser) {
        this.fromUser = fromUser;
    }

    public ToUserBean getToUser() {
        return toUser;
    }

    public void setToUser(ToUserBean toUser) {
        this.toUser = toUser;
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

    public static class FromUserBean {
        /**
         * userId : 50000
         * nickname : user1
         * role : ROLE_USER
         * headImg : http://headImg.jpg
         */

        private String userId;
        private String nickname;
        private String role;
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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }

    public static class ToUserBean {
        /**
         * userId : 50001
         * nickname : user2
         * role : ROLE_ANCHOR
         * headImg : http://headImg.jpg
         */

        private String userId;
        private String nickname;
        private String role;
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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
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
         * roomId : 123456
         * roomTitle : 房间标题
         */

        private String roomId;
        private String roomTitle;
        private String anchorId;

        public String getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(String anchorId) {
            this.anchorId = anchorId;
        }

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
         * type : text/voice/images
         * value : message/url
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
