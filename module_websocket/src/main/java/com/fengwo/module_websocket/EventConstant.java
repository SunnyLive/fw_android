package com.fengwo.module_websocket;

/**
 *
 */
public class EventConstant {

    public final static int chatLogin = 10000;//聊天登录
    public final static int chatLogout = 10001;//聊天退出
    public final static int saveAMessage = 20000;//保存聊天
    public final static int loopMessage = 30000;//循环获取一组(20条)聊天数据
    public final static int firstComeinMessage = 30001;//第一次进入聊调室，获取最近的20条数据
    public final static int serverMessage = 30002;//服务的推送的消息
    public final static int deleteAChatMessage = 40000;//删除一个聊天数据
    public final static int rebackAChatMessage = 40001;//撤回一个聊天数据
    public final static int deleteAllChatMessage = 50000;//获取聊天数据
    public final static int joinGroup = 10003;//加入群
    public final static int leaveGroup = 10004;//离开群
    public final static int levelUp = 100005;//等级提升
    public final static int attention = 100044;
    public final static int ACTIVITY_BROCAST = 100318;
    public final static int ACTIVITY_PRIVATE = 100319;

    public final static int getFriendList = 60000;//获取好友列表

    public final static int uploadAudioFiles = 60005;//聊天上传文件
    public final static int uploadImgFiles = 60007;//聊天上传文件

    public final static int getGroupList = 60008;//获取群组用户的信息
    public final static int broadcastMessage = 99999; // 广播消息
    public final static int SEND_GIFT = 11001;//本地发送礼物标记
    public final static int pendantview = 100387; // 贴图移动
    public final static int private_msg = 20001;//私人消息
    public final static int forbiden_someone = 100307;//禁言用户
    public final static int un_forbiden_someone = 100308;//解除禁言
    public final static int platform_forbiden = 100002;//全平台禁言
    public final static int un_platform_forbiden = 100003;//全平台解禁
    public final static int banned_login = 100004;//封号（登陆的用户退出登录）
    public final static int banned_host = 100302;//封禁主播
    public final static int warn_host = 100306;//警告主播
    public final static int update_hot = 100312;//更新热度值
    public final static int host_end_live = 100303;//主播下播 直播间接收到消息
    public final static int host_level = 100313;//主播掉线
    public final static int host_comeback = 100314;//主播掉线重连
    public final static int private_letter = 100008;// 禁/解 私信
    public final static int add_roomManager = 100316;// 加房管
    public final static int remove_roomManager = 100317;// 移除房管
    public final static int kicked_out = 100304;// 踢出

    public final static int EVENT_TEASE_HIM = 100390;    //撩他一下
    public final static int EVENT_ANCHOR_ATTENTION = 100388;    //关注主播
    public final static int EVENT_ANCHOR_SHARE = 100389;    //分享主播
    public final static int First_kill = 100386;//首杀
    public final static int EVENT_MVP = 100385;//MVP
    public final static int CMD_NOTICE = 100324;//收到公告
    public final static int CMD_NOTICEGG = 100325;//活动收到公告
    public final static int CMD_MSGWSJ = 100326;//活动收到公告  万圣节活动去围观公告
    public final static int CMD_ENDWSJ = 100327;//活动结束
    public final static int CMD_STARTWSJ = 100328;//活动开始start
    public final static int CMD_YEAR_MSG = 100329;//赠送许愿提醒消息
    public final static int CMD_WISH_MSG = 100330;//给主播许愿
    public final static int CMD_UP_MSG = 100331;//给主播许愿
    public final static int CMD_PACKET_NOTICE = 100901;//红包消息
    public final static int CMD_PACKET_DIALOG = 100900;//红包公告
    public final static String ACTIONID = "actionId";
    public final static String CMD_CHAT = "cmd_chat";//聊天
    public final static String CMD_GIFT = "cmd_gift";//礼物
    public final static String CMD_GIFT_BOARDCAST = "cmd_gift_boardcast";
    public final static String CMD_KICKEDOUT = "cmd_kickedOut";//踢出
    public final static String CMD_TEASE_HIM = "cmd_people_join";//撩一下
    public final static String CMD_SHUTUP = "cmd_shutUp";//禁言
    public final static String CMD_ROOMMANAGER = "cmd_roomManager";//添加房管
    public final static String CMD_OPENSHOUHU = "cmd_openShouHu";//开通守护
    public final static String CMD_TEXT_LEAVEROOM = "cmd_text_leaveRoom";//主播关闭直播
    public final static String CMD_INVITE_PK = "cmd_invite_pk";//邀请pk
    public final static String CMD_RESPONSE_INVITE_PK = "cmd_response_invite_pk";//回复pk邀请
    public final static String CMD_INVITATION_NOTICE = "anchorInviteUser";


