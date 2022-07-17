package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface IFavouriteView extends MvpView {
    void setVideoData(List<String> data,int page);
    void setShopData(List<String> data,int page);
}
