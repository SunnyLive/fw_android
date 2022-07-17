package com.fengwo.module_comment.api;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_comment.bean.RedPacketConfigBean;
import com.fengwo.module_comment.bean.ZhuboDto;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CommentService {

    //退出直播间
    @POST(API.BUNKO_2030016)
    Flowable<HttpResult> quitRoom(@Body RequestBody body);

    //检测主播直播状态
    @POST(API.BUNKO_2029019)
    Flowable<HttpResult<CheckAnchorStatus>> checkAnchorStatus(@Body RequestBody body);

    @POST(API.LEAVE_LIVINGROOM)
    Flowable<HttpResult> leaveLivingRoom(@Path("channelId") int channelId);

    /**
     * 获取红包配置
     *
     * @return
     */
    @GET(API.GET_RED_PACKET_CONFIG)
    Flowable<HttpResult<RedPacketConfigBean>> getRedPacketConfig();

    /**
     * 判断当前用户是否为官方号
     *
     * @return
     */
    @GET(API.GET_CHECK_OFFICIAL_ACCOUNT)
    Flowable<HttpResult<Boolean>> checkOfficialAccount();


    @GET(API.GET_LIVE_ZHUBO_LIST)
    Flowable<HttpResult<BaseListDto<ZhuboDto>>> getZhuboList(@Query("pageParam") String pageParam, @Query("menuId") int menuId);
}
