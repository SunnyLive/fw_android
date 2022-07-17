package com.fengwo.module_login.serviceImpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.IBackpackService;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_comment.bean.BackpackDto;

@Route(path = ArouterApi.BACKPACK_SERVICE,name = "这个是我的背包通用接口")
public class BackpackServiceImpl implements IBackpackService {

    @Override
    public void getBackpack(LoadingObserver<HttpResult<BackpackDto>> l) {
        new RetrofitUtils().createApi(LoginApiService.class)
                .getBackpack().compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<BackpackDto>>() {
                    @Override
                    public void _onNext(HttpResult<BackpackDto> data) {
                        l._onNext(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        l._onError(msg);
                    }
                });
    }

    @Override
    public void init(Context context) {

    }
}
