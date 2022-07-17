package com.fengwo.module_comment.bean;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/7/8 11:17
 */
public class AnchorWishBean {

    /**
     * anchorId : 0
     * createTime : 2020-07-08T03:16:45.885Z
     * devoteUser : string
     * expireTime : 2020-07-08T03:16:45.885Z
     * factQuantity : 0
     * giftId : 0
     * giftName : string
     * icon : string
     * id : 0
     * isGenerated : 0
     * nickname : string
     * progress : string
     * repay : 0
     * repayName : string
     * status : 0
     * updateTime : 2020-07-08T03:16:45.885Z
     * wishDevoteUserVOList : [{"headImg":"string","rank":0,"userId":0}]
     * wishQuantity : 0
     * wishType : 0
     * wishTypeName : string
     */

    public int anchorId;
    public String createTime;
    public String devoteUser;
    public String expireTime;
    public int factQuantity;
    public int giftId;
    public String giftName;
    public String icon;
    public int id;
    public int isGenerated;
    public String nickname;
    public String progress;
    public int repay;//报答方式 1：自行协商 2：唱歌 3：跳舞
    public String repayName;
    public int status;//状态：0：未完成 1：已完成
    public String updateTime;
    public int wishQuantity;
    public int wishType; //心愿类型 1：本日 2：本周 3：本月
    public String wishTypeName;
    public List<WishDevoteUserVOListBean> wishDevoteUserVOList;

    public static class WishDevoteUserVOListBean {
        public String headImg;
        public int rank;
        public int userId;
    }

    @Override
    public String toString() {
        return "AnchorWishBean{" +
                "anchorId=" + anchorId +
                ", createTime='" + createTime + '\'' +
                ", devoteUser='" + devoteUser + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", factQuantity=" + factQuantity +
                ", giftId=" + giftId +
                ", giftName='" + giftName + '\'' +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                ", isGenerated=" + isGenerated +
                ", nickname='" + nickname + '\'' +
                ", progress='" + progress + '\'' +
                ", repay=" + repay +
                ", repayName='" + repayName + '\'' +
                ", status=" + status +
                ", updateTime='" + updateTime + '\'' +
                ", wishQuantity=" + wishQuantity +
                ", wishType=" + wishType +
                ", wishTypeName='" + wishTypeName + '\'' +
                ", wishDevoteUserVOList=" + wishDevoteUserVOList +
                '}';
    }
}
