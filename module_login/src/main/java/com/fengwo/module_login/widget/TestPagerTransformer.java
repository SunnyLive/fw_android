package com.fengwo.module_login.widget;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class TestPagerTransformer implements ViewPager.PageTransformer {

    private final float SCALE_MAX;

    public TestPagerTransformer(float maxScale) {
        SCALE_MAX = maxScale;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        ViewPager parent = (ViewPager) page.getParent();
        int vppl = parent.getPaddingLeft();
        int vppr = parent.getPaddingRight();
        int clientWidth = parent.getMeasuredWidth() - vppl - vppr;
        int scrollX = parent.getScrollX();
        float childLeft = position * clientWidth + scrollX;
        float realPosition = (childLeft - vppl - scrollX) / clientWidth;

        float scalePercent = 1 - (1 - SCALE_MAX) * Math.min(Math.abs(realPosition), 1);
        page.setScaleX(scalePercent);
        page.setScaleY(scalePercent);
    }
}