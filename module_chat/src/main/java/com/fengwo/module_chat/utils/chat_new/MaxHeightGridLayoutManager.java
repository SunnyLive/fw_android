package com.fengwo.module_chat.utils.chat_new;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fengwo.module_comment.utils.DensityUtils;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public class MaxHeightGridLayoutManager extends GridLayoutManager {

    private final int DEFAULT_MAX_HEIGHT;

    public MaxHeightGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DEFAULT_MAX_HEIGHT = DensityUtils.dp2px(context, 500);
    }

    public MaxHeightGridLayoutManager(Context context, int spanCount, int maxHeight) {
        super(context, spanCount);
        DEFAULT_MAX_HEIGHT = maxHeight;
    }

    public MaxHeightGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        DEFAULT_MAX_HEIGHT = DensityUtils.dp2px(context, 500);
    }


    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        int newHSpec = View.MeasureSpec.makeMeasureSpec(DEFAULT_MAX_HEIGHT, AT_MOST);
        super.setMeasuredDimension(childrenBounds, wSpec, newHSpec);
    }
}
