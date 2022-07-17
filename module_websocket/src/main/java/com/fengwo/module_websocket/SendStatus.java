package com.fengwo.module_websocket;

public interface SendStatus {
        /**
         * 消息发送中
         */
        int sending = 1;
        /**
         * 消息已被对方收到（我方已收到应答包）
         */
        int beReceived = 0;
        /**
         * 消息发送失败（在超时重传的时间内未收到应答包）// 修改为发送失败 违规不可再次发送
         */
        int sendFaild = 2;
        /**
         * 没收到ack 可以点击重发
         */
        int comeBack = 3;
    }
