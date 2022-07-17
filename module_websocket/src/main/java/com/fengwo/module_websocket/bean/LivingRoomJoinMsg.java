package com.fengwo.module_websocket.bean;

import java.util.ArrayList;
import java.util.List;

public class LivingRoomJoinMsg extends BaseMsg {
    public String uid;
    public String headerurl;
    public String nickname;
    public String level;
    public String consumNums = "0";
    public String groupId;
    public String carSwf = "";
    public String carName;
    public String userVipLevel;
    public int carFrameRate;
    public String userShouHuLevelIMG = "";
    public boolean isRoomManage = false;
    public String guardSwf = "";
    public int guarFrameRate;
    public String guarName;
    public int isTourist;
    public int isActivityGuardType = 0;
    public int isCombatCheer = 0;//是否是助力达人 0：不是 1：是
    public int isShareTalent = 0;//是否是分享达人 0：不是 1：是
    public String  specialEffectUrl;
    public String cpRank;
    public int isOfficialUser;
    public List<Integer> medalId = new ArrayList<>();
}
