package com.fengwo.module_live_vedio.api;

public class API {
    public static final String CONFIG = "/api/live/stickers/config";
    public static final String GET_LIVE_ZHUBO_MENU = "api/live/live_channel_menus";
    public static final String GET_LIVE_ZHUBO_LIST = "api/live/user_play/get_lives_new";
    public static final String ENTER_LIVINGROOM = "api/live/user_play/enter_live";    //进入直播间
    //    public static final String GET_ACTIVIY_INFO_REWARD = "/api/live/activity/last_activity_info";
    public static final String GET_ACTIVIY_INFO_REWARD = "api/live/activity/last_activity_info";

    public static final String GET_ACTIVIY_MY_HOUR= "api/virtual_goods/gift/channel_rank";
    //    public static final String ENTER_LIVINGROOM = "api/live/user_play/enter_live_new/{channelId}";
    public static final String GET_ACTIVIY_ADD_STICERS= "api/live/stickers";

    public static final String GET_ACTIVIY_ADD_STICERS_CK= "api/live/stickers/{liveChannelId}";

    public static final String LEAVE_LIVINGROOM = "api/live/user_play/exit_room/{channelId}";
    public static final String ROOM_PEOPLE = "api/live/user_play/online_users/{roomId}";
    public static final String START_LIVE = "api/live/startLive";
    public static final String GET_GIFTS = "api/virtual_goods/gift/lists";
    public static final String SEND_GIFT = "api/virtual_goods/gift/send";

    public static final String SEND_ActivityGIFT = "api/virtual_goods/activity/gifts_discount_package/send";

    public static final String TOUTIAO_GIFT_SEND = "api/virtual_goods/gift_package/send";
    public static final String SINGLE_RANDOM_PK = "/api/pk/pkSingle/pk_single_queue";
    public final static String GET_RECHARGE_LIST = "api/virtual_goods/package/get_list";//充值列表
    public final static String GET_PENDANT_LIST = "/api/live/stickers/config";//挂件列表
    public static final String CLOSE_LIVE_PUSH = "/api/live/closeLive";
    public static final String CLOSE_MATCH_SINGLE_PK = "/api/pk/pkSingle/pk_cancel";//取消单人随机匹配
    public static final String QUERY_PK_STATUS = "api/live/user_play/enterLive/{roomId}";//查询当前主播pk状态
    public static final String SINGLE_INVITE_PK = "api/pk/pkSingle/pk_single_friend_queue/{friendId}";//单人开始pk（好友接受邀请后，发起方，调用此方法）
    public static final String TEAM_RANDOM_PK = "api/pk/pkGroup/pk_Group_queue";//团队随机匹配

