package com.fengwo.module_comment.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.utils.L;

public class LooperLayoutManager extends LinearLayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
    private static final String TAG = "LooperLayoutManager";
    private boolean looperEnable = true;
    private int nowPosition;

    public LooperLayoutManager(Context context, int nowPosition) {
        super(context);
        this.nowPosition = nowPosition;
    }


    public void setLooperEnable(boolean looperEnable) {
        this.looperEnable = looperEnable;
    }


    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getChildCount() == 0) {
            return null;
        }
        int firstChildPosition = getPosition(getChildAt(0));
        int direction = targetPosition < firstChildPosition ? -1 : 1;
        return new PointF(direction * 1f, 0f);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            return;
        }
        //preLayout主要支持动画，直接跳过
        if (state.isPreLayout()) {
            return;
        }
        //将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler);

        int autualHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            //初始化，将在屏幕内的view填充
            View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //测量itemView的宽高
            measureChildWithMargins(itemView, 0, 0);
            int width = getDecoratedMeasuredWidth(itemView);
            int height = getDecoratedMeasuredHeight(itemView);
            //根据itemView的宽高进行布局
            layoutDecorated(itemView, 0, autualHeight, width, autualHeight + height);
            autualHeight += height;
            //如果当前布局过的itemView的宽度总和大于RecyclerView的宽，则不再进行布局
            if (autualHeight > getHeight()) {
                break;
            }
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        fill1(dy, recycler, state);
        recyclerHideView(dy, recycler, state);
        offsetChildrenVertical(dy * -1);
        return dy;
    }


    private void fill1(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy > 0) {//上滑
            while (true) {
                View lastView = getChildAt(getChildCount()-1);
                if (null == lastView) {
                    return;
                }
                if (lastView.getBottom() - dy > getHeight()) {
                    return;
                }
                int layoutPosition = getPosition(lastView);
                L.e("xxxxxxxxxxxx", layoutPosition + "");
                View nextView;
                if (layoutPosition == getChildCount() - 1) {
                    nextView = recycler.getViewForPosition(0);
                    nowPosition = 0;
                } else {
                    nextView = recycler.getViewForPosition(layoutPosition + 1);
                    nowPosition++;
                }
                addView(nextView);
                measureChildWithMargins(nextView, 0, 0);
                int viewWidth = getDecoratedMeasuredWidth(nextView);
                int viewHeight = getDecoratedMeasuredHeight(nextView);
                int offsetY = lastView.getBottom();
                layoutDecorated(nextView, 0, offsetY, viewWidth, offsetY + viewHeight);
            }
        } else {//下滑
            while (true) {
                View firstView =getChildAt(0);
                if (null == firstView) {
                    return;
                }
                if (firstView.getTop() - dy < 0) {
                    return;
                }
                int layoutPosition = getPosition(firstView);
                View nextView;
                if (nowPosition == 0) {
                    nextView = recycler.getViewForPosition(getItemCount() - 1);
                    nowPosition = getItemCount() - 1;
                } else {
                    nextView = recycler.getViewForPosition(layoutPosition - 1);
                    nowPosition--;
                }
                addView(nextView, 0);
                measureChildWithMargins(nextView, 0, 0);
                int viewWidth = getDecoratedMeasuredWidth(nextView);
                int viewHeight = getDecoratedMeasuredHeight(nextView);
                int offsetY = firstView.getTop();
                layoutDecorated(nextView, 0, offsetY - viewHeight, viewWidth, offsetY);
            }
        }
    }

    /**
     * 回收界面不可见的view
     */
    private void recyclerHideView(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == null) {
                continue;
            }
            if (dy > 0) {//上拉
                if (view.getBottom() < 0) {
                    removeAndRecycleView(view, recycler);
                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
                }
            } else {
                //向右滚动，移除一个右边不在内容里的view
                if (view.getTop() > getHeight()) {
                    removeAndRecycleView(view, recycler);
                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
                }
            }
        }

    }
}