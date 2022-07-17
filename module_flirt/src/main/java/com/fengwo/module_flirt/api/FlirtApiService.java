package com.fengwo.module_flirt.api;

import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.DictsBean;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_flirt.bean.AnchorDetailDto;
import com.fengwo.module_flirt.bean.AppointTimes;
import com.fengwo.module_flirt.bean.AppointmentListBean;
import com.fengwo.module_flirt.bean.BeforeQuitMsg;
import com.fengwo.module_flirt.bean.CerMsgBean;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.ClientReconnentDto;
import com.fengwo.module_flirt.bean.CommentDetailsDTO;
import com.fengwo.module_flirt.bean.CommentTagDto;
import com.fengwo.module_flirt.bean.CommentWordDto;
import com.fengwo.module_flirt.bean.DateRecordDto;
import com.fengwo.module_flirt.bean.EnterRoomBean;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.FindHeaderDto;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.bean.FlirtCommentBean;
import com.fengwo.module_flirt.bean.FlirtRankBean;
import com.fengwo.module_flirt.bean.GetAnchorRoomInfo;
import com.fengwo.module_flirt.bean.GiftDto;
import com.fengwo.module_flirt.bean.GiftRecordPriceBean;
import com.fengwo.module_flirt.bean.HostOrderBean;
import com.fengwo.module_flirt.bean.ImpressDTO;
import com.fengwo.module_flirt.bean.IliaoBean;
import com.fengwo.module_flirt.bean.LabelTalentDto;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.bean.OrderIdBean;
import com.fengwo.module_flirt.bean.OrderListBean;
import com.fengwo.module_flirt.bean.OrderNumDto;
import com.fengwo.module_flirt.bean.ReMindUserBean;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_flirt.bean.ShareUrlDto;
import com.fengwo.module_flirt.bean.StartWBBean;
import com.fengwo.module_flirt.bean.SureAppointmentBean;
import com.fengwo.module_flirt.bean.TimeGiftResponse;
import com.fengwo.module_flirt.bean.TopicTagBean;
import com.fengwo.module_live_vedio.mvp.dto.SearchDto;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FlirtApiService {
    //分享
    @POST(API.BUNKO_2030020)
    Flowable<HttpResult<ShareInfoBean>> getShareInfo(@Body RequestBody body);

    @GET(com.fengwo.module_live_vedio.api.API.IS_ATTENTION)
    Flowable<HttpResult<IsAttentionDto>> isAttention(@Path("id") int id);

    //获取附近的人
    @POST(API.BUNKO_2031002)
    Flowable<HttpResult<BaseListDto<CityHost>>> getPeopleNearby(@Body RequestBody body);

    //获取发现列表三个头像
    @POST(API.BUNKO_2031005)
    Flowable<HttpResult<List<FindHeaderDto>>> getHeaderInfo(@Body RequestBody body);

    //获取发现列表
    @POST(API.BUNKO_FIND_NEAR_CARD)
    Flowable<HttpResult<BaseListDto<FindListDto>>> getFindList(@Body RequestBody body);

    //获取分享信息/添加分享次数
    @GET(API.BUNKO_SHARE_CARD)
    Flowable<HttpResult<ShareUrlDto>> getShareInfo(@Path("id") int id);

    //获取约会助手列表
    @POST(API.BUNKO_2029017)
    Flowable<HttpResult<BaseListDto<AppointmentListBean>>> getAppointList(@Body RequestBody body);

    //清空约会助手列表
    @POST(API.BUNKO_2029022)
    Flowable<HttpResult> getDatingAssistantsClearList();

    //检测主播直播状态
    @POST(API.BUNKO_2029019)
    Flowable<HttpResult<CheckAnchorStatus>> checkAnchorStatus(@Body RequestBody body);

    //清空点单已评价列表
    @POST(API.BUNKO_2029032)
    Flowable<HttpResult> clearZhuBoList(@Body RequestBody body);

    //获取主播信息
    @POST(API.BUNKO_2029015)
    Flowable<HttpResult<CityHost>> getAnchorInfo(@Body RequestBody body);

    //约单用户迟到提醒/过期邀约
    @POST(API.BUNKO_2029009)
    Flowable<HttpResult<ReMindUserBean>> remindUser(@Body RequestBody body);

    // 预约拒绝与接单
    @POST(API.BUNKO_2029014)
    Flowable<HttpResult> acceptAppointment(@Body RequestBody body);

    //城市主播列表
    @POST(API.BUNKO_2029010)
    Flowable<HttpResult<BaseListDto<CityHost>>> getCityHost(@Body RequestBody body);

    //获取在线列表
    @POST(API.BUNKO_2029023)
    Flowable<HttpResult<BaseListDto<CityHost>>> getOnLineList(@Body RequestBody body);

    // 查询主播时间段价格
    @POST(API.BUNKO_2030012)
    Flowable<HttpResult<AppointTimes>> getPeriodPrice(@Body RequestBody body);

    // 点单价格表
    @POST(API.BUNKO_2029012)
    Flowable<HttpResult<ArrayList<OrderListBean>>> getOrderListPrice(@Body RequestBody body);

    // 确定支付
    @POST(API.BUNKO_2029002)
    Flowable<HttpResult<OrderIdBean>> getSurePayInfo(@Body RequestBody body);

    // 确定预约
    @POST(API.BUNKO_2029008)
    Flowable<HttpResult<SureAppointmentBean>> getAppointment(@Body RequestBody body);

    // 添加详情页浏览记录
    @POST(API.BUNKO_2030013)
    Flowable<HttpResult> addBrowingRecord(@Body RequestBody body);

    @POST(API.GET_ANCHOR_DETAIL)
    Flowable<HttpResult<AnchorDetailDto>> getAnchorDetail(@Body RequestBody body);

    @POST(API.CER_TAG_LIST)
    Flowable<HttpResult<BaseListDto<CerTagBean>>> getCerTagList(@Body RequestBody body);

    @POST(API.HOME_TAG_LIST)
    Flowable<HttpResult<List<CerTagBean>>> getHomeTagList(@Body RequestBody body);

    @POST(API.NEW_HOME_TAG_LIST)
    Flowable<HttpResult<List<CerTagBean>>> getNewHomeTagList();

    @POST(API.NEW_HOME_TAG_LIST2)
    Flowable<HttpResult<List<CerTagBean>>> getNewHomeTagList2(@Body RequestBody body);

    @POST(API.NEW_LABEL_TALENT)
    Flowable<HttpResult<BaseListDto<LabelTalentDto>>> getLabelTalent(@Body RequestBody body);

    @POST(API.FLIRT_START)
    Flowable<HttpResult<StartWBBean>> startFlirt(@Body RequestBody body);

    @POST(API.GET_FLIRT_MSG)
    Flowable<HttpResult<CerMsgBean>> getCerMsg();

    @POST(API.SAVE_FLIRT_MSG)
    Flowable<HttpResult> saveCerMsg(@Body RequestBody body);

    @POST(API.BUNKO_2030017)
        //进入直播间
    Flowable<HttpResult<EnterRoomBean>> getEnterRoom(@Body RequestBody body);

    @POST(API.BUNKO_2030023)
    Flowable<HttpResult<GetAnchorRoomInfo>> getAnchorRoomInfo(@Body RequestBody body);

    @POST(API.BUNKO_2030016)
//退出直播间
    Flowable<HttpResult> quitRoom(@Body RequestBody body);

    @POST(API.BUNKO_2030001)
//主播接单/拒接
    Flowable<HttpResult> anchorOrderReceive(@Body RequestBody body);

    //主播加时接单/拒接
    @POST(API.BUNKO_2030018)
    Flowable<HttpResult> anchorAddTimeOrderReceive(@Body RequestBody body);

    @POST(API.OPEN_FLIRT_TAG)
    Flowable<HttpResult<BaseListDto<TopicTagBean>>> getTopicTag(@Body RequestBody body);

    @POST(API.RECONNEC)
    Flowable<HttpResult<ReconWbBean>> reconnec();

    @POST(API.REJECT_RECONNEC)
    Flowable<HttpResult> rejectReconnec();

    @POST(API.PUSH_END)
    Flowable<HttpResult> pushEnd();

    @POST(API.BUNKO_2027003)
    Flowable<HttpResult<List<CommentTagDto>>> getCommentTag(@Body RequestBody body);

    @POST(API.BUNKO_2029003)
    Flowable<HttpResult<List<GiftDto>>> getGiftNormal(@Body RequestBody body);

    @POST(API.BUNKO_2029004)
    Flowable<HttpResult<List<GiftDto>>> getGiftTime(@Body RequestBody body);

    @POST(API.BUNKO_2027006)
    Flowable<HttpResult<List<GiftDto>>> getGiftTime2(@Body RequestBody body);

    @POST(API.BUNKO_V2_2029002)
    Flowable<HttpResult<TimeGiftResponse>> orderGiftTime(@Body RequestBody body);

    @POST(API.BUNKO_2029001)
    Flowable<HttpResult<BaseListDto<DateRecordDto>>> getDateRecord(@Body RequestBody body);

    @POST(API.BUNKO_2029011)
    Flowable<HttpResult> cancleDateRecord(@Body RequestBody body);

    @POST(API.BUNKO_2029013)
    Flowable<HttpResult> sendNormalGift(@Body RequestBody body);

    @POST(API.BUNKO_2029005)
    Flowable<HttpResult<TimeGiftResponse>> sendTimeGift(@Body RequestBody body);

    @POST(API.BUNKO_2029030)
    Flowable<HttpResult> cancleOrder(@Body RequestBody body);

    @POST(API.HOST_ORDER_LIST)
    Flowable<HttpResult<BaseListDto<HostOrderBean>>> getHostOrderList(@Body RequestBody body);

    @POST(API.HISTORY_LIST)
    Flowable<HttpResult<BaseListDto<HostOrderBean>>> getHistoryList(@Body RequestBody body);

    @POST(API.GIFT_2030005)
    Flowable<HttpResult<BaseListDto<HostOrderBean>>> getGiftDetail(@Body RequestBody body);

    @POST(API.GIFT_2030021)
    Flowable<HttpResult<BaseListDto<HostOrderBean>>> getGiftRecord(@Body RequestBody body);

    @POST(API.GIFT_2030022)
    Flowable<HttpResult<GiftRecordPriceBean>> getGiftRecordPrice();

    @POST(API.REMIND_AND_INVITE)
    Flowable<HttpResult> remindAndInvite(@Body RequestBody body);

    @POST(API.BEFORE_QUITE_ROOM)
    Flowable<HttpResult<BeforeQuitMsg>> getBeforQuitMsg(@Body RequestBody body);

    @POST(API.APPRAISE_HOST)
    Flowable<HttpResult> appraiseHost(@Body RequestBody body);

    @POST(API.FLIRT_RANK)
    Flowable<HttpResult<BaseListDto<FlirtRankBean>>> flirtRank(@Body RequestBody body);

    @POST(API.SEARCH_ANCHOR)
    Flowable<HttpResult<BaseListDto<SearchDto>>> searchAnchor(@Body RequestBody body);


    @POST(API.GET_CHAT_HISTORY)
    Flowable<HttpResult<BaseListDto<SocketRequest<WenboWsChatDataBean>>>> getChatHistory(@Body RequestBody body);

    @POST(API.GET_WENBO_GIFT_RES)
    Flowable<HttpResult<List<String>>> getGiftList();

    @POST(API.CLIENT_RECONNECT)
    Flowable<HttpResult<ClientReconnentDto>> clientReconnent();

    @GET(API.GET_DICTS)
    Flowable<HttpResult<List<DictsBean>>> getDicts(@Path("key") String key);

    @POST(API.BUNKO_2029031)
    Flowable<HttpResult<List<MyOrderDto>>> getMyOrder();

    @POST(API.BUNKO_2040001)
    Flowable<HttpResult<BaseListDto<CommentWordDto>>> getCommentWord(@Body RequestBody body);

    @POST(API.BUNKO_2030024)
    Flowable<HttpResult<IliaoBean>> getYqIliao(@Body RequestBody body);


    @POST(API.BUNKO_2030025)
    Flowable<HttpResult> getYq(@Body RequestBody body);


    @POST(API.BUNKO_2029033)
    Flowable<HttpResult<OrderNumDto>> getAddTimeNum(@Body RequestBody body);

    @GET(API.FIND_NEAR_CARD_DETAIL)
    Flowable<HttpResult<FindDetailBean>> getFindDetail(@Path("id") int id);

    @POST(API.COMMENT)
    Flowable<HttpResult<CommentModel>> comment(@Body RequestBody body);

    @GET(API.GET_COMMENT_LIST)
    Flowable<HttpResult<BaseListDto<CommentModel>>> getCommentList(@Query("id") int id, @Query("pageParam") String pageParam);

    /**
     * 获取正在观看达人直播的用户列表
     *
     * @param body
     * @return
     */
    @POST(API.BUNKO_2028002)
    Flowable<HttpResult<List<MessageListVO>>> getChatUsers(@Body RequestBody body);

    @POST(API.BUNKO_2029025)
    Flowable<HttpResult<ImpressDTO>> getImpress(@Body RequestBody body);

    /**
     *
     * 这个是打赏榜单的接口
     *
     * @param body
     * @return
     */
    @POST(API.LIST_REWARD)
    Flowable<HttpResult<BaseListDto<HostOrderBean>>> getListReward(@Body RequestBody body);

    /**
     *
     * 这个是打赏榜总计接口
     *
     * @param body
     * @return
     */
    @POST(API.REWARD_TOTAL)
    Flowable<HttpResult<GiftRecordPriceBean>> getRewardTotal(@Body RequestBody body);

    /**
     *
     * 这个是印象榜单的接口
     *
     * @param body
     * @return
     */
    @POST(API.LIST_IMPRESSION)
    Flowable<HttpResult<BaseListDto<HostOrderBean>>> getListImpression(@Body RequestBody body);

    /**
     *
     * 这个是印象榜单总计接口
     *
     * @param body
     * @return
     */
    @POST(API.IMPRESSION_TOTAL)
    Flowable<HttpResult<GiftRecordPriceBean>> getImpressionTotal(@Body RequestBody body);

    /**
     * 达人直播间获取评价列表
     * @param body
     * @return
     */
    @POST(API.GET_COMMENT)
    Flowable<HttpResult<FlirtCommentBean>> getComments(@Body RequestBody body);


    @POST(API.GET_COMMENT_DETAILS)
    Flowable<HttpResult<CommentDetailsDTO>> getCommentDetails(@Body RequestBody body);


}
