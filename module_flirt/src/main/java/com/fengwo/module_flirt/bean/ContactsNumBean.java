package com.fengwo.module_flirt.bean;

public class ContactsNumBean {

    private int friendsNum;
    private int attentionNum;
    private int fansNum;

    public ContactsNumBean(int friendsNum, int attentionNum, int fansNum) {
        this.friendsNum = friendsNum;
        this.attentionNum = attentionNum;
        this.fansNum = fansNum;
    }

    public ContactsNumBean() {

    }

    public int getFriendsNum() {
        return friendsNum;
    }

    public void setFriendsNum(int friendsNum) {
        this.friendsNum = friendsNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }
}
