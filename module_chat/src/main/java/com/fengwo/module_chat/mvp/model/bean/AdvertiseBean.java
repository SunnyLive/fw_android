package com.fengwo.module_chat.mvp.model.bean;

import com.fengwo.module_comment.widget.BannerView.BannerView;
import com.fengwo.module_comment.widget.BannerView.IBanner;

public class AdvertiseBean implements IBanner {
    public String advertName;
    public String contentUrl;
    public String createTime;
    public String descrip;
    public String endTime;
    public String id;
    public String image;
    public String title;

    public String getAdvertName() {
        return advertName;
    }

    public void setAdvertName(String advertName) {
        this.advertName = advertName;
    }

    public int modelType; // 所属模块分类:1直播、2社交、3短视频 4.文博
    public int objectType; // 项目类型：1app、2pc、3公会后台
    public int contentType; // 内容类型：1网页、2视频
    public int pageType;
    public int status;

    @Override
    public String getImgUrl() {
        return image;
    }

    @Override
    public String getJumpUrl() {
        return contentUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isOut() {
        return false;
    }

    @Override
    public boolean isImg() {
        return true;
    }
}
