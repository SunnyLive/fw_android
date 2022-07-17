package com.fengwo.module_comment.event;

/**
 * 未读消息数
 */
public class UndateUnReadMessageEvent {
    public static int unReadMessageCount;
    public int num;
    public UndateUnReadMessageEvent(int num) {
        this.num = num;
    }
}
