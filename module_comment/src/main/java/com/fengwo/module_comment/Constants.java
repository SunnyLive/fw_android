package com.fengwo.module_comment;

public class Constants {

    //    public static final String BASE_URL = "https://api.fwhuyu.com/";//上线正式地址
    public static final int GROUP_BASE = 900000000;
    public static final int CIRCLE_GROUP_BASE = 800000000;
   // public static final int TX_APP_ID = 1258964686;
    public static final int TX_APP_ID = 1304411524;

    public static final String BUGLY_APP_ID = "a3934323c0";
    public final static String BANNER_JUMP_TYPE_LIVE = "live";
    public final static String BANNER_JUMP_TYPE_ALBUM = "movie";
    public static int unReadAppoint = 0;
    public final static String UNION_INTRO = "https://app.fwhuyu.com/economic";//工会介绍
    //    public static final String REGULAR_EMPTY_LINE ="(?m)^\\s*$" + System.lineSeparator();
    public static final String REGULAR_EMPTY_LINE = "(?m)^\\s*$(\\n|\\r\\n)";
    public static final String WX_MINI_PRO_APP_ID = "wx545d1f660d3ba55b";//微信小程序appid
    public static final String WX_MINI_PRO_ORIGIN_ID = "gh_7085ec655c51";//微信小程序原始id
    public static final String WX_MINI_PRO_PATH = "sqtg_sun/pages/home/pay/bosspay?token=";//拉起小程序的路径
    public static final String WX_APP_ID = "wx0caf01020ad1af24";//拉起小程序的路径


    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }

    public static final int NOTICE_TYPE_APPOINT = 0;//约会助手
    public static final int NOTICE_TYPE_INTERACT = 1;//互动通知
    public static final int NOTICE_TYPE_RECENT_VISITORS = 2;//最近访客
    public static final int NOTICE_TYPE_SYSTEM = 3;//系統消息
    public static final int NOTICE_TYPE_GREET = 4;//打招呼
    public static final int NOTICE_TYPE_OFFICIAL = 5;//系统消息
    public static String CURRENT_CHAT_TARGET_ID = "";//当前正在聊天的目前id 用于接收socket标记为已读

    public static final String TEENAGER_MODE_ENABLE = "1";//启用青少年模式
    public static final String TEENAGER_MODE_UNENABLE = "0";//未启用青少年模式
    public static final String MAIN_CLASS_NAME = "com.fengwo.module_main.mvp.activity.MainActivity";//首页包名

    public static final int REQUEST_CODE_UPDATE_APP_VERSION = 8;//app更新申请权限requestCode
//    app.fengwohuyu.com
//    各个H5页面路由：
//    广告详情 adInfo
//    活动详情 activityInfo
//    主题详情 themeInfo
}
