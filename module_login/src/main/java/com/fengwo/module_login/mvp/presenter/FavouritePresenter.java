package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.mvp.ui.iview.IFavouriteView;

public class FavouritePresenter extends BasePresenter<IFavouriteView> {

    public void getVedioData(int page) {
        getView().setVideoData(null, page);
    }

    public void getShopData(int page) {
        getView().setShopData(null, page);
    }
}
