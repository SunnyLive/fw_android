package com.fengwo.module_vedio.api;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_vedio.mvp.dto.AdvertiseBean;
import com.fengwo.module_vedio.mvp.dto.AlbumDto;
import com.fengwo.module_vedio.mvp.dto.FilterLabelDto;
import com.fengwo.module_vedio.mvp.dto.MenuDto;
import com.fengwo.module_vedio.mvp.dto.SearchResultDto;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.dto.ShortVideoModel;
import com.fengwo.module_vedio.mvp.dto.SmallCommentDto;
import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;
import com.fengwo.module_vedio.mvp.dto.SmallVedioListDto;
import com.fengwo.module_vedio.mvp.dto.SmallVideoMenuDto;
import com.fengwo.module_vedio.mvp.dto.VideoAlbumListDto;
import com.fengwo.module_vedio.mvp.dto.VideoBanerDto;
import com.fengwo.module_vedio.mvp.dto.VideoHomeCategoryDto;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VedioApiService {
    @POST(API.GET_MENU)
    Flowable<HttpResult<List<MenuDto>>> getMenu(@Body RequestBody params);

    @POST(API.GET_SMALL_LIST)
    Flowable<HttpResult<SmallVedioListDto>> getSmallVedioList(@Body RequestBody params);//, @Field("menuId") int menuId, @Field("order") int order, @Field("size") int size);

    @POST(API.GET_SHORT_LIST)
    Flowable<HttpResult<ShortVedioListDto>> getShortVedioList(@Body RequestBody params);


    @GET(API.SMALL_COMMENT)
    Flowable<HttpResult<SmallCommentDto>> getComment(@Query("parentId") int parentId, @Query("pageParam") String pageParam, @Query("type") int type, @Query("videoId") int videoId);

    @POST(API.SMALL_DETAIL_LIKE)
    Flowable<HttpResult> setSmallLike(@Body RequestBody params);

    @POST(API.ADD_SMALL_VIEWS)
    Flowable<HttpResult> addSmallViews(@Body RequestBody params);

    @POST(API.ADD_SMALL_COMMENT)
    Flowable<HttpResult> addSmallComment(@Body RequestBody params);

    @POST(API.SEARCH)
    Flowable<HttpResult<SearchResultDto>> search(@Body RequestBody params);

    @GET(API.GET_SHORT_DETAIL)
    Flowable<HttpResult<ShortVedioListDto.ShortVedio>> getShortDetail(@Query("id") int id);

    @POST(API.GET_SHORT_COMMENT)
    Flowable<HttpResult<SmallCommentDto>> getShortComment(@Body RequestBody params);

    @GET(API.GET_FILTER_LABS)
    Flowable<HttpResult<FilterLabelDto>> getFilterLabs();

    @POST(API.SET_SHORT_FAVOURITE)
    Flowable<HttpResult> setShortFavourite(@Body RequestBody params);

    //---------------------------------------
    @GET(API.GET_BANNER)
    Flowable<HttpResult<VideoBanerDto>> getBanner(@Query("modelType") int modelType, @Query("objectType") int objectType, @Query("pageType") int pageType, @Query("position") int position);


    @GET(API.GET_HOME_CATEGORY)
    Flowable<HttpResult<BaseListDto<VideoHomeCategoryDto>>> getCategory(@Query("pageParam") String pageParam);

    @GET(API.GET_BANNER_AD)
    Flowable<HttpResult<BaseListDto<AdvertiseBean>>> getBannerAd(@Query("modelType") int modelType, @Query("objectType") int objectType, @Query("pageParam") String pageParam,
                                                                 @Query("pageType") int pageType, @Query("parentIndex") int parentId, @Query("position") int position);

    @GET(API.GET_SHORT_VIDEO_LIST)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getShortVideoList(@Query("menuId") int menuId,
                                                                         @Query("order") int order,
                                                                         @Query("pageParam") String pageParam);

    @GET(API.GET_SHORT_VIDEO_LIST)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getVideoHomeList(@Query("order") int order,
                                                                            @Query("pageParam") String pageParam);

    @GET(API.GET_ALBUM_LIST)
    Flowable<HttpResult<BaseListDto<VideoAlbumListDto>>> getAlbumList(@Query("pageParam") String pageParam);

    @GET(API.GET_SHORT_VIDEO_LIST_NEW)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getShortVideoList(@Query("pageParam") String pageParam, @Query("status") int status);

    @GET(API.GET_SHORT_VIDEO_LIST_NEW)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getSearchShortVideoList(@Query("pageParam") String pageParam, @Query("content") String content);

    @GET(API.GET_SHORT_VIDEO_LIST_NEW)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getAlbumListById(@Query("pageParam") String pageParam, @Query("albumId")
            int albumId,@Query("order") int order,@Query("userId") int uid);//专辑列表(带userid)

    @GET(API.GET_SHORT_VIDEO_LIST_NEW)
    Flowable<HttpResult<BaseListDto<VideoHomeShortModel>>> getAlbumListById(@Query("pageParam") String pageParam, @Query("albumId")
            int albumId,@Query("order") int order);//专辑列表


    @POST(API.LIKE_SHORT_VIDEO)
    Flowable<HttpResult> likeVideo(@Body RequestBody body);


    @GET(API.GET_VIDEO_SEARCH_HOT)
    Flowable<HttpResult<List<String>>> getVideoSearchHot();

    @GET(API.GET_VIDEO_SEARCH)
    Flowable<HttpResult<VideoSearchDto>> getVideoSearch(@Query("content") String content, @Query("pageParam") String pageParam);

    @GET(API.GET_SHORT_VIDEO_COMMENT)
    Flowable<HttpResult<BaseListDto<ShortVideoCommentDto>>> getShortVideoComment(@Query("movieId") String movieId,
                                                                                   @Query("pageParam") String pageParam,
                                                                                   @Query("parentId") String parentId,
                                                                                   @Query("type") String type);

    @POST(API.COMMENT_SHORT_VIDEO)
    Flowable<HttpResult<ShortVideoCommentDto>> comment(@Body RequestBody body);

    @GET(API.GET_SMALL_VIDEO_LIST)
    Flowable<HttpResult<BaseListDto<ShortVideoCommentDto>>> getSmallVideoCommentList(@Query("type") String type,
                                                                                       @Query("videoId") String videoId,
                                                                                       @Query("pageParam") String pageParam);

    @GET(API.GET_SMALL_VIDEO_LIST)
    Flowable<HttpResult<BaseListDto<ShortVideoCommentDto>>> getSmallVideoSecondCommentList(@Query("parentId") String parentId,
                                                                                             @Query("type") String type,
                                                                                             @Query("videoId") String videoId,
                                                                                             @Query("pageParam") String pageParam);

    @POST(API.LIKE_SMALL_VIDEO)
    Flowable<HttpResult> likeSmallVideo(@Body RequestBody body);

    @POST(API.COMMENT_SMALL_VIDEO)
    Flowable<HttpResult<ShortVideoCommentDto>> commentSmallVideo(@Body RequestBody body);

    @GET(API.GET_MY_ALBUM_LIST)
    Flowable<HttpResult<BaseListDto<AlbumDto>>> getMyAlbumList(@Query("pageParam") String pageParam);

    @POST(API.ADD_ALBUM)
    Flowable<HttpResult> addAlbum(@Body RequestBody body);

    @POST(API.ADD_SHORT_VIDEO_LIKE)
    Flowable<HttpResult> addShortVideoLike(@Body RequestBody body);

    @POST(API.ADD_SHORT_VIDEO)
    Flowable<HttpResult> addShortVideo(@Body RequestBody body);

    @POST(API.ADD_SMALL_VIDEO)
    Flowable<HttpResult> addSmallVideo(@Body RequestBody body);

    @GET(API.GET_SMALL_VIDEO_MENU)
    Flowable<HttpResult<BaseListDto<SmallVideoMenuDto>>>getSmallVideoMenu(@Query("pageParam") String pageParam);

    @GET(API.ADD_SHORT_VIDEO_PLAY_NUM)
    Flowable<HttpResult>addShortVideoPlayNem(@Path("id") int movieId);

    @GET(API.ADD_SMALL_VIDEO_PLAY_NUM)
    Flowable<HttpResult>addSmallVideoPlayNem(@Path("id") int movieId);

    @POST(API.DEL_SHORT_VIDEO)
    Flowable<HttpResult> delShortVideo(@Body RequestBody body);

    @POST(API.DEL_SMALL_VIDEO)
    Flowable<HttpResult> delSmallVideo(@Body RequestBody body);

}
