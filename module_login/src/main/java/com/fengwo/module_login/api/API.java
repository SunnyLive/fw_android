package com.fengwo.module_login.api;

public class API {
    public final static String SEND_CODE = "api/user/login/v2/send_Message";//1分钟1次，1小时5次，1天10次，5分钟有效,login:登录 pwd:找回密码 mobile:绑定手机号 bank:绑定银行卡 name:实名认证

    public final static String LOGIN = "api/user/login/login_code";
    public final static String AUTO_LOGIN = "api/user/login/login_ali";
    public final static String THIRD_LOGIN = "api/user/login/login_Authorization_new";

    public final static String GET_USER_CENTER = "api/aggregation/myCenter";//获取我的详细用户信息
    public final static String GET_USER_INFO = "api/user/member/member_info";//获取我的用户信息
    public final static String GET_USER_FANSNUMBER = "api/user/attention/fans";
    public final static String GET_USER_ATTENTIONS = "api/user/attention/attention_num";
    public final static String GET_USER_FRIENDS = "api/community/user/getUserFriendNum";

    public final static String GET_MINE_SMALL_VEDIO = "api/video/short/getPersonInfoCount";
    public final static String GET_MINE_SMALL_ZAN_VEDIO = "api/video/short/likeVideoCount";
    public final static String GET_MINE_SHORT_VEDIO = "api/video/movie/getMovieCount";

    public final static String GET_HUAFEN_LEVEL = "api/user/level/level";
    public final static String GET_ALL_TASK = "api/user/task/task";
    public final static String GET_MY_TASK = "api/user/TaskLog/task_log_my";

    public final static String GET_ALL_PRIVILEGE = "api/user/privilege/privileges";
    public final static String GET_MY_PRIVELEGE = "api/user/privilege/privilege";

    public final static String GET_WALLET_INFO = "api/virtual_goods/user_wallet_info";

    public final static String FRIENDS_LIST = " /api/user/attention/listFriend";//我的好友列表
    public final static String ATTENTION_LIST = "api/user/attention/attention_page/v2";//我的关注列表
    public final static String FANS_LIST = "api/user/attention/fansPage/v2";//获取粉丝列表

    public final static String REAL_NAME = "api/user/idCard/idCard_new";//实名认证

    public final static String REAL_IDCARD = "api/user/idCard/idCard_withdraw";//提现认证（实名认证）

    public final static String REAL_NAME_FAILED_REASON = "api/user/idCard/idCard_remark";//实名认证失败理由

    public final static String GET_RECHARGE_LIST = "api/virtual_goods/package/get_list";//充值列表

    public final static String GET_RECHARGE_RECORDS = "api/virtual_goods/user_balance_add_log";//获取充值记录
    public final static String GET_WITHDRAW_RECORDS = "api/virtual_goods/withdraw/records";//获取提现记录

    public final static String GET_BANK_LIST = "api/user/bankList/bankList";//获取银行列表
    public final static String BIND_CARD = "api/user/bank/bank";//绑定银行卡

    public final static String BLACK_LIST = "api/user/blacklist/blacklist_page";//黑名单

    public final static String DELETE_BLACK = "/api/user/blacklist/blacklist/{id}";//取消黑名单

    public final static String ADD_BLACK = "/api/user/blacklist/blacklist/{id}";//拉黑

    public final static String IS_REALNAME = "api/user/idCard/is_idCard";//0 1

    public final static String LOGOUT = "api/user/logout/logout";//登出

    public final static String UPDATE_USERINFO = "api/user/member/member_info";//更新用户信息

    public final static String USER_ADD_PHOTOS = "api/user/photos";//新增照片墙
    public final static String USER_DELETE_PHOTOS = "api/user/photos/{id}";//删除照片墙
    public final static String USER_UPDATE_PHOTOS = "/api/user/photos";//更新用户照片
    public final static String USER_UPDATE_PHOTOS_ORDER = "api/user/photos/order";//更新用户照片排序


