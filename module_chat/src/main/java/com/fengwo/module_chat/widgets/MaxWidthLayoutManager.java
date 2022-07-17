package com.fengwo.module_chat.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ScreenUtils;

import static android.view.View.MeasureSpec.AT_MOST;

public class MaxWidthLayoutManager extends LinearLayoutManager {

    private final int maxWidth;

    public MaxWidthLayoutManager(Context context) {
        super(context);
        maxWidth = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 100);
    }

    public MaxWidthLayoutManager(Context context, int maxWidth) {
        super(context);
        this.maxWidth = maxWidth;
    }

    public MaxWidthLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        maxWidth = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 100);
    }

    public MaxWidthLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        maxWidth = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 100);
    }

    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        super.setMeasuredDimension(childrenBounds, View.MeasureSpec.makeMeasureSpec(maxWidth, AT_MOST), hSpec);
    }
}
