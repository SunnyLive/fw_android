package com.fengwo.module_comment.widget.BannerView.act;

public interface IActBanner {
    String getImgUrl();

    String getJumpUrl();

    String getTitle();

    boolean isOut();

    int getType(); //活动类型 类型 1：积分 2：心愿 3：签到
}
