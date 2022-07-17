package com.fengwo.module_login.mvp.dto;

import java.util.List;

public class GiftWallDto {


    private PageList pageList;
    private double totalMoney;

    public PageList getPageList() {
        return pageList;
    }

    public void setPageList(PageList pageList) {
        this.pageList = pageList;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public static class PageList {

        private List<GiftWall> records;
        private int total;
        private int size;
        private int current;
        private int pages;
        private boolean searchCount;

        public List<GiftWall> getRecords() {
            return records;
        }

        public void setRecords(List<GiftWall> records) {
            this.records = records;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

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

        public static class GiftWall {

            private String giftIcon;  //礼物图片
            private int giftId;  //礼物ID
            private String giftName;  //  礼物名称
            private int giftPrice;  //  礼物价格
            private int totalMoney;  // 礼物总价
            private int totalQuantity;  // 礼物总数量


            public String getGiftIcon() {
                return giftIcon;
            }

            public void setGiftIcon(String giftIcon) {
                this.giftIcon = giftIcon;
            }

            public int getGiftId() {
                return giftId;
            }

            public void setGiftId(int giftId) {
                this.giftId = giftId;
            }

            public String getGiftName() {
                return giftName;
            }

            public void setGiftName(String giftName) {
                this.giftName = giftName;
            }

            public int getGiftPrice() {
                return giftPrice;
            }

            public void setGiftPrice(int giftPrice) {
                this.giftPrice = giftPrice;
            }

            public int getTotalMoney() {
                return totalMoney;
            }

            public void setTotalMoney(int totalMoney) {
                this.totalMoney = totalMoney;
            }

            public int getTotalQuantity() {
                return totalQuantity;
            }

            public void setTotalQuantity(int totalQuantity) {
                this.totalQuantity = totalQuantity;
            }

        }
    }

}
