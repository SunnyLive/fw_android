package com.fengwo.module_chat.mvp.model.bean.chat_new;

import android.text.TextUtils;

import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;

import java.util.List;

public class BaseChatBean {

    public static final String TYPE_GROUP = "cmd_GroupChat";
    public static final String TYPE_SINGLE = "cmd_OneChat";

    public String actionId;
    public String busiType;
    public int eventId;
    public String userId;
    public ChatSocketBean messageModel;
    public String message;
    public String headerurl;

    // 气球用到的字段
    public String id;
    public String label;

    // 资源刷新用到的字段
    public String imgUrl;
    public String linkUrl;
    public String name;

    // 创建私群字段
    public String groupName;
    public String createId; // 群主Id
    public String groupHeadImg;
    public List<GroupMemberModel> users;

    // 删除群成员IM字段
    // 群成员离开IM字段
    public String deleteNickname;
    public String fromUid;
    public String deleteUid;
    public String groupImg;
    public String msg;

    // 系统通知消息字段
    public int uid;
    public int groupId;
    public String nickname;
    public String title;
    public String subTitle;

    public BaseChatBean(String actionId, String busiType, String userId, int eventId, ChatSocketBean messageModel) {
        this.actionId = actionId;
        this.busiType = busiType;
        this.eventId = eventId;
        this.userId = userId;
        this.messageModel = messageModel;
    }

    public BaseChatBean(){}
    public static BaseChatBean builder(){
        return new BaseChatBean();
    }

    public BaseChatBean setActionId(String actionId){
        this.actionId = actionId;
        return this;
    }
    public BaseChatBean setBusiType(String busiType){
        this.busiType = busiType;
        return this;
    }

    public BaseChatBean setUserId(String userId){
        this.userId = userId;
        return this;
    }
    public BaseChatBean setEventId(int eventId){
        this.eventId = eventId;
        return this;
    }
    public BaseChatBean setMessage(String message){
        if (!TextUtils.isEmpty(message)) this.message = message;
        return this;
    }
    public BaseChatBean setUid(int uid){
        this.uid = uid;
        return this;
    }
    public BaseChatBean setMessageModel(ChatSocketBean messageModel){
        this.messageModel = messageModel;
        return this;
    }


}