    public static final String ADD_ATTENTION = "api/user/attention/attention/{id}";
    public static final String RANK_SINGLE_PK = "api/pk/pkRank/singleRank";//PK单人排行榜
    public static final String RANK_TEAM_PK = "api/pk/pkRank/groupRank";//PK团队排行版
    public static final String RANK_GUILD_PK = "api/pk/pkRank/guildRank";//PK公会排行版
    public static final String RANK_TUHAO = "api/virtual_goods/gift/send_rank";//土豪榜（名人榜）
    public static final String ATTENTION_LIST = "/api/user/attention/attention_page";//关注列表
    public static final String TOP_ZHUBO = "api/virtual_goods/gift/receive_rank";
    public static final String GET_BANNER = "api/base/banners";
    public static final String GET_RECOMMENT = "api/live/user_play/recommend_users/{value}";
   // public static final String GET_EACH_ATTENTION = "api/user/attention/each_attention/{id}";
    public static final String GET_EACH_ATTENTION = "api/user/attention/private_letter/each_attention/{id}";
    public static final String GUARD_LIST_BY_ID = "api/virtual_goods/user/guard_list";//获取用户守护列表
    public static final String RESTRICT_LIST = "api/live/live_channel_restrict?pageParam=1,100";//踢人列表
    public static final String ROOM_MANAGER = "api/live/live_channel_manages";//房管列表
    public static final String MOVIE_ROOM_MANAGER = "/api/live/live_channel_manages/{userId}";//移除房管列表
    public static final String MOVIE_STRICK = "api/live/live_channel_restricts/remove";//移除踢禁列表
    public static final String THIS_CONTRIBUTE = "/api/live/user_data_list/receive_list";//本场贡献
    public static final String ADD_RESTRICTS = "/api/live/live_channel_restricts";//踢人 禁言
    public static final String GET_USER_INFO = "api/aggregation/getUserInfo";//根据用户id获取用户信息
    //    public static final String GET_USER_INFO_NEW = "api/aggregation/getUserBaseInfo";//根据用户id获取用户信息
    public static final String GET_USER_INFO_NEW = "api/aggregation/getUserBaseInfo/v2";//根据用户id获取用户信息
    public static final String THIS_GIFTS = "/api/live/user_data_list/channel_get_gifts";//收礼列表
    public static final String GET_GUARD_LIST = "api/virtual_goods/guard_privilege_level/list";
    public static final String ADD_ROOM_MANAGER = "api/live/live_channel_manages/{userId}";//添加房管
    public static final String ADD_BLACKLIST = "api/user/blacklist/blacklist/{id}";//拉黑
    public static final String GET_COMMENT_WORD = "api/base/common_word";//获取快捷聊天
    public static final String GET_RECEIVE_LIST = "api/live/user_data_list/receive_list";//主播贡献榜
    public static final String BUY_ORDER = "api/virtual_goods/package/buy_order_new";//购买充值套餐
    public static final String BUY_GUARD_ORDER = "api/virtual_goods/guard/buy_order";//购买守护套餐
    public static final String REMOVE_ATTENTION = "api/user/attention/attention/{id}";//取消关注

    public static final String BROKER_RANK = "api/broker/rank";//经纪人榜单
    public static final String PROFIT_TODAY = "api/broker/my/today/profit";//我今日收益
    public static final String SHARE_INFO = "api/broker/share/info";//分享信息获取

    public static final String SEARCH_CHANNEL = "api/live/user_play/search_channel/{keyword}";
    public static final String INVITE_FRIEND_PK = "api/pk/pkSingle/invite_friends/{friendId}";//邀请好友进行pk(单人）
    public static final String RESPONSE_FRIEND_AGREE = "api/pk/pkSingle/agree/{friendId}";//同意
    public static final String RESPONSE_FRIEND_REFUSE = "api/pk/pkSingle/refuse/{friendId}";//拒绝
    public static final String CREATE_ROOM = "api/pk/pkGroup/group_room";//创建房间
    public static final String INVITE_FRIEND_PK_TEAM = "api/pk/pkGroup/invite_friends";//邀请好友进行pk（多人）
    public static final String ATTENTION_HOST_LIST = "api/pk/pkBase/invites";//pk邀请列表
    public static final String SINGLE_ADD_GROUP_RANDOM = "api/pk/pkGroup/randomStart";//单人随机加入团队
    public static final String CANCEL_INVITE_FRIEND = "api/pk/pkSingle/cancel_invite";//取消好友邀请
    public static final String INTO_GROUP_ROOM = "api/pk/pkGroup/into_group_room/{teamId}";//进入房间
    public static final String CANCEL_GROUP_PK = "api/pk/pkGroup/pk_cancel";//团队取消匹配
    public static final String LEAVE_PK_ROOM = "api/pk/pkGroup/group_room/{teamId}";//离开匹配房间
    public static final String CHECK_PK_START = "api/pk/pkBase/pkStart/{id}";//查询pk是否开始
    public static final String CHECK_PK_RESULT = "api/pk/pkBase/pkResult/{id}";//查询pk结果

    public static final String SEND_DANMU_MSG = "api/virtual_goods/send/bullet_screen";//发送弹幕消息
    public static final String GET_MAP_POI = "api/pk/pkCity/city_base";//获取工会地图 工会poi信息
    public static final String GET_SINGLE_PK_LIST = "/api/pk/pkList/single_pk"; // 获取单人PK列表
    public static final String GET_GROUP_PK_LIST = "/api/pk/pkList/group_pk"; // 获取组队PK列表

