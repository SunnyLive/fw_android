package com.fengwo.module_chat.base;


import com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatSocketBean;

import static com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean.TYPE_GROUP;
import static com.fengwo.module_chat.mvp.model.bean.chat_new.BaseChatBean.TYPE_SINGLE;

public class ChatSocketRequest {
    public String fromUid;
    public String toUid;
    public BaseChatBean data;
    public int chatType = 0;
    public int eventId ;
    public boolean isGroupMsg = false;
    public int usertype = 1; //用户类型 1普通用户，0其他用户

    /**
     * 系统消息才有，不要根据这个判断msg
     */
    public String msgId;
    public String groupId;
    public static ChatSocketRequest builder(){
        return new ChatSocketRequest();
    }

    public ChatSocketRequest setFromUid(String fromUid){
        this.fromUid = fromUid;
        return this;
    }
    public ChatSocketRequest setToUid(String toUid){
        this.toUid = toUid;
        return this;
    }
    public ChatSocketRequest setData(ChatSocketBean messageModel,String message){
        BaseChatBean baseChatBean = BaseChatBean.builder()
                .setActionId(isGroupMsg ? TYPE_GROUP : TYPE_SINGLE)
                .setBusiType("1")
                .setUserId(fromUid)
                .setMessage(message)
                .setUid(Integer.parseInt(fromUid))
                .setEventId(eventId).setMessageModel(messageModel);
        this.data = baseChatBean;
        return this;
    }
    public ChatSocketRequest setChatType(int chatType){
        this.chatType = chatType;
        return this;
    }
    public ChatSocketRequest setEventId(int eventId){
        this.eventId = eventId;
        return this;
    }
    public ChatSocketRequest setIsGroupMsg(boolean isGroupMsg){
        this.isGroupMsg = isGroupMsg;
        return this;
    }
    public ChatSocketRequest setMsgId(String msgId){
        this.msgId = msgId;
        return this;
    }
    public ChatSocketRequest setGroupId(String groupId){
        if (isGroupMsg) this.groupId = groupId;
        return this;
    }

    public ChatSocketRequest setUsertype(int usertype) {
        this.usertype = usertype;
        return this;
    }
}
