package com.fengwo.module_comment.base;

import com.fengwo.module_comment.widget.BannerView.IBanner;

public class BannerBean implements IBanner {
    public String title;
    public int id;
    public int type;
    public String url;
    public String enterUrl;
    public int isOut;


    @Override
    public String getImgUrl() {
        return url;
    }

    @Override
    public String getJumpUrl() {
        return enterUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isOut() {
        return (isOut == 1);
    }

    @Override
    public boolean isImg() {
        return true;
    }


}
