/*
 *
 *  这是一个可以垂直换行的 viewGroup
 *
 * */
package com.fengwo.module_comment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fengwo.module_comment.R;

import androidx.annotation.Nullable;

public class FlowLinearLayout extends LinearLayout {

    private int mColumnCount = 0;
    private int mChildWidth = 0;

    public FlowLinearLayout(Context context) {
        this(context,null);
    }

    public FlowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FlowLinearLayout, 0, 0);
        mColumnCount = attributes.getInt(R.styleable.FlowLinearLayout_columnCount, mColumnCount);
        attributes.recycle();
    }



    public void setChildWidth(int width) {
        this.mChildWidth = width;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int lineWidth = 0;
        int lineHeight = 0;
        int heightSize = 0;

        int count = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        View childView;
        for (int i = 0; i < count; i++) {
            childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            MarginLayoutParams params = ((MarginLayoutParams) childView.getLayoutParams());
            if (mChildWidth > 0) {
                params.width = mChildWidth;
            }
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (0 < mColumnCount) {
                if (childWidth * mColumnCount > size - getPaddingRight() - getPaddingLeft()) {//要换行了
                    lineWidth = childWidth;
                    heightSize += lineHeight;
                    lineHeight = childHeight;
                } else {
                    lineWidth += childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
            } else if (lineWidth + childWidth > size - getPaddingRight() - getPaddingLeft()) {//要换行了
                lineWidth = childWidth;
                heightSize += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == count - 1) {
                heightSize += lineHeight;
            }
        }

        setMeasuredDimension(size,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec) :
                        heightSize + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        int heightSize = 0;
        View childView;

        for (int i = 0; i < count; i++) {
            childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            MarginLayoutParams params = ((MarginLayoutParams) childView.getLayoutParams());
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (lineWidth + childWidth + params.leftMargin + params.rightMargin >
                    width - getPaddingLeft() - getPaddingRight()) {//要换行了
                heightSize += lineHeight;
                lineWidth = 0;
            } else {
                lineHeight = Math.max(lineHeight, childHeight + params.topMargin + params.bottomMargin);
            }

            int left = lineWidth + params.leftMargin;
            int top = heightSize + params.topMargin;
            int right = left + childWidth;
            int bottom = top + childHeight;

            childView.layout(left, top, right, bottom);
            lineWidth += childWidth + params.leftMargin + params.rightMargin;
        }
    }
}