    public static final String GUILD_CITY_LIST = "api/pk/pkCity/guild_city/{id}";//获取工会地图 工会poi信息
    public static final String GUILD_CHALLENGE = "api/pk/pkGuild/challenge";//发起公会挑战

    public static final String ON_OFF_PK = "/api/pk/pkBase/pkState/{state}";//开启/关闭pk
    public static final String CHECK_RECEIVE_PK_MSG = "api/pk/pkBase/pkState"; //查询是否开启接收pk信息
    public static final String HOST_CONNECTION_LINE = "api/live/conn_line";// 主播断线重连


    public static final String TOUTIAO_LIST = "api/virtual_goods/headnews/gift/lists";//头条礼物列表
    public static final String TOUTIAO_LIST_NEW = "api/virtual_goods/gift_package/list";//头条礼物列表

    public static final String SEND_FEIZAO = "api/virtual_goods/special/gift/lists";//赠送肥皂
    public static final String GET_EXIST_TOUTIAO = "/api/virtual_goods/gift/headnews/info"; // 获取存在的头条信息
    public static final String GET_CHANNEL_INFO = "/api/live/channel_info/share"; // 获取房间信息

    public static final String GET_ADD_NOTICE = "api/live/notice/room_notice_add"; //主播添加公告
    public static final String GET_INFO_NOTICE = "api/live/notice/play_notice_info"; //轮训挂件

    public static final String GET_EDL_NOTICE = "api/live/notice/room_notice_remove"; //主播删除公告

    public static final String GIFT_EFFECT = "/api/virtual_goods/gift/effects";//获取礼物特效连击 信息
    public static final String IS_ATTENTION = "api/user/attention/is_attention/{id}"; // 是否已关注
    public static final String PK_SURRENDER = "/api/pk/pkBase/surrender"; // pk 认输
    public static final String PK_TYPE = "/api/pk/pkBase/pkType/{id}"; // pk 状态
    public static final String GET_DETAIL = "api/live/activity/get_detail"; //获取主播活动进度详情
    public static final String INVITE_LIST = "api/broker/my/invite/list";//邀请用户列表

    public static final String IS_BLACK = "api/user/blacklist/is_black/{id}";//是否已拉黑

    public static final String REJECT_RECONNECT = "api/live/refuse_conn_line";//主播拒绝断线重连

    public static final String GET_GIFT_BY_TYPE = "api/virtual_goods/activity/gifts";

    public static final String ENTER_LIVE_DEL = "api/live/stickers/{id}";

    public static final String ENTER_ROOM_PK_ACTIVITY = "api/live/activity/enter_activity_pk_room";//pk排位赛活动

    public static final String ENTER_ROOM_ACTIVITY = "api/live/activity/enter_activity_room";//普通活动进入直播间信息


    public static final String ENTER_ROOM_ACTIVITY_NEW = "api/live/activity/enter_live_room_activity/{channelId}";//普通活动  新

    public static final String GET_ACTIVITY_SHARE_INFO = "api/broker/share/info/activity";

    public static final String GET_PK_RANK_SHARE_INFO = "api/pk_ranking/share/share/pk/activity";

    public static final String GET_USER_MEDAL = "api/live/service/activity/get_user_leader";//获取主播活动勋章

    public static final String WISH_COMMIT = "api/virtual_goods/wish_new";
    public static final String AD_URL = "api/virtual_goods/activity/address";//广告地址接口

    public static final String PK_CONTRIBUTE_INFO = "api/pk/pkSingle/pkCtrbInfo/{id}";//获取pk贡献榜数据

    //1023
    public static final String GET_ANCHOR_WISH_INFO ="api/virtual_goods/wish/info";//心愿信息-主播端
    public static final String GET_USER_WISH_INFO ="api/virtual_goods/wish/speed/{anchorId}";//心愿进度-用户端
    public static final String GET_WISH_REPAY ="api/virtual_goods/wish/wish_repay";//心愿进度-用户端

