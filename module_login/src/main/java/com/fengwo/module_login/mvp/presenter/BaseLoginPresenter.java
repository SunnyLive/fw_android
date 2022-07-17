package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_login.api.LoginApiService;

public class BaseLoginPresenter<V extends MvpView> extends BasePresenter<V> {

    public LoginApiService service;

    public BaseLoginPresenter() {
        service = new RetrofitUtils().createApi(LoginApiService.class);
    }


}
