package com.fengwo.module_comment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.utils.L;

public class MyRecycleView extends RecyclerView {
    private final String TAG = "=====RecyclerView";

    public MyRecycleView(@NonNull Context context) {
        super(context);
    }

    public MyRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        L.e(TAG, "onTouchEvent");
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        L.e(TAG, "onInterceptTouchEvent");
        return super.onInterceptTouchEvent(e);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //父层ViewGroup不要拦截点击事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
