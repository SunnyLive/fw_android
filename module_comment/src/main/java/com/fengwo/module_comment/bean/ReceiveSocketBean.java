package com.fengwo.module_comment.bean;

/**
 * @Author BLCS
 * @Time 2020/5/5 12:02
 */
public class ReceiveSocketBean {

    /**
     * action : liveStart
     * content : {"type":"text","value":"主播开播提示内容"}
     * user:{"headImg": "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/513037/images_1588062351_dSGe7ui1iA.jpeg","nickname": "我要","userId": "513037" }
     */
    private String action;
    private String appointmentTime;
    private String appointmentId;
    private String appointmentResult;

    public String getAppointmentResult() {
        return appointmentResult;
    }

    public void setAppointmentResult(String appointmentResult) {
        this.appointmentResult = appointmentResult;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    private UserBean user;
    private AnchorBean anchor;

    public AnchorBean getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorBean anchor) {
        this.anchor = anchor;
    }

    private ContentBean content;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    private ResultBean result;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }


    public static class UserBean {
        /**
         * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/513037/images_1588062351_dSGe7ui1iA.jpeg
         * nickname : 我要
         * userId : 513037
         */

        private String headImg;
        private String nickname;
        private String userId;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
    public static class AnchorBean {
        /**
         * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/513037/images_1588062351_dSGe7ui1iA.jpeg
         * nickname : 我要
         * userId : 513037
         */

        private String headImg;
        private String nickname;
        private String userId;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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


    public static class ResultBean {
        private int accept;

        public int getAccept() {
            return accept;
        }

        public void setAccept(int accept) {
            this.accept = accept;
        }
    }

}
