package com.fengwo.module_chat.mvp.model.bean;

import java.util.List;

public class MerchantListBoean {
    public int addUserId;
    public int approvalUser;
    public int auditStatus; // 审核状态：0审核中，1成功，2驳回
    public String averageConsumption;// 人均消费
    public String buildTime;
    public String city;
    public double cleanScore;
    public int commentNum;
    public String commentTagIds;
    public String cover;
    public double compreScore;
    public String createTime;
    public int divideId;
    public String divideName;
    public double environmentScore;
    public int id;
    public String images;
    public String introduce;
    public String latitude;
    public String longitude;
    public String merchantCircle;
    public String merchantDesc;
    public String merchantName;
    public String merchantPosition;
    public int picImgNum;
    public int picLowScoreNum;
    public int picNum;
    public double positionScore;
    public String recommend;
    public String remark;
    public double serviceScore;
    public int status;
    public String tagIds;
    public String updateTime;
    public String url;
    public List<MerchantTagModel> merchantTagList;

    public static class MerchantTagModel{
        public String city;
        public String createTime;
        public int divideId;
        public int id;
        public int status; // 状态：1正常，0删除
        public String tagColor;
        public String tagName;
        public String times;
    }
}
