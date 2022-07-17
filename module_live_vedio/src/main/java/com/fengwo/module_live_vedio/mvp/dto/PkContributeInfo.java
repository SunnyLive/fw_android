package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/14
 */
public class PkContributeInfo {

    /**
     * pkId : 1282944544421187586
     * anchorId : 500874
     * contributionRank : [{"userId":500881,"nickname":"小洪🌹","headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1581750447917.jpg?x-oss-process=style/500x500_watermark","userRank":1,"isGuard":false}]
     * contributionGift : [{"giftId":28,"giftName":"赤焰红唇","giftIcon":"http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/gift/28.png","giftNumber":1}]
     * userId : 500881
     * nickname : 小洪🌹
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1581750447917.jpg?x-oss-process=style/500x500_watermark
     * userContribution : 20
     */

    private String pkId;
    private int anchorId;
    private int userId;
    private String nickname;
    private String headImg;
    private int userContribution;
    private List<ContributionRankBean> contributionRank;
    private List<ContributionGiftBean> contributionGift;

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public int getUserContribution() {
        return userContribution;
    }

    public void setUserContribution(int userContribution) {
        this.userContribution = userContribution;
    }

    public List<ContributionRankBean> getContributionRank() {
        return contributionRank;
    }

    public void setContributionRank(List<ContributionRankBean> contributionRank) {
        this.contributionRank = contributionRank;
    }

    public List<ContributionGiftBean> getContributionGift() {
        return contributionGift;
    }

    public void setContributionGift(List<ContributionGiftBean> contributionGift) {
        this.contributionGift = contributionGift;
    }

    public static class ContributionRankBean {
        /**
         * userId : 500881
         * nickname : 小洪🌹
         * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1581750447917.jpg?x-oss-process=style/500x500_watermark
         * userRank : 1
         * isGuard : false
         */

        private int userId;
        private String nickname;
        private String headImg;
        private int userRank;
        private boolean isGuard;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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

        public int getUserRank() {
            return userRank;
        }

        public void setUserRank(int userRank) {
            this.userRank = userRank;
        }

        public boolean isIsGuard() {
            return isGuard;
        }

        public void setIsGuard(boolean isGuard) {
            this.isGuard = isGuard;
        }
    }

    public static class ContributionGiftBean {
        /**
         * giftId : 28
         * giftName : 赤焰红唇
         * giftIcon : http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/gift/28.png
         * giftNumber : 1
         */

        private int giftId;
        private String giftName;
        private String giftIcon;
        private int giftNumber;

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

        public String getGiftIcon() {
            return giftIcon;
        }

        public void setGiftIcon(String giftIcon) {
            this.giftIcon = giftIcon;
        }

        public int getGiftNumber() {
            return giftNumber;
        }

        public void setGiftNumber(int giftNumber) {
            this.giftNumber = giftNumber;
        }
    }
}
