package com.fengwo.module_websocket.bean;

public class WenboGiftMsg {
    @Override
    public String toString() {
        return "WenboGiftMsg{" +
                "action='" + action + '\'' +
                ", user=" + user +
                ", gift=" + gift +
                ", content=" + content +
                '}';
    }

    private String action;
    private User user;
    private Gift gift;
    private Content content;
    private int gears;

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        this.gears = gears;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public static class Content {

        /**
         * type : text
         * value : 涛声依旧 用户送了1个梦幻水晶
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


    public static class User {

        /**
         * userId : 500794
         * nickname : 涛声依旧
         * headImg : null
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


    public static class Gift {
        @Override
        public String toString() {
            return "Gift{" +
                    "giftId='" + giftId + '\'' +
                    ", giftName='" + giftName + '\'' +
                    ", bigImgPath='" + bigImgPath + '\'' +
                    ", smallImgPath='" + smallImgPath + '\'' +
                    ", giftPrice='" + giftPrice + '\'' +
                    ", charmValue='" + charmValue + '\'' +
                    '}';
        }

        /**
         * giftId :
         * giftName :
         * bigImgPath :
         * smallImgPath :
         * giftPrice :
         */

        private String giftId;
        private String giftName;
        private String bigImgPath;
        private String smallImgPath;
        private String giftPrice;
        private String charmValue;
        private int gears;
        private boolean isGears = false;

        public int getGears() {
            return gears;
        }

        public void setGears(int gears) {
            this.gears = gears;
        }

        public boolean isGears() {
            return isGears;
        }

        public void setGears(boolean gears) {
            isGears = gears;
        }

        public String getCharmValue() {
            return charmValue;
        }

        public void setCharmValue(String charmValue) {
            this.charmValue = charmValue;
        }

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getBigImgPath() {
            return bigImgPath;
        }

        public void setBigImgPath(String bigImgPath) {
            this.bigImgPath = bigImgPath;
        }

        public String getSmallImgPath() {
            return smallImgPath;
        }

        public void setSmallImgPath(String smallImgPath) {
            this.smallImgPath = smallImgPath;
        }

        public String getGiftPrice() {
            return giftPrice;
        }

        public void setGiftPrice(String giftPrice) {
            this.giftPrice = giftPrice;
        }
    }

}
