package com.fengwo.module_comment.utils;

/**
 * Created by cxf on 2018/9/29.
 */

public class ClickUtil {

    private static long sLastClickTime;

    public static boolean canClick() {
        return canClick(500);
    }

    public static boolean canClick(long interval) {
        long curTime = System.currentTimeMillis();
        if (curTime - sLastClickTime < interval) {
            return false;
        }
        sLastClickTime = curTime;
        return true;
    }

}
