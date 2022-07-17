package com.fengwo.module_live_vedio.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private final int padding;
    private boolean needPadding = false;

    public GridItemDecoration(int paddingPX) {
        this.padding = paddingPX;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int columnCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int position = parent.getChildAdapterPosition(view);
            if (position % columnCount == 0) {// 第一列
                outRect.set(needPadding ? padding : 0, 0, padding / 2, 0);
            } else if (position % columnCount == columnCount - 1) {// 最后一列
                outRect.set(padding / 2, 0, needPadding ? padding : 0, 0);
            } else {
                outRect.set(padding / 2, 0, padding / 2, 0);
            }
        } else throw new IllegalArgumentException("layoutManager must be gridLayoutManager!");
    }

    public void setNeedPadding(boolean needPadding) {
        this.needPadding = needPadding;
    }
}
