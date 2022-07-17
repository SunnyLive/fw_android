package com.fengwo.module_chat.mvp.api;

import com.fengwo.module_chat.base.OffcialBean;
import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.GreetMessageBean;
import com.fengwo.module_comment.bean.GreetTipsBean;
import com.fengwo.module_chat.mvp.model.bean.InteractBean;
import com.fengwo.module_chat.mvp.model.bean.SystemMessageBean;
import com.fengwo.module_chat.mvp.model.bean.VisitorRecordBean;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_chat.mvp.model.bean.ContactBean;
import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_chat.mvp.model.bean.GroupInfoModel;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_chat.mvp.model.bean.MerchantListBoean;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.model.bean.SearchCardBean;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.IsBlackDto;
import com.fengwo.module_comment.bean.RandomContentModel;

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

public interface ChatService {
    @GET(API.GET_CONTACT_LIST)
    Flowable<HttpResult<List<ContactBean>>> getContactList();

    @POST(API.ADD_BLACKLIST)
    Flowable<HttpResult> AddBlackList(@Path("id") int userId);

    @DELETE(API.DELETE_BLACK)
    Flowable<HttpResult> deleteBlack(@Path("id") int userId);

    @GET(API.IS_BLACK)
    Flowable<HttpResult<IsBlackDto>> judgeBlack(@Path("id") String userId);

    @GET(API.GET_RECOMMEND_CIRCLE_LIST)
    Flowable<HttpResult<List<RecommendCircleBean>>> getRecommendCircleList();

    @GET(API.GET_CIRCLE_LIST)
    Flowable<HttpResult<BaseListDto<CircleListBean>>> getCirclesList(@Query("circleId") String circleId, @Query("pageParam") String pageParam, @Query("tab") String tab);

    @GET(API.GET_CHAT_HOME_BANNER)
    Flowable<HttpResult<BaseListDto<AdvertiseBean>>> getBanner(@Query("modelType") int modelType, @Query("objectType") int objectType, @Query("pageParam") String pageParam,
                                                               @Query("pageType") int pageType, @Query("parentId") String parentId, @Query("position") int position);

    @GET(API.SEARCH_HOT)
    Flowable<HttpResult<List<String>>> getSearchHot();

    @GET(API.SEARCH_CARD)
    Flowable<HttpResult<SearchCardBean>> getSearchCard(@Query("pageParam") String pageParam, @Query("searchContent") String searchContent);

    @GET(API.GET_CARD_LIST)
    Flowable<HttpResult<BaseListDto<ChatCardBean>>> getCardList(@Query("cardId") String cardId,
                                                                @Query("circleId") String circleId,
                                                                @Query("direction") int direction,
                                                                @Query("userId") int uid,
                                                                @Query("tab") int tab,
                                                                @Query("pageParam") String pageParam,
                                                                @Query("myLike") int myLike);

    @GET(API.GET_CIRCLE_MEMBER)
    Flowable<HttpResult<CardMemberModel>> getCircleMemberList(@Query("circleId") String circleId);

    @GET(API.GET_RECOMMEND_CIRCLE_LIST + "/{id}")
    Flowable<HttpResult<RecommendCircleBean>> getCircleInfo(@Path("id") String id);

    @GET(API.GET_ALL_CIRCLES)
    Flowable<HttpResult<BaseListDto<RecommendCircleBean>>> getCircleList(@Query("pageParam") String pageParam);

    @POST(API.SET_USER_LOVE_CIRCLE)
    Flowable<HttpResult> setUserLoveCircle(@Body RequestBody body);

    @GET(API.GET_TAG_BY_CIRCLE)
    Flowable<HttpResult<List<CardTagModel>>> getTagList(@Path("circleId") int circleId);

    @POST(API.POST_CARD)
    Flowable<HttpResult<CardDetailBean>> postCard(@Body RequestBody body);

    @PUT(API.CARD_LIKE)
    Flowable<HttpResult> cardLike(@Path("id") String id);


    @POST(API.ADD_ATTENTION)
    Flowable<HttpResult> addAttention(@Path("id") String id);

    @POST(API.BUNKO_2042002)
    Flowable<HttpResult<List<RandomContentModel>>> getRandomContent();

    @HTTP(method = "DELETE", path = API.REMOVE_ATTENTION, hasBody = false)
    Flowable<HttpResult> removeAttention(@Path("id") String userId);

    @GET(API.GET_COMMENT_LIST)
    Flowable<HttpResult<BaseListDto<CommentModel>>> getCommentList(@Query("id") int id, @Query("pageParam") String pageParam);

    @PUT(API.LIKE_COMMENT)
    Flowable<HttpResult> likeComment(@Path("id") int id);

    @GET(API.GET_SECOND_COMMENT)
    Flowable<HttpResult<BaseListDto<CommentModel>>> getSecondCommentList(@Query("id") int id, @Query("pageParam") String pageParam);

    @POST(API.COMMENT)
    Flowable<HttpResult<CommentModel>> comment(@Body RequestBody body);

    @GET(API.GET_GROUP_INFO)
    Flowable<HttpResult<GroupInfoModel>> getGroupInfo(@Path("id") String groupId);

