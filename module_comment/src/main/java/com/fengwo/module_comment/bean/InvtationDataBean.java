package com.fengwo.module_comment.bean;

import android.content.Context;

/**
 * @anchor Administrator
 * @date 2020/10/30
 */
public class InvtationDataBean {
    private String pic;
    private String roomId;
    private String nickname;
    private String text;
    public InvtationDataBean( String pic, String roomId, String nickname, String text) {
        this.pic = pic;
        this.roomId = roomId;
        this.nickname = nickname;
        this.text = text;
    }

    public String getPic() {
        return pic;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }
}
