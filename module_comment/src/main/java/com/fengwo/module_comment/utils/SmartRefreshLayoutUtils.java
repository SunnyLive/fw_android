package com.fengwo.module_comment.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.fengwo.module_comment.R;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;


public class SmartRefreshLayoutUtils {
    public static void setClassicsColor(Context c, SmartRefreshLayout smartRefreshLayout, int bgColor, int textColor) {
        DefaultRefreshHeaderCreator creator = new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(bgColor, textColor);//全局设置主题颜色
                return new ClassicsHeader(context);
            }
        };
        smartRefreshLayout.setRefreshHeader(creator.createRefreshHeader(c, smartRefreshLayout));
    }

    public static void setWhiteBlackText(Context context, SmartRefreshLayout smartRefreshLayout) {
        setClassicsColor(context, smartRefreshLayout, R.color.white, R.color.text_33);
    }

    public static void setTransparentBlackText(Context c, SmartRefreshLayout smartRefreshLayout) {
        SmartRefreshLayoutUtils.setClassicsColor(c, smartRefreshLayout, android.R.color.transparent, R.color.text_66);
    }

    public static void setTransparentBgWithWhileText(Context c, SmartRefreshLayout smartRefreshLayout) {
        SmartRefreshLayoutUtils.setClassicsColor(c, smartRefreshLayout, android.R.color.transparent, R.color.text_white);
    }

    public static void setWhiteBlackTextNoNight(Context context, SmartRefreshLayout smartRefreshLayout) {
        setClassicsColor(context, smartRefreshLayout, R.color.mine_white, R.color.black_333333);
    }
}