    public static final String ALL_CAR_LIST = "api/virtual_goods/motoring/lists";//所有座驾列表
    public static final String MY_CAR_LIST = "api/virtual_goods/user/motoring_list";//我的座驾列表
    public static final String BUY_CAR = "api/virtual_goods/buyOrder";//购买续费座驾
    public static final String OPEN_CAR = "api/virtual_goods/user/use/my_motoring";//开启/关闭座驾
    public static final String CAR_DETAIL = "api/virtual_goods/manage/motorings/{id}";//获取座驾信息
    public static final String MY_PROFIT = "api/broker/my/profit";//"我的收益"
    public static final String MY_PROFIT_DETAIL = "/api/virtual_goods/my/profit_logs";//"收益明细"
    public static final String GUARD_ME = "/api/virtual_goods/guard/me";//"守护我的"
    public static final String MY_GUARD = "/api/virtual_goods/guard/users";//"我守护的"

    public static final String GET_SERVICE_KEY = "api/base/sys_configs/{key}";//获取服务器配置参数

    public static final String BIND_MOBILE = "/api/user/login/login_Authorization_mobile";//绑定手机号
    public static final String MY_USING_CAR = "/api/virtual_goods/user/motoring";

    public static final String BUY_ORDER = "api/virtual_goods/package/buy_order_new";//购买充值套餐
    public static final String CASH_OUT_PAGE = "api/virtual_goods/profit/withdraw_info";//提现页面信息
    public static final String CASH_OUT_COMMIT = "api/virtual_goods/profit/withdraw";//提现提交
    public static final String LIVE_LENGTH = "api/live/live_channel_logs";//直播时长（开播记录）

    public static final String WATCHHISTORY = "api/live/live_channel_look_logs";

    public static final String RECEIVEGIFT = "api/virtual_goods/gift/user_receive_record";//用户收礼记录
    public static final String COMPLAINT_COMMIT = "api/base/complaints";//投诉提交
    public static final String SEND_GIFT_HISTORY = "/api/virtual_goods/gift/user_send_record"; // 用户送礼记录
    public static final String REPORT_LABEL = "api/base/base_reports"; // 获取举报标签
    public static final String REPORT_COMMIT = "api/base/report_user_logs"; // 举报提交
    public static final String GET_USER_DETAIL = "api/aggregation/getUserInfo"; // 获取某个用户的信息
    public static final String GET_RANK_LEVEL = "/api/live/channel_level";// 获取用户直播等级
    public static final String GET_RECEIVE_LIST = "/api/live/user_data_list/receive_list";//获取当日贡献值  传4

    public static final String GET_NOBILITY_TYPE_LIST = "/api/virtual_goods/manage/level/list";// 获取贵族类型列表
    public static final String GET_NOBILITY_PRIVILEGE = "/api/virtual_goods/nobility_privilege_level/list"; //  获取贵族套餐特权
    public static final String BUY_NOBILITY = "/api/virtual_goods/nobility/buy_order"; // 购买贵族套餐

    public static final String GET_BANK_BIND_STATUS = "/api/user/bank/is_bank"; // 获取银行卡绑定状态
    public static final String GET_BIND_BANK = "/api/user/bank/my_Bank"; // 获取已绑定银行卡信息

    public static final String GONGHUI_LIST = "/api/broker/family/list";//工会列表
    public static final String APPLY_GONGHUI = "/api/broker/apply/family";//申请入驻公会"

    public static final String GET_USER_CARD = "/api/community/discuss_circle_card/user_create";//获取用户发布的卡片
    public static final String GET_USER_LIKE_CARD = "/api/community/CardAction/card_likes";//用户卡片点赞列表
    public static final String GET_USER_VEDIO = "/api/video/short/getInfoPage";//获取用户发布的小视频
    public static final String GET_USER_LIKE_VEDIO = "/api/video/short/getLikeVideoPage";//我的点赞小视频分页
    public static final String GET_USER_MOVIE = "/api/video/movie/getAlbumPage/{id}";//获取用户发布的小视频
    public static final String GET_USER_LIKE_MOVIE = "/api/video/movie/user_likes_page";//用户点赞列表

    public static final String GET_USER_LIVE_STATUS = "/api/live/channel_status/{userId}";//获取直播状态