    //七夕活动
    public static final String GET_PLANE_LIST_ANCHOR ="api/live/plane_box/get_anchor_plane";//获取主播端全部航仓
    public static final String GET_PLANE_LIST_USER ="api/live/plane_box/get_user_plane";//获取用户端全部航仓
    public static final String GET_BOX_GIFT_INFO = "api/live/activity/box_gift_info";//航仓详情
    public static final String OPEN_PLANE_GIFT = "api/live/plane_box/plane_random_gift";//打开航仓宝箱

    public static final String OPEN_TREE_GIFT = "api/virtual_goods/bag/lotto/tree";//圣诞树抽奖




    public static final String GET_USER_BOX = "api/live/treasure_box/get_user_box";//获取直播间宝箱
    public static final String GET_USER_ACTIVITY_INFO = "api/live/activity/get_user_activity_info";//当前用户主页活动勋章边框信息
    public static final String GET_USER_LIVE_ACTIVITY_INFO = "api/live/activity/get_user_live_activity_info";//主播直播间活动勋章边框信息
    public static final String OPEN_CAR = "api/virtual_goods/user/use/my_motoring";//开启/关闭座驾
    public static final String LIVE_HOUR_RANK = "/api/virtual_goods/gift/channel_rank";


    public static final String GET_USER_LIVE_ACTIVITY_FESTVAL = "api/virtual_goods/activity/gifts_new";//新增中秋活动  现在是万圣节活动 1分钟谈一次引导送礼弹窗
    public static final String GET_USER_LIVE_ACTIVITY_DISCOUNT = "api/virtual_goods/activity/gifts_discount_package";//同上 这个是套餐礼物

    public final static String GET_USER_CENTER = "api/aggregation/myCenter";//获取我的详细用户信息
    public static final String GET_BLESSING = "/api/virtual_goods/activity/blessings";//活动祝福语

    public static final String PUT_TEENAGER_UPDATE_INFO = "api/community/teenager/updateInfo";//更新青少年模式
    public static final String GET_TEENAGER_VIDEO_LIST = "api/community/teenager/page";//获取青少年模式视频
    public static final String PUT_TEENAGER_ADD_VIEW = "api/community/teenager/view";//青少年模式播放计数


    public static final String GET_WISH_LIST = "/api/live/activity/wishWall/list";//许愿墙列表的数据

    public static final String POST_WISHING = "/api/live/activity/wishing";//活动 马上许愿


    public static final String PUT_USE_CAR = "/api/virtual_goods/bag/use/motor";//使用座驾

    public static final String PUT_USE_VIP = "/api/virtual_goods/bag/use/nobility";//使用贵族

    public static final String POST_GIVING = "/api/live/activity/prop/send";//赠送活动道具礼物接口

    public static final String POST_GIVING_BAG = "/api/virtual_goods/bag/gift/send";//赠送背包礼物

    public static final String GAME_LIST = "/api/game/lotto/list";//游戏列表

    public static final String PACKET_COUNT = "/api/game/gameRedpacket/listPacket";//直播间红包数量


    public static final String POST_RED_PACKAGE_SEND = "api/game/gameRedpacket/lottery";//发红包
    public static final String POST_RED_PACKAGE_ORDER_IN = "/api/game/gameRedpacket/income";//我的收益
    public static final String POST_RED_PACKAGE_ORDER_OUT = "/api/game/gameRedpacket/outcome";//我发出的记录
    public static final String GET_RED_PACKAGE_MY_STATISTICS = "/api/game/gameRedpacket/myStatistics";//我的记录

    public static final String PACKET_GIVE = "/api/game/gameRedpacket/getPacket";//直播间红包数量

    public static final String PACKET_RESULT = "/api/game/gameRedpacket/getResult";//查询红包结果

    public static final String DROP_LIVE_STREAM = "/api/live/tencentApi/dropLiveStream"; //主播端 推流断线重连三次失败就调用当前的接口

    public static final String GET_LIVE_STATUS = "/api/live/anchor/isCanReconn";//主播端 异常断波 点击需要按钮时的操作
    public static final String RED_PACKET_CLICK = "/api/game/gameRedpacket/clickPacket";//红包拆包
}
