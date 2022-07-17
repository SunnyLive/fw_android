package com.fengwo.module_login.mvp.dto;

import java.util.List;

public class ReceiveGiftDto {

    /**
     * list : {"current":0,"pages":0,"records":[{"createTime":"2019-10-31T11:10:18.537Z","giftName":"string","money":0,"objectId":0,"quantity":0,"userHeadImg":"string","userId":0,"userNickname":"string"}],"searchCount":true,"size":0,"total":0}
     * totalMoney : 0
     */

    private ListBean list;
    private int totalMoney;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public static class ListBean {
        /**
         * current : 0
         * pages : 0
         * records : [{"createTime":"2019-10-31T11:10:18.537Z","giftName":"string","money":0,"objectId":0,"quantity":0,"userHeadImg":"string","userId":0,"userNickname":"string"}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<RecordsBean> records;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
            /**
             * createTime : 2019-10-31T11:10:18.537Z
             * giftName : string
             * money : 0
             * objectId : 0
             * quantity : 0
             * userHeadImg : string
             * userId : 0
             * userNickname : string
             */

            private String createTime;
            private String giftName;
            private int money;
            private int objectId;
            private int quantity;
            private String userHeadImg;
            private int userId;
            private String userNickname;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getGiftName() {
                return giftName;
            }

            public void setGiftName(String giftName) {
                this.giftName = giftName;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public int getObjectId() {
                return objectId;
            }

            public void setObjectId(int objectId) {
                this.objectId = objectId;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getUserHeadImg() {
                return userHeadImg;
            }

            public void setUserHeadImg(String userHeadImg) {
                this.userHeadImg = userHeadImg;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserNickname() {
                return userNickname;
            }

            public void setUserNickname(String userNickname) {
                this.userNickname = userNickname;
            }
        }
    }
}
