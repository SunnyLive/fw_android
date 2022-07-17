package com.fengwo.module_main.mvp.api;

import com.fengwo.module_comment.api.API;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.VensionDto;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/17
 */
public interface MainApiService {

    @GET(API.APP_VERSION)
    Flowable<HttpResult<VensionDto>> getAppVension();

    @GET(Api.GIFT_LIST)
    Flowable<HttpResult<List<String>>> getGiftList();

    @POST(Api.LOGIN_LOG)
    Flowable<HttpResult> loginlog(@Body RequestBody params);

    @POST(Api.USER_OFFLINE_LOG)
    Flowable<HttpResult> userOfflineLog();

    @POST(Api.ADD_VISITOR_RECORD)
    Flowable<HttpResult> addVisitorRecord(@Body RequestBody body);
}