    public static final int BIG_GIFT_PIAOPING = 100601;    //大礼物全服飘屏
    public static final int EVENT_HOUR_RANK = 100604;    //小时榜更新
    public static final int EVENT_HOUR_XYD = 100605;    //心愿单更新

    public final static int recent_visitor_event = 100717; // 最近访客通知
    public final static int interact_event = 100716; // 互动通知通知消息
    public final static int appoint_event = 100718; // 约会助手通知消息（本地造的 服务器走的是文播socket 没事件 这里自己造的用于数据库发送者id）
    public final static int system_event = 100715;//系统消息
    public final static int greet_event = 100719;//打招呼
    public final static int comment_event = 100721;//评论被拒 系统消息
    public final static int official_news = 100722;//官方消息
    /**
     * kafka PK 积分通知 编号
     */
    public static final int KAFKA_MSG_ID_PK_POINT = 100357;
    /**
     * kafka PK 时间校验 编号
     */
    public static final int KAFKA_MSG_ID_PK_START_TIME = 100358;
    /**
     * kafka PK 结束通知
     */
    public static final int KAFKA_MSG_ID_PK_END = 100359;
    /**
     * kafka 单人 PK 匹配结果编号
     */
    public static final int KAFKA_MSG_ID_SINGLE_RESULT = 100360;
    /**
     * kafka 多人 PK 匹配结果编号
     */
    public static final int KAFKA_MSG_ID_GROUP_RESULT = 100361;
    /**
     * kafka PK 进行中/惩罚中异常编号
     */
    public static final int KAFKA_MSG_ID_PK_ING_ERROR = 100362;
    /**
     * kafka PK 单人结果通知 编号
     */
    public static final int KAFKA_MSG_ID_PK_SINGLE_RESULT = 100363;
    /**
     * kafka PK 团队结果通知 编号
     */
    public static final int KAFKA_MSG_ID_PK_GROUP_RESULT = 100364;
    /**
     * kafka 多人PK 进行中/惩罚中异常编号 掉线
     */
    public static final int KAFKA_MSG_ID_GROUP_PK_ING_ERROR = 100366;
    /**
     * kafka 多人PK 用户提前结束PK，离开房间通知
     */
    public static final int KAFKA_MSG_ID_GROUP_PK_LEAVE = 100367;
    /**
     * kafka 多人PK 用户掉线导致对手提前结束PK通知
     */
    public static final int KAFKA_MSG_ID_GROUP_PK_ERROR_TO_OBJECT = 100368;
    /**
     * kafka PK 战队结果通知 编号
     */
    public static final int KAFKA_MSG_ID_PK_GUILD_RESULT = 100365;
    /**
     * kafka 战队PK，准备倒计时消息通知
     */
    public static final int KAFKA_MSG_ID_GUILD_PK_READY = 100369;
    /**
     * kafka 战队PK，开始消息通知
     */
    public static final int KAFKA_MSG_ID_GUILD_PK_START = 100370;
    /**
     * kafka 战队PK，未开始(异常)消息通知
     */
    public static final int KAFKA_MSG_ID_GUILD_PK_ERROR_READY = 100371;

