package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/17
 */
public class H5addressBean {


    /**
     * status : 1
     * addressLink : https://appszdev.fengwohuyu.com/activeChristmas
     * homePageActivity : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/activity/newyear/popup.png
     * title : 冰雪奇缘双旦Party · 跨年
     * exchangeActivityPropInfo : {"prevPropName":"幸运袜","prevPropIcon":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/giftTask/1607052033000*giftTask714336577.png","prevPropQuantity":143,"propName":"许愿棒","propIcon":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/giftTask/1607052033000*giftTask714336577.png","propQuantity":143}
     */

    private String status;
    private String address_link;
    private String home_page_activity;
    private String title;
    private ExchangeActivityPropInfoBean exchangeActivityPropInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressLink() {
        return address_link;
    }

    public void setAddressLink(String addressLink) {
        this.address_link = addressLink;
    }

    public String getHomePageActivity() {
        return home_page_activity;
    }

    public void setHomePageActivity(String homePageActivity) {
        this.home_page_activity = homePageActivity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExchangeActivityPropInfoBean getExchangeActivityPropInfo() {
        return exchangeActivityPropInfo;
    }

    public void setExchangeActivityPropInfo(ExchangeActivityPropInfoBean exchangeActivityPropInfo) {
        this.exchangeActivityPropInfo = exchangeActivityPropInfo;
    }

    public static class ExchangeActivityPropInfoBean {
        /**
         * prevPropName : 幸运袜
         * prevPropIcon : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/giftTask/1607052033000*giftTask714336577.png
         * prevPropQuantity : 143
         * propName : 许愿棒
         * propIcon : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/giftTask/1607052033000*giftTask714336577.png
         * propQuantity : 143
         */

        private String prevPropName;
        private String prevPropIcon;
        private int prevPropQuantity;
        private String propName;
        private String propIcon;
        private int propQuantity;

        public String getPrevPropName() {
            return prevPropName;
        }

        public void setPrevPropName(String prevPropName) {
            this.prevPropName = prevPropName;
        }

        public String getPrevPropIcon() {
            return prevPropIcon;
        }

        public void setPrevPropIcon(String prevPropIcon) {
            this.prevPropIcon = prevPropIcon;
        }

        public int getPrevPropQuantity() {
            return prevPropQuantity;
        }

        public void setPrevPropQuantity(int prevPropQuantity) {
            this.prevPropQuantity = prevPropQuantity;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public String getPropIcon() {
            return propIcon;
        }

        public void setPropIcon(String propIcon) {
            this.propIcon = propIcon;
        }

        public int getPropQuantity() {
            return propQuantity;
        }

        public void setPropQuantity(int propQuantity) {
            this.propQuantity = propQuantity;
        }
    }
}
