package com.fengwo.module_live_vedio.eventbus;

import com.fengwo.module_comment.iservice.UserInfo;

public class ShowBuyGuardPopEvent {
    public int type;
    public UserInfo userInfo;
    public boolean isMine ;
    public ShowBuyGuardPopEvent(int type, UserInfo userInfo,boolean isMine){
        this.type = type;
        this.userInfo = userInfo;
        this.isMine = isMine;
    }
}
