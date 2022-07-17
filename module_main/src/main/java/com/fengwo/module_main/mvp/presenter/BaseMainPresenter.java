package com.fengwo.module_main.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_main.mvp.api.MainApiService;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/17
 */
public class BaseMainPresenter<V extends MvpView> extends BasePresenter<V> {

    MainApiService service;

    public BaseMainPresenter() {
       service =  new RetrofitUtils().createApi(MainApiService.class);
    }
}
