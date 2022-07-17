package com.fengwo.module_flirt.bean;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/10/28
 */
public class IliaoBean {

    /**
     * current : 0
     * pages : 0
     * records : [{"age":0,"headImg":"string","id":0,"isInvited":true,"nickname":"string","onlineStatus":0,"sex":0,"signature":"string"}]
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
         * age : 0
         * headImg : string
         * id : 0
         * isInvited : true
         * nickname : string
         * onlineStatus : 0
         * sex : 0
         * signature : string
         */

        private int age;
        private String headImg;
        private int id;
        private boolean isInvited;
        private String nickname;
        private int onlineStatus;
        private int sex;
        private String signature;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsInvited() {
            return isInvited;
        }

        public void setIsInvited(boolean isInvited) {
            this.isInvited = isInvited;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(int onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
