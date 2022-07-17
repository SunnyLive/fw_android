package com.fengwo.module_comment.widget.BannerView.act;

import com.fengwo.module_comment.bean.ActBannerBean;

public interface OnActBannerItemCliakListenr {
    void integralJump(String url);
    void wishJump(int wishStatus);
    void signJump(int id);
    void normalActJump(String url);
    void actTips(String content);
    void panishJump(int id);
    void showPlaneMp4(int id);
    void openBox(String url);
    void actWSJ();
    void actRedBoxRain(int type);
    void endActivitiy();
    void actSDJ(ActBannerBean bannerBean);
    void actSDJRedBoxRain();
    void actNewYear(ActBannerBean bannerBean);

}
