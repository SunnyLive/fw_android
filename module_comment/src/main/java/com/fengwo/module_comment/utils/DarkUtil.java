package com.fengwo.module_comment.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.google.android.exoplayer2.C;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/5/27
 */
public class DarkUtil {
    public static boolean isDarkTheme(Context context){
        int flag = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return flag == Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * 设置应用的主题颜色
     * @param mode MODE_NIGHT_YES 深色模式，MODE_NIGHT_NO，浅色模式
     */
    public static void setDarkTheme(int mode){
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