    @PUT(API.FORBIDDEN_GROUP_TALK)
    Flowable<HttpResult> forbiddenGroupTalk(@Path("id") String groupId);

    @PUT(API.UNFORBIDDEN_GROUP_TALK)
    Flowable<HttpResult> unforbiddenGroupTalk(@Path("id") String groupId);

    @GET(API.GET_GROUP_MEMBER_LIST)
    Flowable<HttpResult<BaseListDto<GroupMemberModel>>> getGroupMemberList(@Query("id") String groupId,
                                                                           @Query("nickname") String nickname,
                                                                           @Query("pageParam") String pageParam);

    @GET(API.ENTER_GROUP)
    Flowable<HttpResult<EnterGroupModel>> enterGroup(@Path("groupId") String groupId);

    @HTTP(method = "DELETE", path = API.DELETE_GROUP, hasBody = false)
    Flowable<HttpResult> quitGroup(@Path("id") String groupId);

    @DELETE(API.DELETE_GROUP_MEMBER)
    Flowable<HttpResult> deleteGroupMember(@Query("groupId") String groupId, @Query("ids") String ids);

    @POST(API.CREATE_GROUP)
    Flowable<HttpResult<String>> createGroup(@Body RequestBody body);

    @PUT(API.EDIT_NOTICE)
    Flowable<HttpResult> editNotice(@Body RequestBody body);

    @GET(API.GET_MERCHANT_LIST)
    Flowable<HttpResult<BaseListDto<MerchantListBoean>>> getMerchantList(@Query("pageParam") String pageParam,
                                                                         @Query("tagId") String tagId);

    @PUT(API.GROUP_DISTURB)
    Flowable<HttpResult> groupDisturb(@Body RequestBody body);

    @GET(API.GET_CARD_TAG_LIST)
    Flowable<HttpResult<List<CardTagModel>>> getCardTagList(@Path("cardId") String cardId);

    @GET(API.GET_CARD_FROM_TAGS)
    Flowable<HttpResult<BaseListDto<CircleListBean>>> getCardFromTags(@Query("pageParam") String pageParam,
                                                                      @Query("tagIds") String tagIds);

    @GET(API.GET_LIST_CARD_LIKE)
    Flowable<HttpResult<BaseListDto<InteractBean>>> getCardLikeList(@Query("pageParam") String pageParam);

    @PUT(API.DELETE_CARD_LIKE)
    Flowable<HttpResult> updateCardLike(@Path("id") String id);

    @GET(API.GET_LIST_CARD_COMMENT)
    Flowable<HttpResult<BaseListDto<InteractBean>>> getCardCommentList(@Query("pageParam") String pageParam);

    @DELETE(API.DELETE_CARD_COMMENT)
    Flowable<HttpResult> deleteCardComment(@Path("id") String id);

//    @POST(API.ADD_VISITOR_RECORD)
//    Flowable<HttpResult<BaseListDto<VisitorRecordBean>>> addVisitorRecord(@Body RequestBody body);

    @POST(API.GET_LIST_VISITOR_RECORD)
    Flowable<HttpResult<BaseListDto<VisitorRecordBean>>> getVisitorRecordList(@Query("pageParam") String pageParam);

    @GET(API.GET_LIST_SYSTEM_MESSAGE)
    Flowable<HttpResult<BaseListDto<SystemMessageBean>>> getSystemMessageList(@Query("pageParam") String pageParam);

    @DELETE(API.CLEAR_LIST_SYSTEM_MESSAGE)
    Flowable<HttpResult> clearSystemMessageList();

    @GET(API.GET_MY_ATTENTION_NUM)
    Flowable<HttpResult> getMyAttentionNum();

    @GET(API.GET_MY_FANS_NUM)
    Flowable<HttpResult> getMyFansNum();

    @GET(API.GET_MY_FRIENDS_NUM)
    Flowable<HttpResult> getMyFriendsNum();

    @POST(API.CARD_TOP)
    Flowable<HttpResult> cardTop(@Body RequestBody body);

    @POST(API.CARD_POWER)
    Flowable<HttpResult> cardPower(@Body RequestBody body);

    @POST(API.CARD_DELETE)
    Flowable<HttpResult> cardDelete(@Body RequestBody body);

    @POST(API.CARD_DRAFT_POST)
    Flowable<HttpResult> cardDraftPost(@Body RequestBody body);

    @GET(API.CARD_DETAIL_EDIT)
    Flowable<HttpResult<CardDetailBean>> getCardDetail(@Path("id") int id);

    @GET(API.GET_LIST_GREET_MESSAGE)
    Flowable<HttpResult<BaseListDto<GreetMessageBean>>> getGreetMessageList(@Query("pageParam") String pageParam);

    @DELETE(API.CLEAR_LIST_GREET_MESSAGE)
    Flowable<HttpResult> clearGreetMessageList();



    @POST(API.OFFICIAL_NEW_POST)
    Flowable<HttpResult<BaseListDto<OffcialBean>>> officialNewPost(@Body RequestBody body);

    @POST(API.OFFICIAL_MARK_POST)
    Flowable<HttpResult> officialMarkPost();

    @POST(API.MARK_GREET_MESSAGE)
    Flowable<HttpResult> clearMarkMessageList();

}
