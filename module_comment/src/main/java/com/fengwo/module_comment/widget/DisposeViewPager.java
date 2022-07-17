package com.fengwo.module_comment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class DisposeViewPager extends ViewPager {
    public DisposeViewPager(@NonNull Context context) {
        super(context);
    }

    public DisposeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
