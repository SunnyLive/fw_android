package com.fengwo.module_live_vedio.serviceimpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.iservice.UserMedalService;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/11
 */
@Route(path = ArouterApi.GET_USER_MEDAL_SERVICE, name = "获取用户勋章服务")
public class UserMedalServiceImpl implements UserMedalService {
    LiveApiService service;
    @Override
    public void getUserMedal(int uid,int channelId, LoadingObserver<HttpResult<UserMedalBean>> loadingObserver) {
        Map map = new HashMap();
        map.put("userId",uid);
        map.put("channelId",channelId);
        String json = new Gson().toJson(map);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        service.getUserMedalInfo(uid)
                .compose(RxUtils.applySchedulers())
                .subscribe(loadingObserver);
    }

    @Override
    public void init(Context context) {
        service = new RetrofitUtils().createApi(LiveApiService.class);
    }
}
