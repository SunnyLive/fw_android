package com.fengwo.module_live_vedio.api;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.Param;
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackageOrderData;
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackagePerson;
import com.fengwo.module_live_vedio.mvp.dto.redpackage.SendRedPackageParam;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 红包API
 *
 * @Author gukaihong
 * @Time 2021/1/6
 */
public interface RedPackageApiService {
    /**
     * 发送红包
     *
     * @param params 参数
     * @return
     */
    @POST(API.POST_RED_PACKAGE_SEND)
    Flowable<HttpResult<String>> postRedPackageSend(@Body Param<SendRedPackageParam> params);

    /**
     * 我的记录
     *
     * @param anchorId    主播id
     * @param redpacketId 红包id
     * @return
     */
    @GET(API.GET_RED_PACKAGE_MY_STATISTICS)
    Flowable<HttpResult<RedPackagePerson>> getRedPackageSend(@Query("anchorId") String anchorId, @Query("redpacketId") String redpacketId);

    /**
     * 我的收益
     *
     * @param params 参数
     * @return
     */
    @POST(API.POST_RED_PACKAGE_ORDER_IN)
    Flowable<HttpResult<RedPackageOrderData>> postOrderIn(@Body() RequestBody params);

    /**
     * 我的发出的记录
     *
     * @param params 参数
     * @return
     */
    @POST(API.POST_RED_PACKAGE_ORDER_OUT)
    Flowable<HttpResult<RedPackageOrderData>> postOrderOut(@Body() RequestBody params);
}
