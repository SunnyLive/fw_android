package com.fengwo.module_chat.mvp.model.bean;

import com.fengwo.module_comment.bean.CircleListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/27
 */
public class SearchCardBean  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * current : 0
     * pages : 0
     * records : [{"adContentUrl":"string","adImage":"string","cover":"string","excerpt":"string","headImg":"string","id":0,"isAd":0,"likes":0,"nickname":"string","position":"string","roomId":"string","status":0,"userId":0}]
     * searchCount : true
     * size : 0
     * total : 0
     */

    private int current;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private List<CircleListBean> records;

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

    public List<CircleListBean> getRecords() {
        return records;
    }

    public void setRecords(List<CircleListBean> records) {
        this.records = records;
    }



}
