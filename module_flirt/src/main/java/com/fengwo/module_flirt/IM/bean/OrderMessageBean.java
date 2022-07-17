package com.fengwo.module_flirt.IM.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Author BLCS
 * @Time 2020/5/4 11:18
 */
public class OrderMessageBean implements Serializable {

    public static final int TYPE_ORDER = 1;
    public static final int TYPE_ADD_TIME = 2;

    public static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "OrderMessageBean{" +
                "action='" + action + '\'' +
                ", user=" + user +
                ", order=" + order +
                ", content=" + content +
                '}';
    }

    /**
     * action : order
     * user : {"userId":"60000","nickname":"用户昵称","headImg":""}
     * order : {"ordNo":""}
     * content : {"type":"text","value":"用户昵称1请求接入(半小时，30000花钻)是否接单"}
     */

    private String action;
    private UserBean user;
    private OrderBean order;
    private ContentBean content;


    private long showTime;
    public long guoqishijian;

    public String getAction() {
        return action;
    }
    public long getShowTime() {
        return showTime - System.currentTimeMillis() / 1000;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime+30;
    }

    public int getActionType() {
        switch (action) {
            case "order":
                return TYPE_ORDER;
            case "anchorAddTime":

                return TYPE_ADD_TIME;

        }
        return -1;
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

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class UserBean {
        /**
         * userId : 60000
         * nickname : 用户昵称
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

    public static class OrderBean {
        public String getConsNo() {
            return consNo;
        }

        public void setConsNo(String consNo) {
            this.consNo = consNo;
        }

        /**
         * ordNo :
         */

        private String consNo;

    }

    public static class ContentBean {
        /**
         * type : text
         * value : 用户昵称1请求接入(半小时，30000花钻)是否接单
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
