package com.fengwo.module_chat.widgets.xbanner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.core.view.ViewCompat;

/**
 * <p>
 * link https://xiaohaibin.github.io/
 * email： xhb_199409@163.com
 * github: https://github.com/xiaohaibin
 * description： XBanner轮播控件的工具类
 */
public class XBannerUtils {


    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }


    public static void resetPageTransformer(List<? extends View> views) {
        if (views == null) {
            return;
        }
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
            ViewCompat.setAlpha(view, 1);
            ViewCompat.setPivotX(view, view.getMeasuredWidth() * 0.5f);
            ViewCompat.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            ViewCompat.setTranslationX(view, 0);
            ViewCompat.setTranslationY(view, 0);
            ViewCompat.setScaleX(view, 1);
            ViewCompat.setScaleY(view, 1);
            ViewCompat.setRotationX(view, 0);
            ViewCompat.setRotationY(view, 0);
            ViewCompat.setRotation(view, 0);
        }
    }


}
