package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public class AttentionListDto {

    /**
     * current : 0
     * pages : 0
     * records : [{"headImg":"string","id":0,"nickname":"string"}]
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
         * headImg : string
         * id : 0
         * nickname : string
         */

        private String headImg;
        private int id;
        private String nickname;

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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
