package com.fengwo.module_login.api;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.bean.GreetTipsBean;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_login.mvp.dto.AllCarDto;
import com.fengwo.module_login.mvp.dto.AttentionNumberDto;
import com.fengwo.module_comment.bean.BackpackDto;
import com.fengwo.module_login.mvp.dto.BankBindStatusDTO;
import com.fengwo.module_login.mvp.dto.BankDto;
import com.fengwo.module_login.mvp.dto.BindBankDTO;
import com.fengwo.module_login.mvp.dto.BindCardDto;
import com.fengwo.module_login.mvp.dto.BlackDto;
import com.fengwo.module_login.mvp.dto.CashOutDto;
import com.fengwo.module_login.mvp.dto.ComplaintRecordDto;
import com.fengwo.module_login.mvp.dto.ComplaintsDto;
import com.fengwo.module_login.mvp.dto.FansDto;
import com.fengwo.module_login.mvp.dto.FansNumberDto;
import com.fengwo.module_login.mvp.dto.FriendsNumberDto;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.dto.GonghuiListDto;
import com.fengwo.module_login.mvp.dto.GuardDto;
import com.fengwo.module_login.mvp.dto.HuafenLevelDto;
import com.fengwo.module_login.mvp.dto.IsRealNameDto;
import com.fengwo.module_login.mvp.dto.LiveLengthDto;
import com.fengwo.module_login.mvp.dto.LiveStatusDto;
import com.fengwo.module_login.mvp.dto.LoginDto;
import com.fengwo.module_login.mvp.dto.MCD_CommentDto;
import com.fengwo.module_login.mvp.dto.MineCardDetailDto;
import com.fengwo.module_login.mvp.dto.MineCardDto;
import com.fengwo.module_login.mvp.dto.MineMovieDto;
import com.fengwo.module_login.mvp.dto.MyCarDto;
import com.fengwo.module_login.mvp.dto.MyPrivilegeDto;
import com.fengwo.module_login.mvp.dto.MyTaskDto;
import com.fengwo.module_login.mvp.dto.MyUsingCarDto;
import com.fengwo.module_login.mvp.dto.NickNameRuleBean;
import com.fengwo.module_login.mvp.dto.NobilityListDTO;
import com.fengwo.module_login.mvp.dto.NobilityTypeDTO;
import com.fengwo.module_login.mvp.dto.OrderDto;
import com.fengwo.module_login.mvp.dto.PrivilegeDto;
import com.fengwo.module_login.mvp.dto.ProfitDetailDto;
import com.fengwo.module_login.mvp.dto.ProfitDto;
import com.fengwo.module_login.mvp.dto.RankLevelDto;
import com.fengwo.module_login.mvp.dto.RealNameFailedDto;
import com.fengwo.module_login.mvp.dto.ReceiveGiftDto;
import com.fengwo.module_login.mvp.dto.RechargeDto;
import com.fengwo.module_login.mvp.dto.RecordDto;
import com.fengwo.module_login.mvp.dto.ReportLabelDto;
import com.fengwo.module_login.mvp.dto.SendGiftHistoryDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;
import com.fengwo.module_login.mvp.dto.TagsDto;
import com.fengwo.module_login.mvp.dto.TaskDto;
import com.fengwo.module_login.mvp.dto.UnionInfo;
import com.fengwo.module_login.mvp.dto.UserDetailDto;
import com.fengwo.module_login.mvp.dto.VedioNumberDto;
import com.fengwo.module_login.mvp.dto.VerifyResultDto;
import com.fengwo.module_login.mvp.dto.VerifyTokenDto;
import com.fengwo.module_login.mvp.dto.WalletDto;
import com.fengwo.module_login.mvp.dto.WatchHistoryDto;
import com.fengwo.module_login.mvp.dto.WithDrawDto;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginApiService {

    @POST(API.SEND_CODE)
    Flowable<HttpResult> getCode(@Header("nonce") String nonce, @Header("sign") String sign, @Body RequestBody params);

    @PUT(API.LOGIN)
    Flowable<HttpResult<LoginDto>> login(@Body RequestBody params);

    @PUT(API.AUTO_LOGIN)
    Flowable<HttpResult<LoginDto>> autoLogin(@Body RequestBody params);

    @PUT(API.BIND_MOBILE)
    Flowable<HttpResult<LoginDto>> bindMobile(@Body RequestBody params);

    @PUT(API.THIRD_LOGIN)
    Flowable<HttpResult<LoginDto>> thirdLogin(@Body RequestBody params);

    @GET(API.GET_USER_INFO)
    Flowable<HttpResult<UserInfo>> getUserInfo();

    @GET(API.GET_USER_FANSNUMBER)
    Flowable<HttpResult<FansNumberDto>> getUserFansNumber();

    @GET(API.GET_USER_ATTENTIONS)
    Flowable<HttpResult<AttentionNumberDto>> getUserAttentionNumber();

    @GET(API.GET_USER_FRIENDS)
    Flowable<HttpResult<FriendsNumberDto>> getUserFriendNumber();

    @GET(API.GET_HUAFEN_LEVEL)
    Flowable<HttpResult<HuafenLevelDto>> getHuafenLevel();

    @GET(API.IS_REALNAME)
    Flowable<HttpResult<IsRealNameDto>> getIsRealName();

    @GET(API.GET_ALL_TASK)
    Flowable<HttpResult<List<TaskDto>>> getAllTask();

    @GET(API.GET_MY_TASK)
    Flowable<HttpResult<List<MyTaskDto>>> getMyTask();

    @GET(API.GET_ALL_PRIVILEGE)
    Flowable<HttpResult<List<PrivilegeDto>>> getPrivilege();

    @GET(API.GET_MY_PRIVELEGE)
    Flowable<HttpResult<MyPrivilegeDto>> getMyPrivilege();

    @GET(API.FANS_LIST)
    Flowable<HttpResult<BaseListDto<FansDto>>> getFansList(@Query("pageParam") String page, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("orderBy") int orderBy);

    @GET(API.ATTENTION_LIST)
    Flowable<HttpResult<BaseListDto<FansDto>>> getAttentionList(@Query("pageParam") String page, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("orderBy") int orderBy);

    @GET(API.FRIENDS_LIST)
    Flowable<HttpResult<BaseListDto<FansDto>>> getFriendList(@Query("pageParam") String page, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("orderBy") int orderBy);

    @POST(API.REAL_NAME)
    Flowable<HttpResult> realName(@Body RequestBody params);

    @POST(API.REAL_IDCARD)
    Flowable<HttpResult> realIdCard(@Body RequestBody params);

    @GET(API.REAL_NAME_FAILED_REASON)
    Flowable<HttpResult<RealNameFailedDto>> realNameFailedReason();

    @GET(API.GET_RECHARGE_LIST)
    Flowable<HttpResult<List<RechargeDto>>> getRechargeList(@Query("pageParam") String page);

    @GET(API.GET_RECHARGE_RECORDS)
    Flowable<HttpResult<BaseListDto<RecordDto>>> getRechargeRecords(@Query("pageParam") String page);

    @GET(API.GET_WITHDRAW_RECORDS)
    Flowable<HttpResult<BaseListDto<WithDrawDto>>> getWithDrawRecords(@Query("pageParam") String page);

    @GET(API.GET_BANK_LIST)
    Flowable<HttpResult<List<BankDto>>> getBankList();

    @POST(API.BIND_CARD)
    Flowable<HttpResult<BindCardDto>> bindCard(@Body RequestBody params);

    @GET(API.GET_MINE_SMALL_VEDIO)
    Flowable<HttpResult<VedioNumberDto>> getMineSmallVedio();

    @GET(API.GET_MINE_SHORT_VEDIO)
    Flowable<HttpResult<VedioNumberDto>> getMineShortVedio();

    @GET(API.GET_MINE_SMALL_ZAN_VEDIO)
    Flowable<HttpResult<VedioNumberDto>> getMineZanVedio();

    @GET(API.GET_WALLET_INFO)
    Flowable<HttpResult<WalletDto>> getWalletInfo();

    @PUT(API.LOGOUT)
    Flowable<HttpResult> logout();

    @POST(API.BLACK_LIST)
    Flowable<HttpResult<BaseListDto<BlackDto>>> getBlackList(@Query("pageParam") String pageParam);

    @DELETE(API.DELETE_BLACK)
    Flowable<HttpResult> deleteBlack(@Path("id") int userId);

    @PUT(API.UPDATE_USERINFO)
    Flowable<HttpResult> updateUserinfo(@Body RequestBody params);

    @POST(API.USER_ADD_PHOTOS)
    Flowable<HttpResult> userAddPhotos(@Body RequestBody params);

    @PUT(API.USER_UPDATE_PHOTOS)
    Flowable<HttpResult> useUpdatePhotos(@Body RequestBody params);

    @PUT(API.USER_UPDATE_PHOTOS_ORDER)
    Flowable<HttpResult> useUpdatePhotosOrder(@Body RequestBody params);

    @DELETE(API.USER_DELETE_PHOTOS)
    Flowable<HttpResult> userDeletePhotos(@Path("id") int id);


    @GET(API.ALL_CAR_LIST)
    Flowable<HttpResult<List<AllCarDto>>> getAllCarList();

    @GET(API.MY_CAR_LIST)
    Flowable<HttpResult<MyCarDto>> getMyCarList();

    @GET(API.MY_USING_CAR)
    Flowable<HttpResult<MyUsingCarDto>> getMyUsingCar();

    @GET(API.BUY_CAR)
    Flowable<HttpResult<PopoDto>> buyCar(@Query("id") String id);

    @GET(API.OPEN_CAR)
    Flowable<HttpResult> openCar(@Query("id") int id, @Query("type") int type);

    @GET(API.CAR_DETAIL)
    Flowable<HttpResult<AllCarDto>> getCarDetail(@Path("id") int id);

    @GET(API.MY_PROFIT)
    Flowable<HttpResult<ProfitDto>> getMyProfit();

    @GET(API.MY_PROFIT_DETAIL)
    Flowable<HttpResult<BaseListDto<ProfitDetailDto>>> getProfitDetail(@Query("pageParam") String pageParam, @Query("start") String start, @Query("end") String end);

    @GET(API.GUARD_ME)
    Flowable<HttpResult<BaseListDto<GuardDto>>> getGuardMeList(@Query("pageParam") String pageParam);

    @GET(API.MY_GUARD)
    Flowable<HttpResult<BaseListDto<GuardDto>>> getMyGuardList(@Query("pageParam") String pageParam);

    @GET(API.GET_USER_CENTER)
    Flowable<HttpResult<UserInfo>> getUserCenter();

    @POST(API.BUY_ORDER)
    Flowable<HttpResult<OrderDto>> getOrder(@Query("id") int id, @Body RequestBody params);

    @GET(API.CASH_OUT_PAGE)
    Flowable<HttpResult<CashOutDto>> getCashOutPageData();

    @GET(API.CASH_OUT_COMMIT)
    Flowable<HttpResult> cashOutCommit(@Query("money") int money);

    @POST(API.LIVE_LENGTH)
    Flowable<HttpResult<LiveLengthDto>> getLiveLength(@Query("pageParam") String pageParam, @Body RequestBody body);

    @GET(API.WATCHHISTORY)
    Flowable<HttpResult<BaseListDto<WatchHistoryDto>>> getWatchHistory(@Query("pageParam") String pageParam);

    @POST(API.WATCHHISTORY)
    Flowable<HttpResult> delWatchHistory();

    @GET(API.RECEIVEGIFT)
    Flowable<HttpResult<ReceiveGiftDto>> getReceiveGift(@Query("pageParam") String pageParam, @Query("type") int type);

    @POST(API.COMPLAINT_COMMIT)
    Flowable<HttpResult> complaintCommit(@Body RequestBody params);

    @GET(API.SEND_GIFT_HISTORY)
    Flowable<HttpResult<BaseListDto<SendGiftHistoryDto>>> getSendGiftHistory(@Query("pageParam") String pageParam);

    @GET(API.REPORT_LABEL)
    Flowable<HttpResult<BaseListDto<ReportLabelDto>>> getReportLabel();

    @POST(API.REPORT_COMMIT)
    Flowable<HttpResult> rePortCommit(@Body RequestBody params);

    @GET(API.GET_USER_DETAIL)
    Flowable<HttpResult<UserDetailDto>> getUserDetail(@Query("userId") String id);

    @GET(API.GET_RANK_LEVEL)
    Flowable<HttpResult<RankLevelDto>> getRankLevel();

    @GET(API.GET_RECEIVE_LIST)
    Flowable<HttpResult<LiveProfitDto>> getReceiveList(@Query("pageParam") String pageParam, @Query("type") int type, @Query("userId") int hostId);

    @GET(API.GET_NOBILITY_TYPE_LIST)
    Flowable<HttpResult<List<NobilityTypeDTO>>> getNobilityTypeList(@Query("type") String type);

    @GET(API.GET_NOBILITY_PRIVILEGE)
    Flowable<HttpResult<NobilityListDTO>> getNobilityPrivilege();

    @POST(API.BUY_NOBILITY)
    Flowable<HttpResult<PopoDto>> buyNobility(@Query("id") int id);

    @GET(API.GET_BANK_BIND_STATUS)
    Flowable<HttpResult<BankBindStatusDTO>> getBankBindStatus();

    @GET(API.GET_BIND_BANK)
    Flowable<HttpResult<BindBankDTO>> getBankInfo();

    @GET(API.GONGHUI_LIST)
    Flowable<HttpResult<BaseListDto<GonghuiListDto>>> getGonghui(@Query("pageParam") String params, @Query("queryParam") String quaryParam);

    @GET(API.APPLY_GONGHUI)
    Flowable<HttpResult> applyGonghui(@Query("familyUid") String familyUid);

    @GET(API.GET_USER_CARD)
    Flowable<HttpResult<BaseListDto<CircleListBean>>> getCardByUser(@Query("pageParam") String pageParam, @Query("userId") String userId);

    @GET(API.GET_USER_LIKE_CARD)
    Flowable<HttpResult<BaseListDto<CircleListBean>>> getLikeCardByUser(@Query("pageParam") String pageParam, @Query("userId") String userId);

    @GET(API.GET_USER_VEDIO)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getVedioByUser(@Query("pageParam") String pageParam, @Query("userId") String userId, @Query("order") int order);

    @GET(API.GET_USER_LIKE_VEDIO)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getLikeVedioByUser(@Query("pageParam") String pageParam, @Query("userId") String userId);

    @GET(API.GET_USER_MOVIE)
    Flowable<HttpResult<BaseListDto<MineMovieDto>>> getMovieByUser(@Path("id") String userId, @Query("pageParam") String pageParam);

    @GET(API.GET_USER_LIKE_MOVIE)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getLikeMovieByUser(@Query("userId") String userId, @Query("pageParam") String pageParam);

    @GET(API.GET_USER_LIVE_STATUS)
    Flowable<HttpResult<LiveStatusDto>> getLiveStatus(@Path("userId") String userId);

    @GET(API.GET_MY_COMPLAINT)
    Flowable<HttpResult<BaseListDto<ComplaintsDto>>> getMyComplaints(@Query("pageParam") String pageParam);

    @GET(API.GET_MODIFY_NAME_RULE)
    Flowable<HttpResult<NickNameRuleBean>> getNickNameRule();

    @GET(API.MODIFY_NAME)
    Flowable<HttpResult> modifyNickName(@Query("nickname") String nickname);

    @GET(API.COMPLAINT_RECORD)
    Flowable<HttpResult<BaseListDto<ComplaintRecordDto>>> getComPlaintRecord(@Path("id") int id, @Query("pageParam") String pp);

    @POST(API.COMPLAINT_REPLY)
    Flowable<HttpResult> complaintReply(@Body RequestBody requestBody);

    @GET(API.SYS_CONFIG)
    Flowable<HttpResult> getSysConfig(@Path("key") String key);

    @POST(API.GET_VIDEO_TIME)
    Flowable<HttpResult<LiveLengthDto>> getVideoTime(@Body RequestBody body);

    @POST(API.VIDEO_LENGTH)
    Flowable<HttpResult> getVideoLength();

    //上传经纬度
    @PUT(API.MODIFY_LNGLAT)
    Flowable<HttpResult> putLnglat(@Body RequestBody body);

    @POST(API.ADD_BLACK)
    Flowable<HttpResult> addBlack(@Path("id") int id);

    @POST(API.DELETE_MINE_CARD)
    Flowable<HttpResult> deleteMineCard(@Body RequestBody body);

    @POST(API.EXIT_LOG)
    Flowable<HttpResult> exitLog(@Body RequestBody params);

    @POST(API.ACC_DESTROY)
    Flowable<HttpResult> accDestroy(@Body RequestBody params);

    @POST(API.ACC_DESTROY_CANCEL)
    Flowable<HttpResult> accDestroyCancel(@Body RequestBody params);

    @GET(API.GET_MOBILE)
    Flowable<HttpResult> getMobile(@Path("userId") int userId);

    @POST(API.GREET_DISCUSS)
    Flowable<HttpResult> greetDiscuss(@Body RequestBody body);

    @POST(API.GREET_TIPS_DISCUSS)
    Flowable<HttpResult<BaseListDto<GreetTipsBean>>> greetTipsList(@Body RequestBody body);

    @GET(com.fengwo.module_live_vedio.api.API.GUARD_LIST_BY_ID)
    Flowable<HttpResult<GuardListDto>> getGuardList(@Query("pageParam") String pageParam, @Query("userId") int uid);

    /**
     * 查询标签集
     */
    @GET(API.GET_TAGS)
    Flowable<HttpResult<TagsDto>> getTags(@Path("tagTypeCode") String tagTypeCode);

    /**
     * 获取礼物墙
     */
    @GET(API.GET_GIFT_WALL)
    Flowable<HttpResult<GiftWallDto>> getGiftWall(@Query("pageParam") String pageParam, @Query("userId") int userId);


    /**
     * 登录/注册页面进入后设置用户信息
     * 保存用户头像和昵称到服务器
     */
    @POST(API.SAVE_USER_INFO)
    //Flowable<HttpResult> saveUserInfo(@Path("headImg") String headImg,@Path("nickname") String nickName);
    Flowable<HttpResult> saveUserInfo(@Body RequestBody params);

    /**
     * 跳过昵称头像的设置
     *
     * @return
     */
    @POST(API.SKIP_USER_INFO)
    Flowable<HttpResult> skipUserInfo();

    /**
     * 获取个人中心动态信息列表数据
     *
     * @param pageParam
     * @param userId
     * @return
     */
    @GET(API.GET_MINE_CARD_LIST)
    Flowable<HttpResult<MineCardDto>> getMineCardList(@Query("pageParam") String pageParam, @Query("userId") String userId);

    //获取分享信息/添加分享次数
    @GET(API.MINE_SHARE_CARD)
    Flowable<HttpResult<ShareUrlDto>> getShareInfo(@Path("id") int id);

    //点赞请求接口
    @PUT(API.MINE_CARD_LIKE)
    Flowable<HttpResult> getLikeInfo(@Path("id") int id);


    //获取个人中心动态详情
    @GET(API.MINE_CARD_DETAIL)
    Flowable<HttpResult<MineCardDetailDto>> getMineCardDetail(@Path("id") int id);

    //个人中心动态详情 置顶
    // cardId	integer($int32)
    //
    // state	integer($int32)
    @POST(API.MINE_CARD_DETAIL_STICK)
    Flowable<HttpResult<Boolean>> setMineCardDetailStick(@Body RequestBody params);

    //个人中心动态详情 权限管理
    //  "id": 0,
    //  "powerStatus": 0
    //
    //  id	integer($int32)
    //  动态id
    //
    //powerStatus	integer($int32)
    //权限状态 0 所有人可见 1 仅自己可见 2 仅好友可见
    @POST(API.MINE_CARD_DETAIL_AUTHORITY)
    Flowable<HttpResult<Boolean>> setMineCardDetailAuthority(@Body RequestBody params);


    //个人中心页面 -> 动态列表 -> 动态详情 -> 评论列表
    //id *
    //integer($int32)
    //(query)
    //卡片ID
    //
    //pageParam *
    //string
    //(query)
    @GET(API.GET_COMMENT_LIST)
    Flowable<HttpResult<BaseListDto<MCD_CommentDto>>> getMineCardComment(@Query("id") int id, @Query("pageParam") String pageParam);


    // 个人中心页面 -> 动态列表 -> 动态详情 -> 评论列表 -> 添加评论
    //
    // 添加评论
    //
    //cardId	integer($int32)
    //卡片ID
    //
    //content	string
    //内容
    //
    //parentId	integer($int32)
    //评论ID,一级评论父ID为0
    //
    //secondType	integer($int32)
    //2级评论类型（0为一级评论） 1：评论 2：回复
    //
    //secondUserId	integer($int32)
    //2级评论回复对象id（2级评论为回复类型填回复对象ID，其余填默认值0）
    //
    //type	integer($int32)
    //评论类型：1 一级 2 二级
    @POST(API.SEND_COMMENT)
    Flowable<HttpResult<MCD_CommentDto>> sendComment(@Body RequestBody params);

    // 个人中心页面 -> 动态列表 -> 动态详情 -> 评论列表 -> 添加评论
    //
    // Name	Description
    //
    // Authorization   string   (header)
    //
    // user token
    //
    // id * integer($int32) (path)评论ID
    //
    @PUT(API.LIKE_COMMENT)
    Flowable<HttpResult> likeComment(@Path("id") int id);


    //
    //
    // 个人中心  我的公会 信息
    //
    //
    @GET(API.GET_UNION_INFO)
    Flowable<HttpResult<UnionInfo>> getUnionInfo();


    //
    //
    // 个人中心  我的公会  搜索达人公会信息
    //
    //
    @POST(API.SEARCH_UNION_EXPERT)
    Flowable<HttpResult<BaseListDto<GonghuiListDto>>> searchExpertUnion(@Body RequestBody params);


    //
    //
    // 个人中心  我的公会  /api/bunko/family/apply 申请入驻达人公会【v2.1】
    //
    //
    @POST(API.APPLY_UNION_EXPERT)
    Flowable<HttpResult> applyExpertUnion(@Body RequestBody params);

    //
    // 个人中心 我的礼物  达人礼物接口
    //

    @POST(API.GET_EXPERT_GIFT_WALL)
    Flowable<HttpResult<GiftWallDto>> getExpertGiftWall(@Body RequestBody params);

    @GET(API.GET_VERIFY_TOKEN)
    Flowable<HttpResult<VerifyTokenDto>> getVerifyToken(@Query("type") String type);

    @GET(API.GET_VERIFY_RESULT)
    Flowable<HttpResult<VerifyResultDto>> getVerifyResult(@Query("bizId") String bizId, @Query("type") String type);

    @POST(API.POST_ANCHOR_ID_CARD)
    Flowable<HttpResult> postAnchorIDCard(@Body RequestBody params);

    @POST(API.GET_VOICE_CHECK_RESULT)
    Flowable<HttpResult> getVoiceCheckResult(@Body RequestBody body);

    //
    // 个人中心 我的背包 获取我的背包信息
    //
    @GET(API.GET_BACKPACK)
    Flowable<HttpResult<BackpackDto>> getBackpack();


    //
    // 人脸识别 人工审核要调用的接口
    //
    @GET(API.GET_VERIFY_REVIEW)
    Flowable<HttpResult> getVerifyReview(@Query("bizId") String bizId);



}

