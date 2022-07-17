package com.fengwo.module_vedio.api;

public class API {
    public final static String GET_MENU = "api/video/video/getVideoDividePage";//	string 分类主类:video-影视,movie-短片,short-小视频,label-筛选标签
    public final static String GET_SMALL_LIST = "api/video/short/getInfoPage";//小视频首页 列表

    public final static String GET_SHORT_LIST = "api/video/movie/getMoviePage";//短片 获取

    public final static String SMALL_COMMENT = "api/video/short/getCommentPage";//小视频评论

    public final static String SMALL_DETAIL_LIKE = "api/video/short/addVideoLikes";//小视频详情页点赞

    public final static String ADD_SMALL_VIEWS = "api/video/short/addShortViews";//添加小視頻瀏覽量

    public final static String ADD_SMALL_COMMENT = "api/video/short/addShortComment";//添加小小视频评论

    public final static String GET_SHORT_DETAIL = "api/video/movie/getMovieInfoById";//获取短片详情

    public final static String GET_SHORT_COMMENT = "api/video/movie/getCommentPage";//获取短片评论

    public final static String SEARCH = "api/video/video/getMovieAndShort";//全局搜索

    public final static String GET_FILTER_LABS = "api/video/video/getVideoDivides";//获取筛选

    public final static String SET_SHORT_FAVOURITE = "api/video/movie/addMovieFavorite";//添加短片收藏

    //---------------------------
    public final static String GET_BANNER = "api/base/base_adverts";//首页banner
    /**
     * 获取影视分类
     */
    public static final String GET_HOME_CATEGORY = "/api/video/short/getShortVideoMenuPage";

    /**
     * 获取影视搜索 - 热搜
     */
    public static final String GET_VIDEO_SEARCH_HOT = "/api/video/search_hot";
    /**
     * 获取影视搜索 - 搜索
     */
    public static final String GET_VIDEO_SEARCH = "/api/video/video_search";

    /**
     * 获取广告
     */
    public static final String GET_BANNER_AD = "/api/base/base_adverts";

    /**
     * 获取小视频列表
     */
    public static final String GET_SHORT_VIDEO_LIST = "/api/video/short/getVideoInfoPage";

    /**
     * 获取专辑列表
     */
    public static final String GET_ALBUM_LIST = "/api/video/movie/getVideoAlbumPage";

    /**
     * 获取影视模块短视频列表
     */
    public static final String GET_SHORT_VIDEO_LIST_NEW = "/api/video/movie/getVideoMoviePage";

    /**
     * 小视频点赞
     */
    public static final String LIKE_SHORT_VIDEO = "/api/video/movie/addMovieLikes";

    /**
     * 获取评论列表
     */
    public static final String GET_SHORT_VIDEO_COMMENT = "/api/video/movie/getCommentPage";

    /**
     * 短视频评论
     */
    public static final String COMMENT_SHORT_VIDEO = "/api/video/movie/addMovieComment";

    /**
     * 获取短视频列表
     */
    public static final String GET_SMALL_VIDEO_LIST = "/api/video/short/getCommentPage";

    /**
     * 小视频(评论)点赞
     */
    public static final String LIKE_SMALL_VIDEO = "/api/video/short/addVideoLikes";

    /**
     * 小视频评论
     */
    public static final String COMMENT_SMALL_VIDEO = "/api/video/short/addShortComment";

    public static final String GET_MY_ALBUM_LIST = "/api/video/movie/getAlbumPage"; //获取个人专辑

    public static final String ADD_ALBUM = "/api/video/movie/addAlbum"; //添加专辑

    public static final String ADD_SHORT_VIDEO_LIKE = "/api/video/movie/addMovieLikes"; //短片点赞

    public static final String ADD_SHORT_VIDEO = "/api/video/movie/addMovieInfo"; //新增短片

    public static final String ADD_SMALL_VIDEO = "/api/video/short/addShortVideo"; //新增小视频

    public static final String GET_SMALL_VIDEO_MENU = "/api/video/short/getShortVideoMenuPage"; //获取小视频分类

    public static final String ADD_SHORT_VIDEO_PLAY_NUM = "/api/video/movie/addMovieView/{id}"; //添加播放量(短片）

    public static final String ADD_SMALL_VIDEO_PLAY_NUM = "/api/video/short/addShortViews/{id}"; //添加播放量(小视频）

    public static final String DEL_SHORT_VIDEO = "/api/video/movie/delMovieInfo"; //删除短片

    public static final String DEL_SMALL_VIDEO = "/api/video/short/delShortVideo"; //删除小视频
}
