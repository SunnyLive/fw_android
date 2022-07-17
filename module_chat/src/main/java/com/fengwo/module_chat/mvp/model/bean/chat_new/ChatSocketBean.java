package com.fengwo.module_chat.mvp.model.bean.chat_new;

import android.text.TextUtils;

public class ChatSocketBean {
    public String otherUid;
    public String memeberUid;
    public String memeberNickName; // 用户昵称
    public String userAvatarFileName;
    public int msgType;
    public long listDate;
    public long date;
    public String message;
    public String sendStatus;
    public String fingerPring;
    public boolean vertical; // 非必须
    public int duration;
    public boolean isRead;
    public String fileMd5;
    public String fileLength;
    public String messageUrl;

    public String groupName;
    public String groupId;
    public String groupIMG;



    // 发送文字、图片消息的构造方法
    public ChatSocketBean(String otherUid, String memeberUid, String memeberNickName,
                          String userAvatarFileName, int msgType, long listDate,
                          String message) {
        this.otherUid = otherUid;
        this.memeberUid = memeberUid;
        this.memeberNickName = memeberNickName;
        this.userAvatarFileName = userAvatarFileName;
        this.msgType = msgType;
        this.listDate = listDate;
        this.message = message;
    }

    // 发送语音消息的构造方法
    public ChatSocketBean(String otherUid, String memeberUid, String memeberNickName,
                          String userAvatarFileName, int msgType, long listDate,
                          String message, String messageUrl) {
        this.otherUid = otherUid;
        this.memeberUid = memeberUid;
        this.memeberNickName = memeberNickName;
        this.userAvatarFileName = userAvatarFileName;
        this.msgType = msgType;
        this.listDate = listDate;
        this.message = message;
        this.messageUrl = messageUrl;
    }

    public ChatSocketBean() {
    }

    public static ChatSocketBean builder(){
        return new ChatSocketBean();
    }

    public ChatSocketBean setOtherUid(String otherUid){
        this.otherUid = otherUid;
        return this;
    }
    public ChatSocketBean setMemeberUid(String memeberUid){
        this.memeberUid = memeberUid;
        return this;
    }
    public ChatSocketBean setMemeberNickName(String memeberNickName){
        this.memeberNickName = memeberNickName;
        return this;
    }
    public ChatSocketBean setUserAvatarFileName(String userAvatarFileName){
        this.userAvatarFileName = userAvatarFileName;
        return this;
    }
    public ChatSocketBean setMsgType(int msgType){
        this.msgType = msgType;
        return this;
    }
    public ChatSocketBean setListDate(long listDate){
        this.listDate = listDate;
        return this;
    }

    public ChatSocketBean setDate(long date) {
        this.date = date;
        return this;
    }
    public ChatSocketBean setMessage(String message){
        this.message = message;
        return this;
    }

    public ChatSocketBean setMessageUrl(String messageUrl){
        this.messageUrl = messageUrl;
        return this;
    }
    public ChatSocketBean setGroupId(String groupId){
        if (!TextUtils.isEmpty(groupId)) this.groupId = groupId;
        return this;
    }
    public ChatSocketBean setGroupName(String groupName){
        if (!TextUtils.isEmpty(groupId)) this.groupName = groupName;
        return this;
    }
    public ChatSocketBean setGroupIMG(String groupIMG){
        if (!TextUtils.isEmpty(groupId)) this.groupIMG = groupIMG;
        return this;
    }
    public ChatSocketBean setFingerPring(String fingerPring){
        this.fingerPring = fingerPring;
        return this;
    }
    public ChatSocketBean setDuration(int duration){
        this.duration = duration;
        return this;
    }
}