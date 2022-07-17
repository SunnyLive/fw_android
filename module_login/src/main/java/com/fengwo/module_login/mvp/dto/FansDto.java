package com.fengwo.module_login.mvp.dto;

public class FansDto {
    /**
     * "age": 0,
     * "aristocracy": 0,
     * "channelLevel": 0,
     * "distance": 0,
     * "headImg": "string",
     * "id": 0,
     * "isAttention": 0,
     * "lastTime": "string",
     * "level": 0,
     * "liveStatus": 0,
     * "liveTime": 0,
     * "nickname": "string",
     * "sex": 0,
     * "signature": "string",
     * "wenboLiveStatus": 0
     */
    public String age;
    public int aristocracy;
    public int channelLevel;
    public String distance;
    public String headImg;
    public int id;
    public int isAttention = 1;
    public String lastTime;
    public int level;
    public int liveStatus;//2 直播
    public long liveTime;
    public String nickname;
    public int sex;
    public String signature;
    public int wenboLiveStatus;//0 未直播 1开播
    public int userState = 1;
}
