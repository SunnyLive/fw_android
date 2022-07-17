package com.fengwo.module_websocket.bean;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/21
 */
public class InvitePkMsg implements Serializable {
    public String headImg;
    public String nickname;
    public int userId;
    public String teamId;
    public boolean isAccept = false;
    public boolean isSingle = true;

    public InvitePkMsg() {
    }

    public InvitePkMsg(int userId,String headerurl, String nickname) {
        this.userId = userId;
        this.headImg = headerurl;
        this.nickname = nickname;
    }
    public InvitePkMsg(int userId,String headerurl, String nickname,String teamId) {
        this.userId = userId;
        this.headImg = headerurl;
        this.nickname = nickname;
        this.teamId = teamId;
    }
}
