package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/23
 */
public class LiveProfitDto {
    /**
     * current : 0
     * lookNums : 0
     * orders : [{"asc":true,"column":"string"}]
     * pages : 0
     * records : [{"headImg":"string","anchorLevel":0,"id":0,"nickname":"string","nobility":0,"score":0,"sex":0,"userLevel":0}]
     * searchCount : true
     * size : 0
     * total : 0
     */

    private int current;
    private int lookNums;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private int thisReceive;
    private List<OrdersBean> orders;
    private List<RecordsBean> records;

    public int getThisReceive() {
        return thisReceive;
    }

    public void setThisReceive(int thisReceive) {
        this.thisReceive = thisReceive;
    }

    public LiveProfitDto(int total, List<RecordsBean> records) {
        this.total = total;
        this.records = records;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLookNums() {
        return lookNums;
    }

    public void setLookNums(int lookNums) {
        this.lookNums = lookNums;
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

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class OrdersBean {
        /**
         * asc : true
         * column : string
         */

        private boolean asc;
        private String column;

        public boolean isAsc() {
            return asc;
        }

        public void setAsc(boolean asc) {
            this.asc = asc;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }
    }

    public static class RecordsBean {
        /**
         * headImg : string
         * anchorLevel : 0
         * id : 0
         * nickname : string
         * nobility : 0
         * score : 0
         * sex : 0
         * userLevel : 0
         */

        private String headImg;
        private int hostLevel;
        private int id;
        private String nickname;
        private int nobility;
        private int receive;
        private int sex;
        private int userLevel;

        public RecordsBean(String nickname, int receive) {
            this.nickname = nickname;
            this.receive = receive;
        }

        public RecordsBean() {
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getHostLevel() {
            return hostLevel;
        }

        public void setHostLevel(int hostLevel) {
            this.hostLevel = hostLevel;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getNobility() {
            return nobility;
        }

        public void setNobility(int nobility) {
            this.nobility = nobility;
        }

        public int getReceive() {
            return receive;
        }

        public void setReceive(int receive) {
            this.receive = receive;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }
    }
}
