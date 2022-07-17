package com.fengwo.module_comment.api;

public class API {

    // 退出直播间
    public static final String BUNKO_2030016 = "api/bunko/2030016";
    // 检测主播直播状态
    public static final String BUNKO_2029019 = "api/bunko/2029019";

    public static final String LEAVE_LIVINGROOM = "api/live/user_play/exit_room/{channelId}";
    //检测版本更新
    public static final String APP_VERSION = "api/user/login/app_version";
    //查询红包配置
    public static final String GET_RED_PACKET_CONFIG = "api/game/gameRedpacket/getConfig";
    //判断当前用户是否为官方号
    public static final String GET_CHECK_OFFICIAL_ACCOUNT = "api/game/gameRedpacket/check/officialAccount";

    public static final String GET_LIVE_ZHUBO_LIST = "api/live/user_play/get_lives_new";
}
