package com.fengwo.module_live_vedio.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fengwo.module_comment.utils.DensityUtils;

import static android.view.View.MeasureSpec.AT_MOST;

public class MaxHeightLayoutmanager extends LinearLayoutManager {

    private final int maxHeight;

    public MaxHeightLayoutmanager(Context context) {
        super(context);
        maxHeight = DensityUtils.dp2px(context, 200);
    }
    public MaxHeightLayoutmanager(Context context,int maxHeight) {
        super(context);
        this.maxHeight = maxHeight;
    }

    public MaxHeightLayoutmanager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        maxHeight = DensityUtils.dp2px(context, 200);
    }

    public MaxHeightLayoutmanager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        maxHeight = DensityUtils.dp2px(context, 200);
    }

    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        super.setMeasuredDimension(childrenBounds, wSpec,
                View.MeasureSpec.makeMeasureSpec(maxHeight, AT_MOST));
    }
}