    /**
     * kafka PK 团队好友邀请通知
     */
    public static final int KAFKA_MSG_ID_PK_GROUP_INVITE = 100373;
    /**
     * kafka PK 团队房间成员变化通知
     */
    public static final int KAFKA_MSG_ID_PK_GROUP_MEMBER_CHANGE = 100374;
    /**
     * kafka PK 单人好友邀请通知
     */
    public static final int KAFKA_MSG_ID_PK_SINGLE_INVITE = 100375;
    /**
     * kafka PK 单人好友拒绝通知
     */
    public static final int KAFKA_MSG_ID_PK_SINGLE_REFUSE = 100376;
    /**
     * kafka PK 团队取消匹配通知
     */
    public static final int KAFKA_MSG_ID_PK_GROUP_CANCEL = 100377;
    /**
     * kafka PK 团队开始匹配通知
     */
    public static final int KAFKA_MSG_ID_PK_GROUP_START = 100378;
    /**
     * kafka PK 认输通知
     */
    public static final int KAFKA_MSG_ID_PK_SURRENDER = 100382;
    /**
     * pk排行版
     */
    public static final int KAFKA_MSG_ID_PK_RANK = 100383;

    /**
     * kafka更新主播战力消息
     **/
    public static final int KAFKA_MSG_ID_UPDATE_COMBAT = 100807;

    /**
     * kafka首胜消息
     **/
    public static final int KAFKA_MSG_ID_FIRST_WIN = 100801;

    /**
     * kafka连胜消息
     **/
    public static final int KAFKA_MSG_ID_WIN_STREAK = 100802;

    /**
     * kafka击败连胜消息
     **/
    public static final int KAFKA_MSG_ID_BEAT_WIN_STREAK = 100803;

    /**
     * kafka场次消息
     **/
    public static final int KAFKA_MSG_ID_GAME_CNT = 100804;

    /**
     * kafka分享消息
     **/
    public static final int KAFKA_MSG_ID_SHARE_CNT = 100805;

    /**
     * kafka最高段位消息
     **/
    public static final int KAFKA_MSG_ID_TOP_LEVEL = 100806;

    /**
     * kafka胜方mvp消息
     **/
    public static final int KAFKA_MSG_ID_PK_WIN_MVP = 100385;

    //更新普通活动积分排名（七夕）
    public static final int REFRESH_ACT_INTERGL_RANK = 100322;

    //主播航仓专属消息
    public static final int UPDATE_PLANE_BANNER_DATA = 100320;

    //航仓开启全服消息
    public static final int ALL_ROOM_OPEN_PLANE = 100321;

    //主播到时间放烟花特效
    public static final int IS_TIME_TO_ANIMATE = 100323;

    ///////////////////////////////////////////////////////////////////////////////////
    /**
     * 系统消息-进入群聊
     */
    public static final int CMD_SYSTEM_ENTER_GROUP = 100712;

    /**
     * 群聊 - 开启群禁言
     */
    public static final int CMD_GROUP_FORBIDDEN = 100704;

    /**
     * 群聊 - 关闭群禁言
     */
    public static final int CMD_GROUP_REMOVE_FORBIDDEN = 100705;

    /**
     * 群聊 - 飘气球
     */
    public static final int CMD_GROUP_TOAST_BUBBLE = 100801;

    /**
     * 群聊 - 资源更新
     */
    public static final int CMD_MERCHANT_LIST = 100802;

    /**
     * 群聊 - 群主删除成员
     */
    public static final int CMD_GROUP_MEMBER_DELETE = 100708;

    /**
     * 群聊 - 群成员离开
     */
    public static final int CMD_GROUP_MEMBER_QUIT = 100710;

    /**
     * 群聊 - 群成员加入
     */
    public static final int CMD_GROUP_ENTER = 100713;

    /**
     * 消息发送失败
     */
    public static final int CMD_CHAT_ERROR = 90001;

    // 单聊后台返回收到消息
    public static final int CMD_SEND_SUCCESS = 21000;
}
