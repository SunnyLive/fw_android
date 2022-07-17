package com.fengwo.module_websocket.bean;

import android.text.TextUtils;

public class LivingRoomTextMsg extends BaseMsg {
    public static final int TYPE_TOUTIAO = 5;
    public static final int TYPE_SOMECOMEING = 4;  //新用户进来
    public static final int TYPE_GIFT_BOARDCAST = 3;
    public static final int TYPE_BUY_GUARD = 6;
    public static final int TYPE_SYSTEM = 2;
    public static final int TYPE_SEND = 1;
    public static final int TYPE_PK_MVP = 7;
    public static final int TYPE_TEASE_HIM= 8;   //撩他一下
    public static final int TYPE_ANCHOR_ATTENTION = 9;   //其他用户关注主播
    public static final int TYPE_ANCHOR_SHARE = 10;   //其他用户分享主播
    public static final int TYPE_LIVERS = 11;//情侣标识
    public int type = 1;
    public String forward = "0";
    public String messageType = "0";

    public String message;
    public String headerurl;
    public String nickname;
    public String toNickname;
    public String uid;
    public String userId;
    public String level;
    public String userVipLevel;

    public int isDanmu;
    public String userNickColor;
    public String toUserNickColor;
    public String userMsgColor;
    public String username_bg_color;
    public String systemColor = "#ffffff";
    public String sysColor = "#ffffff";
    public String bullet_screen;
    public String userShouHuLevelIMG;
    public int isEachSend = 0;
    public boolean isRoomManage;
    public String anchorLevel;
    public boolean isTeaseHim = false;   //是否撩了用户
public int isOfficialUser;//0不是官方号，1官方号
    public String guardCarName;
    public String carName;
    public int isTourist;
    public String cpRank;


    //TYPE_GIFT_BOARDCAST
    public int allPrice;//礼物总价值
    public String giftIcon;
    public String giftName;
    public int sendNum;

    public LivingRoomTextMsg() {
    }

    public boolean isDanmu() {
        return isDanmu == 1;
    }

    public LivingRoomTextMsg(String headerurl, String level, String message, String nickName, String uid, int type) {
        this.headerurl = headerurl;
        this.level = level;
        this.message = message;
        this.nickname = nickName;
        this.type = type;
        this.uid = uid;
    }

    public LivingRoomTextMsg(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public int getUserVipLevel() {
        if (!TextUtils.isEmpty(userVipLevel) && Integer.parseInt(userVipLevel) > 0) {
            return Integer.parseInt(userVipLevel);
        } else
            return 0;
    }
}
