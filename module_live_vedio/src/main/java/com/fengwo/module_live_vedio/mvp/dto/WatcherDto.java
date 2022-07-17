package com.fengwo.module_live_vedio.mvp.dto;

import java.util.ArrayList;
import java.util.List;

public class WatcherDto implements Cloneable{
    public int id;
    public int userId;
    public String nickname;
    public String headImg;
    public int userLevel;
    public double consumNums;
    public String carSwf;
    public String carName;
    public int carFrameRate;
    public int userVipLevel;
    public int isActivityGuardType;
    public int isCombatCheer = 0;//是否是助力达人 0：不是 1：是
    public int isShareTalent = 0;//是否是分享达人 0：不是 1：是
    public String specialEffectUrl;
    public List<Integer> medalId = new ArrayList<>();//勋章数组
    public String cpRank;

    public String guardSwf;
    public int guarFrameRate;
    public String guarName;

    public boolean isRoomManage;
    public String userShouHuLevelIMG;

    public int sex;

    public int getId() {
        if (userId > 0) {
            return userId;
        }
        return id;
    }


    //主播贡献榜
    public int hostLevel;
    public int nobility;
    public double receive;

    @Override
    public Object clone() {
        WatcherDto dto = null;
        try{
            dto = (WatcherDto)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
