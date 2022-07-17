package com.fengwo.module_comment.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author huanghongfu
 * @date 2020/8/22
 * @desc
 */
class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    /**
//     * 去掉上面阴影
//     * @return
//     */
//    @Override
//    protected float getTopFadingEdgeStrength() {
//        return 0;
//    }

    /**
     * 去掉下面阴影
     * @return
     */
    @Override
    protected float getBottomFadingEdgeStrength() {
        return 0;
    }
}
