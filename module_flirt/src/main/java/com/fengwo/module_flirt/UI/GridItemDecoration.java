package com.fengwo.module_flirt.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.fengwo.module_flirt.R;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    protected float mDivStartOffset;
    protected float mDivTopOffset;
    protected float mDivBtmOffset;
    protected float mDivEndOffset;
    protected float mDividerHeight;
    protected float mDividerWidth;

    protected Drawable mDividerDrawable;

    public GridItemDecoration(@NonNull Context context,
                              int dividerColor,
                              int dividerWidth,
                              int dividerHeight) {
        this(context, dividerColor, dividerWidth, dividerHeight,
                R.dimen.dp_0, R.dimen.dp_0,
                R.dimen.dp_0, R.dimen.dp_0);
    }

    /**
     * @param context       Context
     * @param dividerColor  分割线颜色
     * @param dividerWidth  分割线宽度
     * @param dividerHeight 分割线高度
     * @param startOffset   item起始位置偏移距离(左边)
     * @param endOffset     item结束位置偏移距离(右边)
     * @param topOffset     item顶部位置偏移距离
     * @param btmOffset     item底部位置偏移距离
     */
    public GridItemDecoration(@NonNull Context context,
                              int dividerColor,
                              int dividerWidth,
                              int dividerHeight,
                              int startOffset,
                              int endOffset,
                              int topOffset,
                              int btmOffset) {
        mContext = context;
        mDivStartOffset = context.getResources().getDimension(startOffset);
        mDivEndOffset = context.getResources().getDimension(endOffset);
        mDivTopOffset = context.getResources().getDimension(topOffset);
        mDivBtmOffset = context.getResources().getDimension(btmOffset);
        mDividerHeight = context.getResources().getDimension(dividerHeight);
        mDividerWidth = context.getResources().getDimension(dividerWidth);
        _initDividerPaint(dividerColor);
        mDividerDrawable = new ColorDrawable(context.getResources().getColor(dividerColor));
    }


    protected Paint mPaint;

    private void _initDividerPaint(int dividerColor) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mContext.getResources().getColor(dividerColor));
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = (int) (top + mDividerHeight);
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(c);
        }
    }

    protected void drawVertical(Canvas c, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int top = child.getTop() - params.topMargin;
            int right = (int) (left + mDividerWidth);
            int bottom = (int) (child.getBottom() + params.bottomMargin + mDividerHeight);
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(c);
        }
    }


    protected int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (position < spanCount) {
            outRect.top = (int) mDivTopOffset;
        } else {
            outRect.top = (int) mDividerHeight;
        }
        if (spanCount == 2) {
            outRect.left = (int) mDivStartOffset;
            outRect.right = (int) mDivEndOffset;
        } else {
            outRect.left = (int) (column * mDividerWidth / spanCount);
            outRect.right = (int) (mDividerWidth - (column + 1) * mDividerWidth / spanCount);
        }
    }
}

