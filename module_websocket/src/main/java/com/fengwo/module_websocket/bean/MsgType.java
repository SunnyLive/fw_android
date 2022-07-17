package com.fengwo.module_websocket.bean;

import java.io.Serializable;

public class MsgType implements Serializable {
    /**
     * 　发出的消息：普通文本
     */
    public final static int toText = 0;
    /**
     * 　收到的消息：普通文本
     */
    public final static int comeText = 1;

    /**
     * 　发出的消息：图片
     */
    public final static int toImage = 2;
    /**
     * 　收到的消息：图片
     */
    public final static int comeImage = 3;

    /**
     * 　发出的消息：语音留言
     */
    public final static int toVoice = 4;
    /**
     * 　收到的消息：语音留言
     */
    public final static int comeVoice = 5;

    /**
     * 　发出的消息：文件
     */
    public final static int toFile = 6;
    /**
     * 　收到的消息：文件
     */
    public final static int comeFile = 7;

    /**
     * 　收到的系统级消息(比如一些群通知、系统通知等)
     */
    public final static int systemText = 14;
    /**
     * 　收到的系统级消息(比如一些欢迎)
     */
    public final static int systemWelcome = 15;
    /**
     * 　收到的系统级消息礼物
     */
    public final static int systemGift = 15;
    /**
     * 　发出的消息：视频
     */
    public final static int toVideo = 8;
    /**
     * 　收到的消息：视频
     */
    public final static int comeVideo = 9;

    /**
     * 撤回的消息
     */
    public final static int revocation = 12;

    /**
     * 收到合并转发消息
     */
    public final static int toForwardMerge = 10;
    public final static int comeForwardMerge = 11;
    public final static int toGameMsg = 12;//游戏消息
    public final static int fromGameMsg = 13;//游戏消息  自己发
    public final static int splashMeg = 15;//欢迎消息

    public final static int giftMeg = 16;//加时礼物消息
   // public final static int ordinaryMeg = 17;//普通礼物消息
    /**
     * 　被邀请入群
     */
    public final static int inviteIntoGroup = 46;

    /**
     * 撤回的消息
     */
    public final static int comeRevocation = 47;
    public final static int toRevocation = 48;


    public final static int toGiftMsg = 49;//收到礼物消息
    public final static int fromGiftMsg = 50;//发送礼物消息
}