    public static final String GET_IS_ATTENTION = "api/user/attention/is_attention/{id}";//查看是否关注
    public static final String GET_MY_COMPLAINT = "api/base/my_complaint/info";//获取我的投诉列表
    public static final String GET_VIDEO_TIME = "api/bunko/2030014";//获取视频时长
    // 上传经纬度
    public static final String MODIFY_LNGLAT = "/api/user/member/modify/lnglat";
    //获取视频总时长
    public static final String VIDEO_LENGTH = "api/bunko/2030010";
    public static final String GET_MODIFY_NAME_RULE = "/api/user/member/modify/rule";//获取改名规则
    public static final String MODIFY_NAME = "/api/user/member/modify/nickname";//改名
    public static final String COMPLAINT_RECORD = "/api/base/complaints/reply/list/{id}";//用户投诉回复信息列表
    public static final String COMPLAINT_REPLY = "/api/base/complaints/reply";//用户投诉回复
    public static final String SYS_CONFIG = "api/base/sys_configs/{key}";//获取系统设置
    public static final String DELETE_MINE_CARD = "api/community/discuss_circle_card_remove";//删除卡片(个人详情)
    public static final String EXIT_LOG = "api/user/loginLog/exit_log";//退出日志
    public static final String ACC_DESTROY = "api/user/write_off/apply_account_off";//注销账号
    public static final String ACC_DESTROY_CANCEL = "api/user/write_off/cancel_account_off";//取消注销账号
    public static final String GET_MOBILE = "api/user/write_off/account_off/mobile/{userId}";//根据userId获取手机号

    public final static String GREET_DISCUSS = "/api/community/discussGreet";//打招呼
    public final static String GREET_TIPS_DISCUSS = "/api/bunko/2042001";//打招呼提示语列表

    public final static String GET_TAGS = "/api/user/tags/{tagTypeCode}";   //查询标签集 多个标签用”,“分隔；标签类型标识: IDEAL_TYPE-理想型

    public final static String GET_GIFT_WALL = "api/virtual_goods/gift/wall";   //获取本月礼物墙

    public final static String GET_EXPERT_GIFT_WALL = "/api/bunko/2030031";   //获取本月礼物墙  达人礼物墙

    public final static String SAVE_USER_INFO = "api/user/member/modify/user_info";   //保存用户头像和昵称信息

    public final static String SKIP_USER_INFO = "api/user/member/modify/user_info/skip";   //跳过昵称头像修改

    public final static String GET_MINE_CARD_LIST = "/api/community/find_user_dynamic";//查看动态（最新）

    public static final String MINE_SHARE_CARD = "api/community/shareCard/{id}"; //分享的接口

    public static final String MINE_CARD_LIKE = "api/community/CardAction/card_like/{id}"; //点赞的接口

    public static final String MINE_CARD_DETAIL = "/api/community/findNearCard/{id}"; //个人动态详情

    public static final String MINE_CARD_DETAIL_STICK = "/api/community/discuss_circle_card_top"; //个人动态详情_置顶

    public static final String MINE_CARD_DETAIL_AUTHORITY = "/api/community/discuss_circle_card_power"; //个人动态详情_权限管理

    public static final String GET_COMMENT_LIST = "/api/community/CardAction/comments"; // 获取评论列表

    public static final String SEND_COMMENT = "/api/community/CardAction/comment"; // 发表评论

    public static final String LIKE_COMMENT = "/api/community/CardAction/comment_like/{id}"; // 评论点赞/取消点赞

    public static final String GET_UNION_INFO = "/api/aggregation/my_family";    // 获取我的公会

    public static final String SEARCH_UNION_EXPERT = "/api/bunko/family/list";    // 搜索达人公会

    public static final String APPLY_UNION_EXPERT = "/api/bunko/family/apply";    // 申请达人公会

    public static final String GET_BACKPACK = "/api/virtual_goods/bag";           // 获取我的背包信息


    public static final String GET_VERIFY_TOKEN = "/api/user/idCard/aliyun/token";  // 获取实名认证token

    public static final String GET_VERIFY_RESULT = "/api/user/idCard/aliyun/result";    // 获取人脸识别结果

    public static final String POST_ANCHOR_ID_CARD = "/api/user/idCard/anchor_id_card";    // 上传主播身份证照片和形象照

    public static final String GET_VOICE_CHECK_RESULT = "/api/base/sides/sides_sync_voice";   // 获取语音检测结果

    public static final String GET_VERIFY_REVIEW = "/api/user/idCard/apply/manual";  //人工审核的接口


}
