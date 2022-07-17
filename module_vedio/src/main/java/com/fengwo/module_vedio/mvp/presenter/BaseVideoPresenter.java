package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.mvp.ui.iview.IBaseVideoView;

public class BaseVideoPresenter<V extends IBaseVideoView> extends BasePresenter<V> {

    public VedioApiService service;

    public BaseVideoPresenter() {
        service = new RetrofitUtils().createApi(VedioApiService.class);
    }


}
