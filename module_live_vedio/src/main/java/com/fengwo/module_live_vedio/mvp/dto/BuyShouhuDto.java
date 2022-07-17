package com.fengwo.module_live_vedio.mvp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuyShouhuDto {

    private List<LevelListBean> levelList;
    private List<GuardListBean> guardList;


    public List<LevelListBean> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<LevelListBean> levelList) {
        this.levelList = levelList;
    }

    public List<GuardListBean> getGuardList() {
        return guardList;
    }

    public void setGuardList(List<GuardListBean> guardList) {
        this.guardList = guardList;
    }

    public static class LevelListBean {
        /**
         * id : 2
         * type : 1
         * level : 1
         * levelName : 小守护
         * levelIcon : http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/virtualgoods_level/guard/level1.png
         * createDate : 2019-11-01T02:03:18Z
         * updateDate : 2019-11-01T02:03:18Z
         */

        @SerializedName("id")
        private int idX;
        private int type;
        @SerializedName("level")
        private int levelX;
        private String levelName;
        private String levelIcon;
        private String createDate;
        private String updateDate;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getLevelX() {
            return levelX;
        }

        public void setLevelX(int levelX) {
            this.levelX = levelX;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getLevelIcon() {
            return levelIcon;
        }

        public void setLevelIcon(String levelIcon) {
            this.levelIcon = levelIcon;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }

    public static class GuardListBean {
        /**
         * id : 58
         * level : 1
         * itemName : 小守护60天
         * itemPrice : 555
         * guardIcon :
         * dayNum : 60
         * privilegeVOList : [{"id":66,"level":1,"privilegeId":19,"privilegeName":"身份标识","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/1.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/1_1.png","privilegeRemark":"","privilegeValue":"","description":"","levelType":2,"sort":0,"createDate":"2019-11-01T12:55:15Z","updateDate":"2019-11-01T12:55:15Z","isHave":1},{"id":65,"level":1,"privilegeId":20,"privilegeName":"进场特效","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/2.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/2_1.png","privilegeRemark":"","privilegeValue":"","description":"","levelType":2,"sort":0,"createDate":"2019-11-01T12:55:10Z","updateDate":"2019-11-01T12:55:10Z","isHave":1},{"id":81,"level":1,"privilegeId":23,"privilegeName":"踢人","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/7.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/7_1.png","privilegeRemark":"","privilegeValue":"","description":"","levelType":1,"sort":0,"createDate":"2019-11-06T12:22:22Z","updateDate":"2019-11-06T12:22:22Z","isHave":1},{"id":82,"level":1,"privilegeId":24,"privilegeName":"禁言","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/5.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/5_1.png","privilegeRemark":"","privilegeValue":"","description":"","levelType":1,"sort":0,"createDate":"2019-11-06T12:22:27Z","updateDate":"2019-11-06T12:22:27Z","isHave":1},{"id":null,"level":null,"privilegeId":25,"privilegeName":"发弹幕","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/6.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/6_1.png","privilegeRemark":"","privilegeValue":null,"description":null,"levelType":null,"sort":null,"createDate":null,"updateDate":null,"isHave":0},{"id":67,"level":1,"privilegeId":31,"privilegeName":"防禁言","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/3.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/3_1.png","privilegeRemark":"","privilegeValue":"","description":"","levelType":2,"sort":0,"createDate":"2019-11-04T12:18:05Z","updateDate":"2019-11-04T12:18:05Z","isHave":1},{"id":null,"level":null,"privilegeId":41,"privilegeName":"防踢","privilegeIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/4.png","privilegeIconGray":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/4_1.png","privilegeRemark":"","privilegeValue":null,"description":null,"levelType":null,"sort":null,"createDate":null,"updateDate":null,"isHave":0}]
         * giveMotoring : 0
         * status : 1
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("level")
        private int levelX;
        @SerializedName("itemName")
        private String itemNameX;
        @SerializedName("itemPrice")
        private int itemPriceX;
        @SerializedName("guardIcon")
        private String guardIconX;
        @SerializedName("dayNum")
        private int dayNumX;
        @SerializedName("giveMotoring")
        private int giveMotoringX;
        @SerializedName("status")
        private int statusX;
        private List<PrivilegeVOListBean> privilegeVOList;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public int getLevelX() {
            return levelX;
        }

        public void setLevelX(int levelX) {
            this.levelX = levelX;
        }

        public String getItemNameX() {
            return itemNameX;
        }

        public void setItemNameX(String itemNameX) {
            this.itemNameX = itemNameX;
        }

        public int getItemPriceX() {
            return itemPriceX;
        }

        public void setItemPriceX(int itemPriceX) {
            this.itemPriceX = itemPriceX;
        }

        public String getGuardIconX() {
            return guardIconX;
        }

        public void setGuardIconX(String guardIconX) {
            this.guardIconX = guardIconX;
        }

        public int getDayNumX() {
            return dayNumX;
        }

        public void setDayNumX(int dayNumX) {
            this.dayNumX = dayNumX;
        }

        public int getGiveMotoringX() {
            return giveMotoringX;
        }

        public void setGiveMotoringX(int giveMotoringX) {
            this.giveMotoringX = giveMotoringX;
        }

        public int getStatusX() {
            return statusX;
        }

        public void setStatusX(int statusX) {
            this.statusX = statusX;
        }

        public List<PrivilegeVOListBean> getPrivilegeVOList() {
            return privilegeVOList;
        }

        public void setPrivilegeVOList(List<PrivilegeVOListBean> privilegeVOList) {
            this.privilegeVOList = privilegeVOList;
        }

        public static class PrivilegeVOListBean {
            /**
             * id : 66
             * level : 1
             * privilegeId : 19
             * privilegeName : 身份标识
             * privilegeIcon : http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/1.png
             * privilegeIconGray : http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/resource/privilege/guard/1_1.png
             * privilegeRemark :
             * privilegeValue :
             * description :
             * levelType : 2
             * sort : 0
             * createDate : 2019-11-01T12:55:15Z
             * updateDate : 2019-11-01T12:55:15Z
             * isHave : 1
             */

            @SerializedName("id")
            private int idX;
            @SerializedName("level")
            private int levelX;
            private int privilegeId;
            private String privilegeName;
            private String privilegeIcon;
            private String privilegeIconGray;
            private String privilegeRemark;
            private String privilegeValue;
            private String description;
            private int levelType;
            private int sort;
            private String createDate;
            private String updateDate;
            private int isHave;

            public int getIdX() {
                return idX;
            }

            public void setIdX(int idX) {
                this.idX = idX;
            }

            public int getLevelX() {
                return levelX;
            }

            public void setLevelX(int levelX) {
                this.levelX = levelX;
            }

            public int getPrivilegeId() {
                return privilegeId;
            }

            public void setPrivilegeId(int privilegeId) {
                this.privilegeId = privilegeId;
            }

            public String getPrivilegeName() {
                return privilegeName;
            }

            public void setPrivilegeName(String privilegeName) {
                this.privilegeName = privilegeName;
            }

            public String getPrivilegeIcon() {
                return privilegeIcon;
            }

            public void setPrivilegeIcon(String privilegeIcon) {
                this.privilegeIcon = privilegeIcon;
            }

            public String getPrivilegeIconGray() {
                return privilegeIconGray;
            }

            public void setPrivilegeIconGray(String privilegeIconGray) {
                this.privilegeIconGray = privilegeIconGray;
            }

            public String getPrivilegeRemark() {
                return privilegeRemark;
            }

            public void setPrivilegeRemark(String privilegeRemark) {
                this.privilegeRemark = privilegeRemark;
            }

            public String getPrivilegeValue() {
                return privilegeValue;
            }

            public void setPrivilegeValue(String privilegeValue) {
                this.privilegeValue = privilegeValue;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getLevelType() {
                return levelType;
            }

            public void setLevelType(int levelType) {
                this.levelType = levelType;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
            }

            public int getIsHave() {
                return isHave;
            }

            public void setIsHave(int isHave) {
                this.isHave = isHave;
            }
        }
    }
}
