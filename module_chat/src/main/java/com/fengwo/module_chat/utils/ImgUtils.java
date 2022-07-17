package com.fengwo.module_chat.utils;

import android.content.Context;

public class ImgUtils {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
