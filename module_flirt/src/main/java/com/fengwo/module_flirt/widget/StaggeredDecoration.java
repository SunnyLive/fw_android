package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class StaggeredDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int mInterval;
    public StaggeredDecoration(Context context, int interval) {
        this.mContext = context;
        this.mInterval = interval;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int interval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                this.mInterval, mContext.getResources().getDisplayMetrics());
        outRect.bottom = interval;
    }

}
