package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/10/23
 */
public class GetActivityInfoDto {

    /**
     * currentIndex : null
     * isFinish : false
     * processDtVOList : [{"name":null,"pic":null,"status":1,"finishTime":null,"currentTime":"1603452075483","currentGiftNum":0,"targetGiftNum":9,"giftItemList":[{"name":"童话城堡","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502968000*gift7554630688.png","status":1,"currentGiftNum":0,"targetGiftNum":5},{"name":"风驰超跑","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502947000*gift7397793466.png","status":1,"currentGiftNum":0,"targetGiftNum":3},{"name":"一见倾心","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502930000*gift7106200420.png","status":1,"currentGiftNum":0,"targetGiftNum":1}]},{"name":null,"pic":null,"status":1,"finishTime":null,"currentTime":"1603452075483","currentGiftNum":0,"targetGiftNum":24,"giftItemList":[{"name":"童话城堡","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502968000*gift7554630688.png","status":1,"currentGiftNum":0,"targetGiftNum":10},{"name":"风驰超跑","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502947000*gift7397793466.png","status":1,"currentGiftNum":0,"targetGiftNum":8},{"name":"一见倾心","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502930000*gift7106200420.png","status":1,"currentGiftNum":0,"targetGiftNum":6}]},{"name":null,"pic":null,"status":1,"finishTime":null,"currentTime":"1603452075483","currentGiftNum":0,"targetGiftNum":47,"giftItemList":[{"name":"童话城堡","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502968000*gift7554630688.png","status":1,"currentGiftNum":0,"targetGiftNum":20},{"name":"风驰超跑","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502947000*gift7397793466.png","status":1,"currentGiftNum":0,"targetGiftNum":15},{"name":"一见倾心","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502930000*gift7106200420.png","status":1,"currentGiftNum":0,"targetGiftNum":12}]}]
     */

    private Object currentIndex;
    private boolean isFinish;
    private List<ProcessDtVOListBean> processDtVOList;

    public Object getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Object currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean isIsFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public List<ProcessDtVOListBean> getProcessDtVOList() {
        return processDtVOList;
    }

    public void setProcessDtVOList(List<ProcessDtVOListBean> processDtVOList) {
        this.processDtVOList = processDtVOList;
    }

    public static class ProcessDtVOListBean {
        /**
         * name : null
         * pic : null
         * status : 1
         * finishTime : null
         * currentTime : 1603452075483
         * currentGiftNum : 0
         * targetGiftNum : 9
         * giftItemList : [{"name":"童话城堡","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502968000*gift7554630688.png","status":1,"currentGiftNum":0,"targetGiftNum":5},{"name":"风驰超跑","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502947000*gift7397793466.png","status":1,"currentGiftNum":0,"targetGiftNum":3},{"name":"一见倾心","pic":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502930000*gift7106200420.png","status":1,"currentGiftNum":0,"targetGiftNum":1}]
         */

        private String name;
        private String pic;
        private int status;
        private String finishTime;
        private String currentTime;
        private int currentGiftNum;
        private int targetGiftNum;
        private List<GiftItemListBean> giftItemList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public int getCurrentGiftNum() {
            return currentGiftNum;
        }

        public void setCurrentGiftNum(int currentGiftNum) {
            this.currentGiftNum = currentGiftNum;
        }

        public int getTargetGiftNum() {
            return targetGiftNum;
        }

        public void setTargetGiftNum(int targetGiftNum) {
            this.targetGiftNum = targetGiftNum;
        }

        public List<GiftItemListBean> getGiftItemList() {
            return giftItemList;
        }

        public void setGiftItemList(List<GiftItemListBean> giftItemList) {
            this.giftItemList = giftItemList;
        }

        public static class GiftItemListBean {
            /**
             * name : 童话城堡
             * pic : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1598502968000*gift7554630688.png
             * status : 1
             * currentGiftNum : 0
             * targetGiftNum : 5
             */

            private String name;
            private String pic;
            private int status;
            private int currentGiftNum;
            private int targetGiftNum;

            public GiftDto giftInfo;

            public GiftDto getGiftItemList() {
                return giftInfo;
            }

            public void setGiftItemList(GiftDto giftInfo) {
                this.giftInfo = giftInfo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getCurrentGiftNum() {
                return currentGiftNum;
            }

            public void setCurrentGiftNum(int currentGiftNum) {
                this.currentGiftNum = currentGiftNum;
            }

            public int getTargetGiftNum() {
                return targetGiftNum;
            }

            public void setTargetGiftNum(int targetGiftNum) {
                this.targetGiftNum = targetGiftNum;
            }
        }
    }
}
