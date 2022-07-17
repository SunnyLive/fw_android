package com.fengwo.module_login.serviceImpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.ComplaintReplyService;
import com.fengwo.module_comment.iservice.SysConfigService;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_login.api.LoginApiService;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/8
 */
@Route(path = ArouterApi.SYS_CONFIG_SERVICE, name = "系统服务")
public class SysConfigServiceImpl implements SysConfigService {
    @Override
    public void init(Context context) {

    }

    @Override
    public void getSysConfig(String key, LoadingObserver<HttpResult> observer) {
        new RetrofitUtils().createApi(LoginApiService.class)
                .getSysConfig(key)
                .compose(RxUtils.applySchedulers())
                .subscribe(observer);
    }
}
