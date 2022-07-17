package com.fengwo.module_live_vedio.eventbus;

public class AttentionChangeEvent {


    public boolean isAttention = false;
    public boolean isPk = false;
    public int sendIMMsg = 0;   //1的时候发送关注主播IM消息
    public String id = "";

    public AttentionChangeEvent(boolean isAttention) {
        this.isAttention = isAttention;
    }

    public AttentionChangeEvent(boolean isAttention, boolean isPk) {
        this.isAttention = isAttention;
        this.isPk = isPk;
    }
// 谁tm写的 也不给个备注！  id是判断是不是当前主播 发不发im消息
    public AttentionChangeEvent(boolean isAttention,  boolean isPk,int sendIMMsg,String id) {
        this.isAttention = isAttention;
        this.isPk = isPk;
        this.sendIMMsg = sendIMMsg;
        this.id= id;
    }
}
