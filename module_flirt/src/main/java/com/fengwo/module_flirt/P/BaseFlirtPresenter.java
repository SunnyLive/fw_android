package com.fengwo.module_flirt.P;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_flirt.api.FlirtApiService;

public class BaseFlirtPresenter<V extends MvpView> extends BasePresenter<V> {
    FlirtApiService service;

    public BaseFlirtPresenter() {
        service = new RetrofitUtils().createWenboApi(FlirtApiService.class);
    }

}
