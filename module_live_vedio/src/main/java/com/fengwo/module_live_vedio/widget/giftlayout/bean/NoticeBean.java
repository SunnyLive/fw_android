package com.fengwo.module_live_vedio.widget.giftlayout.bean;

import android.text.SpannableStringBuilder;

/**
 * @anchor Administrator
 * @date 2020/12/17
 */
public class NoticeBean {
    private SpannableStringBuilder context;
    private  String noticeBack;
    private  String sendUserName;
    private  String receiveUserName;
    private int channelId;
    private  String giftIcon;
    private  String sendNum;
    public NoticeBean(SpannableStringBuilder context, String noticeBack, String sendUserName, String receiveUserName,int channelId,String giftIcon,String sendNum) {
        this.context = context;
        this.noticeBack = noticeBack;
        this.sendUserName = sendUserName;
        this.receiveUserName = receiveUserName;
        this.channelId = channelId;
        this.giftIcon = giftIcon;
        this.sendNum = sendNum;
    }

    public String getSendNum() {
        return sendNum;
    }

    public void setSendNum(String sendNum) {
        this.sendNum = sendNum;
    }

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public SpannableStringBuilder getContext() {
        return context;
    }

    public void setContext(SpannableStringBuilder context) {
        this.context = context;
    }

    public String getNoticeBack() {
        return noticeBack;
    }

    public void setNoticeBack(String noticeBack) {
        this.noticeBack = noticeBack;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }
}
