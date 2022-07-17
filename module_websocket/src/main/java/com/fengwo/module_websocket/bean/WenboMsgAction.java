package com.fengwo.module_websocket.bean;

public class WenboMsgAction {
    public static final String SEND_GIFT = "sendGift";
    public static final String LIVE_END = "liveEnd";
    public static final String LIVE_WARN = "warn";
    public static final String ACCEPT_ORDER = "acceptOrder";
    public static final String USER_ADDTIME = "userAddTime";
    public static final String LIVE_BANNED = "banned";
    public static final String USER_CANCEL_ORDER = "userCancelOrder";
    public static final String GAME = "game";
    public static final String CHAT = "chat";
    public static final String FLIRT_BULLETIN = "enterRoomNotice";//公告
    public static final String SPLASH = "enterRoomTip";//im区

    public static final String ENTER_LIVING_ROOM = "enterLivingRoom";//有用户进入直播观看
    public static final String EXIT_LIVING_ROOM = "exitLivingRoom";//有用户退出直播观看

    public static final String STAT_FATE = "statFate";//接收到缘分礼物

    public static final String WITHDRAW = "withdraw";//消息撤回
    public static final String RECEIVE_COMMENT = "evaluationAnchorMsg"; // 收到评价消息
}
