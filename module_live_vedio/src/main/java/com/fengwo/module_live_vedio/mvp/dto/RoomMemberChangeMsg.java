package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_websocket.bean.InvitePkMsg;

import java.io.Serializable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/14
 */
public class RoomMemberChangeMsg extends InvitePkMsg implements Serializable {
    public int leader;
}
