package com.fengwo.module_flirt.serviceimpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Interfaces.IRealNameInterceptService;
import com.fengwo.module_flirt.utlis.CommonUtils;

@Route(path = ArouterApi.GET_REAL_NAME_INTERCEPT_SERVICE, name = "实名认证的逻辑")
public class RealNameInterceptImpl implements IRealNameInterceptService {

    @Override
    public void showRealName(Context c, int type, boolean isSetting) {
        CommonUtils.playing(c, type, isSetting);
    }

    @Override
    public void init(Context context) {

    }
}
