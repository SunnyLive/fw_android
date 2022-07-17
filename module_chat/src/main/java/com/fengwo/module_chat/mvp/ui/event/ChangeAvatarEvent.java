package com.fengwo.module_chat.mvp.ui.event;

/**
 * @author Zachary
 * @date 2020/1/1
 */
public class ChangeAvatarEvent {
    public String userId;
    public String avatar;

    public ChangeAvatarEvent(String userId,String avatar) {
        this.userId = userId;
        this.avatar = avatar;
    }
}
