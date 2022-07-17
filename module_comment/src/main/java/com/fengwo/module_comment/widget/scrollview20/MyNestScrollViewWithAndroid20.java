package com.fengwo.module_comment.widget.scrollview20;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MyNestScrollViewWithAndroid20 extends NestedScrollView {
    public MyNestScrollViewWithAndroid20(@NonNull Context context) {
        super(context);
    }

    public MyNestScrollViewWithAndroid20(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestScrollViewWithAndroid20(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.listener != null) {
            listener.mOnScrollChanged(l, t, oldl, oldt);
        }

    }

    public interface MOnScrollListener {
        void mOnScrollChanged(int l, int t, int oldl, int oldt);
    }

    private MOnScrollListener listener;

    public void setMOnScrollListener(MOnScrollListener l) {
        listener = l;
    }
}
