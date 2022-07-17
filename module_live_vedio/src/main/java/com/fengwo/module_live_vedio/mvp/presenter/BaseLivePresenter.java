package com.fengwo.module_live_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;

public class BaseLivePresenter<V extends MvpView> extends BasePresenter<V> {
    LiveApiService service;

    public BaseLivePresenter() {
        service = new RetrofitUtils().createApi(LiveApiService.class);
    }

}
