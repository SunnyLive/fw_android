package com.fengwo.module_live_vedio.api;

import com.fengwo.module_comment.base.BannerBean;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.BaseHttpData;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.bean.Param;
import com.fengwo.module_comment.bean.TreeBean;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.bean.VensionDto;
import com.fengwo.module_comment.bean.WishRepayBean;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_live_vedio.api.param.GivingPacketParam;
import com.fengwo.module_live_vedio.api.param.PacketCountParam;
import com.fengwo.module_live_vedio.api.param.RedPacketClickParam;
import com.fengwo.module_live_vedio.mvp.dto.AgentInviteDto;
import com.fengwo.module_live_vedio.mvp.dto.AttentionHostDto;
import com.fengwo.module_live_vedio.mvp.dto.AttentionListDto;
import com.fengwo.module_live_vedio.mvp.dto.BannedDto;
import com.fengwo.module_live_vedio.mvp.dto.BlessingDto;
import com.fengwo.module_live_vedio.mvp.dto.BrokerRankDto;
import com.fengwo.module_live_vedio.mvp.dto.BuyShouhuDto;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.CitySelectDto;
import com.fengwo.module_live_vedio.mvp.dto.CloseLiveDto;
import com.fengwo.module_live_vedio.mvp.dto.EndGiftDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomActDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomPkActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.GameBean;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftEffectDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftPiaopingDto;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.H5addressBean;
import com.fengwo.module_live_vedio.mvp.dto.IsBlackDto;
import com.fengwo.module_live_vedio.mvp.dto.LastFrameDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveHourRankDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_live_vedio.mvp.dto.MapPoiDto;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.MyHourDto;
import com.fengwo.module_live_vedio.mvp.dto.NewActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.NewTouTiaoListDto;
import com.fengwo.module_live_vedio.mvp.dto.OpenGiftDto;
import com.fengwo.module_live_vedio.mvp.dto.OrderDto;
import com.fengwo.module_live_vedio.mvp.dto.PKGroupListDTO;
import com.fengwo.module_live_vedio.mvp.dto.PKSingleListDTO;
import com.fengwo.module_live_vedio.mvp.dto.PacketCountBean;
import com.fengwo.module_live_vedio.mvp.dto.PacketResultBean;
import com.fengwo.module_live_vedio.mvp.dto.PendantBean;
import com.fengwo.module_live_vedio.mvp.dto.PendantDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;
import com.fengwo.module_live_vedio.mvp.dto.PeopleDto;
import com.fengwo.module_live_vedio.mvp.dto.PkContributeInfo;
import com.fengwo.module_live_vedio.mvp.dto.PkTypeDto;
import com.fengwo.module_live_vedio.mvp.dto.PlaneAllDto;
import com.fengwo.module_live_vedio.mvp.dto.PlaneGiftDto;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_live_vedio.mvp.dto.ProfitDto;
import com.fengwo.module_live_vedio.mvp.dto.PunishTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;
import com.fengwo.module_live_vedio.mvp.dto.RankSinglePkDto;
import com.fengwo.module_live_vedio.mvp.dto.RankTuhaoDto;
import com.fengwo.module_live_vedio.mvp.dto.RankZhuboDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.dto.SearchDto;
import com.fengwo.module_live_vedio.mvp.dto.ShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.StartLivePushDto;
import com.fengwo.module_live_vedio.mvp.dto.StickersDto;
import com.fengwo.module_live_vedio.mvp.dto.TeenagerInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.TeenagerVideoDto;
import com.fengwo.module_live_vedio.mvp.dto.ThisGiftDto;
import com.fengwo.module_live_vedio.mvp.dto.TouTiaoListDto;
import com.fengwo.module_live_vedio.mvp.dto.UserBoxDto;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.mvp.dto.WishingWallDto;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.fengwo.module_live_vedio.mvp.dto.ZqDto;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LiveApiService {
    @GET(API.CONFIG)
    Flowable<HttpResult<List<PendantBean>>> config();

    @GET(API.GET_LIVE_ZHUBO_MENU)
    Flowable<HttpResult<BaseListDto<ZhuboMenuDto>>> getZhuboMenu(@Query("type") int type);

    @GET(API.GET_LIVE_ZHUBO_LIST)
    Flowable<HttpResult<BaseListDto<ZhuboDto>>> getZhuboList(@Query("pageParam") String pageParam, @Query("menuId") int menuId);

    @POST(API.ENTER_LIVINGROOM)
    Flowable<HttpResult<EnterLivingRoomDto>> enterLivingRoom(@Body RequestBody body);



//    @GET(API.GET_ACTIVIY_INFO_REWARD)
//    Flowable<HttpResult<ActivityRewardInfoDto>> getActivityReward(@Query("channelId") int channelId);

    @GET(API.GET_ACTIVIY_MY_HOUR)
    Flowable<HttpResult<MyHourDto>> getActivityHour(@Query("userId") int channelId);

    @GET(API.GET_ACTIVIY_INFO_REWARD)
    Flowable<HttpResult<LastFrameDto>> getActivityReward(@Query("channelId") int channelId);

    @POST(API.GET_ACTIVIY_ADD_STICERS)
    Flowable<HttpResult<PendantDto>> getActivitySticerspost(@Body RequestBody body);

    @PUT(API.GET_ACTIVIY_ADD_STICERS)
    Flowable<HttpResult<HttpResult>> getActivitySticers(@Body RequestBody body);

    @DELETE(API.ENTER_LIVE_DEL)
    Flowable<HttpResult<EnterLivingRoomPkActivityDto>> delSticers(@Path("id") int userId);

    @POST(API.ENTER_ROOM_PK_ACTIVITY)
    Flowable<HttpResult<EnterLivingRoomPkActivityDto>> enterLivingRoomPKActivity(@Body RequestBody body);

    @POST(API.ENTER_ROOM_ACTIVITY)
    Flowable<HttpResult<EnterLivingRoomActDto>> enterLivingRoomActivity(@Body RequestBody body);

    @GET(API.ENTER_ROOM_ACTIVITY_NEW)
    Flowable<HttpResult<NewActivityDto>> enterLivingRoomNewActivity(@Path("channelId") int channelId);

    @POST(API.LEAVE_LIVINGROOM)
    Flowable<HttpResult> leaveLivingRoom(@Path("channelId") int channelId);

    @GET(API.ROOM_PEOPLE)
    Flowable<HttpResult<List<WatcherDto>>> getRoomWatchers(@Path("roomId") String roomId);

    @POST(API.START_LIVE)
    Flowable<HttpResult<StartLivePushDto>> startLive(@Body RequestBody body);

    @POST(API.SINGLE_RANDOM_PK)
    Flowable<HttpResult<PunishTimeDto>> singleRandomPk();

    @GET(API.GET_GIFTS)
    Flowable<HttpResult<List<GiftDto>>> getGifts();

    @GET(API.GET_GIFT_BY_TYPE)
    Flowable<HttpResult<List<GiftDto>>> getGiftByType(@Query("giftTypeId") int giftTypeId);

    @POST(API.SEND_GIFT)
    Flowable<HttpResult<EndGiftDto>> sendGift(@Body RequestBody body);

    @POST(API.SEND_ActivityGIFT)
    Flowable<HttpResult<EndGiftDto>> sendActivityGift(@Body RequestBody body);
    @POST(API.TOUTIAO_GIFT_SEND)
    Flowable<HttpResult<PopoDto>> sendToutiaoGift(@Body RequestBody body);

    @POST(API.CLOSE_LIVE_PUSH)
    Flowable<HttpResult<CloseLiveDto>> closeLivePush();

    @GET(API.GET_RECHARGE_LIST)
    Flowable<HttpResult<List<RechargeDto>>> getRechargeList();


    @GET(API.GET_PENDANT_LIST)
    Flowable<HttpResult<PendantListDto>> getPendantList();

    @HTTP(method = "DELETE", path = API.CLOSE_MATCH_SINGLE_PK, hasBody = false)
    Flowable<HttpResult> cancleMatchSinglePk();

    @GET(API.RANK_SINGLE_PK)
    Flowable<HttpResult<List<RankSinglePkDto>>> getRankSinglePk();

    @GET(API.RANK_TEAM_PK)
    Flowable<HttpResult<List<RankSinglePkDto>>> getRankTeamPk();

    @GET(API.RANK_GUILD_PK)
    Flowable<HttpResult<List<RankSinglePkDto>>> getRankGuildPk();

    @GET(API.RANK_TUHAO)
    Flowable<HttpResult<List<RankTuhaoDto>>> getRankTuhao(@Query("cycle") int cycle);

    @GET(API.ATTENTION_LIST)
    Flowable<HttpResult<AttentionListDto>> getAttentionList(@Query("pageParam") String pageParam);

    @POST(API.ADD_ATTENTION)
    Flowable<HttpResult<BaseHttpData>> addAttention(@Path("id") String id);

    @GET(API.GUARD_LIST_BY_ID)
    Flowable<HttpResult<GuardListDto>> getGuardList(@Query("pageParam") String pageParam, @Query("userId") int uid);

    @GET(API.TOP_ZHUBO)
    Flowable<HttpResult<List<RankZhuboDto>>> getZhuboRank(@Query("cycle") int cycle);

    @GET(API.GET_BANNER)
    Flowable<HttpResult<List<BannerBean>>> getBanner(@Query("type") int type);

    @GET(API.GET_RECOMMENT)
    Flowable<HttpResult<BaseListDto<ZhuboDto>>> getRecomment(@Path("value") int value);

    @GET(API.GET_EACH_ATTENTION)
    Flowable<HttpResult<BaseEachAttention>> getEachAttention(@Path("id") int id);

    @POST(API.RESTRICT_LIST)
    Flowable<HttpResult<BaseListDto<BannedDto>>> getStickList(@Body RequestBody body);

    @GET(API.ROOM_MANAGER)
    Flowable<HttpResult<List<BannedDto>>> getRoomManager();

    @HTTP(method = "DELETE", path = API.MOVIE_ROOM_MANAGER, hasBody = false)
    Flowable<HttpResult> removeRoomManager(@Path("userId") int userId);

    @POST(API.MOVIE_STRICK)
    Flowable<HttpResult> removeStrick(@Body RequestBody body);

    @POST(API.GET_USER_INFO_NEW)
    Flowable<HttpResult<UserInfo>> getUserinfoById(@Body RequestBody body);


    @POST(API.GET_USER_INFO_NEW)
    Flowable<HttpResult<UserInfo>> getUserinfoByIdNew(@Body RequestBody body);

    @GET(API.IS_BLACK)
    Flowable<HttpResult<IsBlackDto>> judgeBlack(@Path("id") String userId);

    @GET(API.GET_GUARD_LIST)
    Flowable<HttpResult<BuyShouhuDto>> getShouhuList();

    @GET(API.THIS_CONTRIBUTE)
    Flowable<HttpResult<LiveProfitDto>> getContribute(@Query("pageParam") String pageParam, @Query("type") int type, @Query("userId") int hostId);

    @POST(API.THIS_GIFTS)
    Flowable<HttpResult<ThisGiftDto>> getThisGift(@Query("pageParam") String pageParam, @Query("userId") int hostId);

    @POST(API.WISH_COMMIT)
    Flowable<HttpResult> commitWish(@Body RequestBody body);

    @POST(API.ADD_RESTRICTS)
    Flowable<HttpResult> AddRestrict(@Body RequestBody body);

    @POST(API.ADD_ROOM_MANAGER)
    Flowable<HttpResult> AddRoomManager(@Path("userId") int userId);

    @POST(API.ADD_BLACKLIST)
    Flowable<HttpResult> AddBlackList(@Path("id") int userId);

    @DELETE(API.ADD_BLACKLIST)
    Flowable<HttpResult> DelBlackList(@Path("id") int userId);

    @GET(API.GET_COMMENT_WORD)
    Flowable<HttpResult<BaseListDto<QuickTalkDto>>> getCommentWord(@Query("pageParam") String pageParam);

    @GET(API.GET_RECEIVE_LIST)
    Flowable<HttpResult<PeopleDto>> getReceiveList(@Query("pageParam") String pageParam, @Query("type") int type, @Query("userId") int id);

    @POST(API.BUY_ORDER)
    Flowable<HttpResult<OrderDto>> getOrder(@Query("id") int id, @Body RequestBody params);

    @GET(API.BUY_GUARD_ORDER)
    Flowable<HttpResult<PopoDto>> buyGuard(@Query("id") int id, @Query("userId") int userId);

    @HTTP(method = "DELETE", path = API.REMOVE_ATTENTION, hasBody = false)
    Flowable<HttpResult> removeAttention(@Path("id") String userId);

    @GET(API.BROKER_RANK)
    Flowable<HttpResult<BaseListDto<BrokerRankDto>>> getBrokerRank(@Query("pageParam") String params);

    @GET(API.PROFIT_TODAY)
    Flowable<HttpResult<ProfitDto>> getProfitToday();

    @GET(API.SHARE_INFO)
    Flowable<HttpResult<ShareInfoDto>> getShareInfo();

    @POST(API.SINGLE_INVITE_PK)
    Flowable<HttpResult> singleInvitePk(@Path("friendId") int id);

    @POST(API.TEAM_RANDOM_PK)
    Flowable<HttpResult> teamRandomPk(@Body RequestBody params);

    @GET(API.SEARCH_CHANNEL)
    Flowable<HttpResult<BaseListDto<SearchDto>>> searchChannel(@Path("keyword") String key, @Query("pageParam") String params);

    @GET(API.INVITE_FRIEND_PK)
    Flowable<HttpResult<PunishTimeDto>> inviteFriendSinglePk(@Path("friendId") int friendId);

    @POST(API.RESPONSE_FRIEND_AGREE)
    Flowable<HttpResult> responseAgree(@Path("friendId") int friendId);

    @GET(API.RESPONSE_FRIEND_REFUSE)
    Flowable<HttpResult> responseRefuse(@Path("friendId") int friendId);

    @POST(API.CREATE_ROOM)
    Flowable<HttpResult> createRoom();

    @POST(API.INVITE_FRIEND_PK_TEAM)
    Flowable<HttpResult> inviteFriendTeamPk(@Body RequestBody params);

    @GET(API.ATTENTION_HOST_LIST)
    Flowable<HttpResult<List<AttentionHostDto>>> getAttentionHost();

    @POST(API.SINGLE_ADD_GROUP_RANDOM)
    Flowable<HttpResult> groupRandomSingleAdd();

    @HTTP(method = "DELETE", path = API.CANCEL_INVITE_FRIEND, hasBody = false)
    Flowable<HttpResult> cancelInviteFriend();

    @GET(API.INTO_GROUP_ROOM)
    Flowable<HttpResult> intoGroupRoom(@Path("teamId") String teamId);

    @HTTP(method = "DELETE", path = API.CANCEL_GROUP_PK, hasBody = false)
    Flowable<HttpResult> cancelGroupPk();

    @HTTP(method = "DELETE", path = API.LEAVE_PK_ROOM, hasBody = false)
    Flowable<HttpResult> leavePKRoom(@Path("teamId") String teamId);


    @POST(API.SEND_DANMU_MSG)
    Flowable<HttpResult<PopoDto>> sendDanmu(@Body RequestBody params);

    @GET(API.GET_MAP_POI)
    Flowable<HttpResult<List<MapPoiDto>>> getMapPoi();

    @GET(API.GET_SINGLE_PK_LIST)
    Flowable<HttpResult<BaseListDto<PKSingleListDTO>>> getSinglePKList(@Query("pageParam") String pageParam);

    @GET(API.GET_GROUP_PK_LIST)
    Flowable<HttpResult<BaseListDto<PKGroupListDTO>>> getGroupPKList(@Query("pageParam") String pageParam);

    @GET(API.TOUTIAO_LIST)
    Flowable<HttpResult<List<TouTiaoListDto>>> getToutiaoList();

    @GET(API.TOUTIAO_LIST_NEW)
    Flowable<HttpResult<List<NewTouTiaoListDto>>> getToutiaoListNew();

    @GET(API.GUILD_CITY_LIST)
    Flowable<HttpResult<CitySelectDto>> getGuildCityList(@Path("id") String id);

    @POST(API.GUILD_CHALLENGE)
    Flowable<HttpResult> guildChallenge(@Body RequestBody params);

    @PUT(API.ON_OFF_PK)
    Flowable<HttpResult> onOffPk(@Path("state") int state);

    @GET(API.SEND_FEIZAO)
    Flowable<HttpResult<PopoDto>> sendFeizao(@Query("anchorId") String anchorId);

    @GET(API.HOST_CONNECTION_LINE)
    Flowable<HttpResult<StartLivePushDto>> hostConnLine();

    @GET(API.GET_EXIST_TOUTIAO)
    Flowable<HttpResult<GiftPiaopingDto>> getExistTouTiao();


    @GET(API.GET_ACTIVIY_ADD_STICERS_CK)
    Flowable<HttpResult<List<StickersDto>>> getstickers(@Path("liveChannelId") int anchorId);

    @GET(API.GET_ACTIVIY_ADD_STICERS)
    Flowable<HttpResult<List<StickersDto>>> getZbstickers();

    @POST(API.GET_CHANNEL_INFO)
    Flowable<HttpResult<ChannelShareInfoDto>> getRoomInfo(@Body RequestBody params);

    @GET(API.CHECK_PK_START)
    Flowable<HttpResult<MatchTeamResult>> checkPkStart(@Path("id") int id);

    @GET(API.CHECK_PK_RESULT)
    Flowable<HttpResult> checkPkResult(@Path("id") int id);

    @GET(API.GIFT_EFFECT)
    Flowable<HttpResult<List<GiftEffectDto>>> getGiftEffect();

    @GET(API.CHECK_RECEIVE_PK_MSG)
    Flowable<HttpResult> checkReceivePkMsg();

    @GET(API.IS_ATTENTION)
    Flowable<HttpResult<IsAttentionDto>> isAttention(@Path("id") int id);

    @GET(API.PK_SURRENDER)
    Flowable<HttpResult> pkSurrender();

    @GET(API.PK_TYPE)
    Flowable<HttpResult<PkTypeDto>> checkPkType(@Path("id") int id);


    @POST(API.GET_DETAIL)
    Flowable<HttpResult<GetActivityInfoDto>> getActivityInfo(@Body RequestBody body);

    @GET(API.INVITE_LIST)
    Flowable<HttpResult<BaseListDto<AgentInviteDto>>> getInviteList(@Query("pageParam") String pageParam);

    @POST(API.REJECT_RECONNECT)
    Flowable<HttpResult> rejectReconnect();

    @GET(API.GET_ACTIVITY_SHARE_INFO)
    Flowable<HttpResult<ShareInfoDto>> getActivityShareInfo();

    @GET(API.GET_PK_RANK_SHARE_INFO)
    Flowable<HttpResult<ShareInfoDto>> getPkRankShareInfo(@Query("anchorId") int anchorId);

    @GET(API.AD_URL)
    Flowable<HttpResult<H5addressBean>> getH5Address();

    @POST(API.GET_USER_MEDAL)
    Flowable<HttpResult<UserMedalBean>> getUsermedal(@Body RequestBody body);

    @GET(API.PK_CONTRIBUTE_INFO)
    Flowable<HttpResult<PkContributeInfo>> getPkContributeInfo(@Path("id") int id);

    //1023
    @POST(API.GET_ANCHOR_WISH_INFO)
    Flowable<HttpResult<List<AnchorWishBean>>> getAnchorWish();

    @POST(API.GET_USER_WISH_INFO)
    Flowable<HttpResult<List<AnchorWishBean>>> getUserWish(@Path("anchorId") int anchorId);

    @POST(API.GET_WISH_REPAY)
    Flowable<HttpResult<WishRepayBean>> getWishRepay();

    @GET(API.GET_PLANE_LIST_ANCHOR)
    Flowable<HttpResult<List<PlaneAllDto>>> getPlaneBoxListAnchor(@Query("anchorId") int anchorId);

    @GET(API.GET_PLANE_LIST_USER)
    Flowable<HttpResult<List<PlaneAllDto>>> getPlaneBoxListUser(@Query("anchorId") int anchorId, @Query("userId") int userId);

    @GET(API.GET_BOX_GIFT_INFO)
    Flowable<HttpResult<PlaneGiftDto>> getBoxGiftInfo(@Query("boxId") int boxId, @Query("channelId") int channelId);

    @GET(API.OPEN_PLANE_GIFT)
    Flowable<HttpResult<OpenGiftDto>> openPlaneGift(@Query("anchorId") int anchorId, @Query("boxId") int boxId, @Query("userId") int userId);

    @POST(API.OPEN_TREE_GIFT)
    Flowable<HttpResult<TreeBean>> getGiftTree(@Body RequestBody body);


    @GET(API.GET_USER_BOX)
    Flowable<HttpResult<List<UserBoxDto>>> getUserBox(@Query("userId") int userId);

    @GET(API.GET_USER_ACTIVITY_INFO)
    Flowable<HttpResult<UserMedalBean>> getUserMedalInfo(@Query("userId") int userId);

    @GET(API.GET_USER_LIVE_ACTIVITY_INFO)
    Flowable<HttpResult<UserMedalBean>> getLiveMedalInfo(@Query("channelId") int channelId);

    @GET(API.OPEN_CAR)
    Flowable<HttpResult> openCar(@Query("id") String id, @Query("type") int type);

    @GET(API.LIVE_HOUR_RANK)
    Flowable<HttpResult<List<LiveHourRankDto>>> getLiveHourRank(@Query("userId") int channelId);


    @POST(API.GET_ADD_NOTICE)
    Flowable<HttpResult> getNoticeAdd(@Body RequestBody params);

    @DELETE(API.GET_EDL_NOTICE)
    Flowable<HttpResult> getNoticeEel();

    @POST(API.GET_INFO_NOTICE)
    Flowable<HttpResult> getNoticeInfo();


    @GET(API.GET_USER_LIVE_ACTIVITY_FESTVAL)
    Flowable<HttpResult<ZqDto>> getLiveFrstvalInfo();


    @GET(API.GET_USER_LIVE_ACTIVITY_DISCOUNT)
    Flowable<HttpResult<ZqDto>> getLiveDiscountInfo();

    @GET(API.GET_USER_CENTER)
    Flowable<HttpResult<UserInfo>> getUserCenter();

    //
    // 活动的祝福语接口
    //
    @GET(API.GET_BLESSING)
    Flowable<HttpResult<BlessingDto>> getBlessing(@Query("pageParam") String pageParam);


    //
    // 许愿墙的数据列表
    //
    @GET(API.GET_WISH_LIST)
    Flowable<HttpResult<WishingWallDto>> requestWishingWallList();

    //
    // 活动许愿 马上许愿
    //
    @POST(API.POST_WISHING)
    Flowable<HttpResult> requestWishing(@Body RequestBody params);

    //
    // 使用座驾
    //
    @PUT(API.PUT_USE_CAR)
    Flowable<HttpResult> requestUseCar(@Body  RequestBody params);

    //
    // 使用贵族
    //
    @PUT(API.PUT_USE_VIP)
    Flowable<HttpResult> requestUseVip(@Body  RequestBody params);

    //
    // 活动道具赠送接口
    //
    @POST(API.POST_GIVING)
    Flowable<HttpResult> requestGiving(@Body RequestBody params);


    //更新青少年模式
    @PUT(API.PUT_TEENAGER_UPDATE_INFO)
    Flowable<HttpResult<String>> updateTeenagerModeInfo(@Body TeenagerInfoDto params);

    //获取青少年模式视频
    @GET(API.GET_TEENAGER_VIDEO_LIST)
    Flowable<HttpResult<TeenagerVideoDto>> teenagerVideoList(@Query("pageParam") String pageParam);

    //青少年模式播放计数
    @PUT(API.PUT_TEENAGER_ADD_VIEW)
    Flowable<HttpResult<String>> putTeenagerAddView(@Query("id") String id);

    //获取版本信息
    @GET(com.fengwo.module_comment.api.API.APP_VERSION)
    Flowable<HttpResult<VensionDto>> appVersion();

    //赠送背包礼物
    @POST(API.POST_GIVING_BAG)
    Flowable<HttpResult<EndGiftDto>> givingBagGift(@Body RequestBody params);

    //游戏列表
    @GET(API.GAME_LIST)
    Flowable<HttpResult<List<GameBean>>> getGameList();

    //直播间红包数量
    @POST(API.PACKET_COUNT)
    Flowable<HttpResult<List<PacketCountBean>>> getPacketCount(@Body PacketCountParam params);

    //领取红包
    @POST(API.PACKET_GIVE)
    Flowable<HttpResult> givingPacket(@Body Param<GivingPacketParam> params);

    //查询红包结果
    @GET(API.PACKET_RESULT)
    Flowable<HttpResult<PacketResultBean>> getPacketResult(@Query("redpacketId") int redpacketId);

    //
    //主播三次推流都失败就调用当前这个接口
    //
    @GET(API.DROP_LIVE_STREAM)
    Flowable<HttpResult> requestDropLiveStream(@Query("streamName") String streamName);

    //
    // 主播端 异常断播 点击弹框的的需要接口
    //
    @GET(API.GET_LIVE_STATUS)
    Flowable<HttpResult<Boolean>> requestLiveConnectionStatus();

    //红包拆包
    @POST(API.RED_PACKET_CLICK)
    Flowable<HttpResult> redPacketClick(@Body RedPacketClickParam serialNo);
}
