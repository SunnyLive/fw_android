package com.fengwo.module_comment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ClickableRecyclerView extends RecyclerView {
    private GestureDetector mGestureDetector;
    private OnClickListener onClickListener;

    public ClickableRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ClickableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (onClickListener != null) {
                    onClickListener.onClick(ClickableRecyclerView.this);
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        this.onClickListener = l;
    }
}
