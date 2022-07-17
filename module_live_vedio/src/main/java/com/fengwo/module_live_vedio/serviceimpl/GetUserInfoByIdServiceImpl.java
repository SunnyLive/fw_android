package com.fengwo.module_live_vedio.serviceimpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.GetUserInfoByIdService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

@Route(path = ArouterApi.GET_USERINFO_SERVICE, name = "获取用户信息服务")
public class GetUserInfoByIdServiceImpl implements GetUserInfoByIdService {
    LiveApiService service;

    @Override
    public void getUserInfoById(String uid, LoadingObserver<HttpResult<UserInfo>> observer) {
        int userId = Integer.parseInt(uid) << 3;
        Map map = new HashMap();
        map.put("userId", userId);
        service.getUserinfoById(createRequestBody(map))
                .compose(RxUtils.applySchedulers())
                .subscribe(observer);
    }

    @Override
    public void getEachAttention(String uid, LoadingObserver<HttpResult<BaseEachAttention>> observer) {
        try {
            int id = Integer.parseInt(uid);
            service.getEachAttention(id)
                    .compose(RxUtils.applySchedulers())
                    .subscribe(observer);
        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }


    @Override
    public void init(Context context) {
        service = new RetrofitUtils().createApi(LiveApiService.class);
    }

    public RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }
}
