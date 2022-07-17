package com.fengwo.module_comment.utils;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/10/9.
 * 描    述：事件间隔控制工具类（防止短时间内多次响应事件）
 * 修订历史：
 * ================================================
 */
public class EventIntervalUtil {
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public static boolean canOperate() {
        boolean toDO = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            toDO = true;
            lastClickTime = curClickTime;
        }
        return toDO;
    }
}