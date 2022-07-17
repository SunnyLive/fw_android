package com.fengwo.module_flirt.IM.bean;

import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @Author BLCS
 * @Time 2020/4/28 14:49
 */
public class ChatText {
    public final static String Text = "text";
    public final static String voice = "voice";
    public final static String images = "images";
    @SerializedName("fromUid:")
    private String _$FromUid215; // FIXME check this code
    private String msgId;
    private String version;
    private String vendor;
    private String timestamp;
    private String busiEvent;
    private DataBean data;

    /**
     * type  text/voice/images
     * @param toId
     * @param type
     */
    public static String build(String type,String toId,String toNickname,String toHeadImg,String content,String msgId,String roomId,String roomTitle){
        UserInfo user = UserManager.getInstance().getUser();
        String uId =  String.valueOf(user.getId());
        ChatText chatText = new ChatText();
        chatText._$FromUid215 = uId;
        chatText.msgId = msgId;
        chatText.version = "2.0";
        chatText.vendor = AppUtils.getModel();
        chatText.timestamp = System.currentTimeMillis() + "";
        chatText.busiEvent = "msg";
        DataBean dataBean = new DataBean();
        dataBean.action = "chat";
        DataBean.ContentBean contentBean = new DataBean.ContentBean();
        contentBean.type = type;
        contentBean.value = content;
        dataBean.content = contentBean;
        DataBean.ToUserBean toUserBean = new DataBean.ToUserBean();
        toUserBean.userId = toId;
        toUserBean.nickname = toNickname;
        toUserBean.role = "ROLE_ANCHOR";
        toUserBean.headImg = toHeadImg;
        dataBean.toUser = toUserBean;
        DataBean.FromUserBean fromUserBean = new DataBean.FromUserBean();
        fromUserBean.nickname = user.getNickname();
        fromUserBean.userId = uId;
        fromUserBean.role = "ROLE_USER";
        fromUserBean.headImg = user.getHeadImg();
        dataBean.fromUser = fromUserBean;
        DataBean.RoomBean roomBean = new DataBean.RoomBean();
        roomBean.roomId =roomId;
        roomBean.roomTitle = roomTitle;
        dataBean.room = roomBean;
        chatText.data = dataBean;
        return new Gson().toJson(chatText);
    }

    public String get_$FromUid215() {
        return _$FromUid215;
    }

    public void set_$FromUid215(String _$FromUid215) {
        this._$FromUid215 = _$FromUid215;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
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
}
