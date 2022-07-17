package com.fengwo.module_comment.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.utils.ScreenUtils;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private final int padding;
    private final Context context;

    public GridItemDecoration(Context context, int paddingPX) {
        this.context = context;
        this.padding = paddingPX;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int columnCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int screenWidth = ScreenUtils.getScreenWidth(context);
            int itemWidth = screenWidth / columnCount;
            int itemRealWidth = (screenWidth - padding * (columnCount + 1)) / columnCount;
            int position = parent.getChildAdapterPosition(view);
            if (position % columnCount == 0) {// 第一列
                outRect.set(padding, 0, itemWidth - itemRealWidth - padding, 0);
            } else if (position % columnCount == columnCount - 1) {// 最后一列
                outRect.set(itemWidth - itemRealWidth - padding, 0, padding, 0);
            } else {
                outRect.set((itemWidth - itemRealWidth) / 2, 0, (itemWidth - itemRealWidth) / 2, 0);
            }
        } else throw new IllegalArgumentException("layoutManager must be gridLayoutManager!");
    }
}
