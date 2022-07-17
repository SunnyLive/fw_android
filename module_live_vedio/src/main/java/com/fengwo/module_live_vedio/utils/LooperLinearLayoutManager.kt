package com.fengwo.module_live_vedio.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fengwo.module_comment.ext.log

/**
 * File LooperLinearLayoutManager.kt
 * Date 2020/12/11
 * Author lucas
 * Introduction 无限循环列表布局管理器
 */
class LooperLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    )

    var isCanLayout = true//是否允许重新布局

    var isLooper = true

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
            RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)

    override fun canScrollVertically(): Boolean = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (isCanLayout)
            super.onLayoutChildren(recycler, state)
//        "onLayoutChildren,childCount:$childCount".log()
    }

    //处理滚动
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val diffY = fill(dy, recycler, state)
        offsetChildrenVertical(-diffY)
        recyclerHideView(dy, recycler, state)
        //回收item
        return diffY
    }

    private fun recyclerHideView(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        for (i in 0 until itemCount) {
            getChildAt(i)?.also {
                if (it.top >= height || it.bottom <= 0) {
                    removeAndRecycleView(it, recycler)
//                    "recyclerHideView:${getPosition(it)}".log()
                }
            }
        }
    }

    private fun fill(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (dy > 0) {//上
            val lastView = getChildAt(childCount - 1) ?: return 0
            val lastPosition = getPosition(lastView)
//            "上 dy:$dy childCount:$childCount,lastPosition:$lastPosition".log()
            if (lastView.bottom <= lastView.height) {
                var nextView: View
                if (lastPosition == itemCount - 1) {//一轮循环的最后一个
                    if (isLooper) {
                        nextView = recycler.getViewForPosition(0)//循环闭环
                    } else {
                        return 0
                    }
                } else {
                    nextView = recycler.getViewForPosition(lastPosition + 1)
                }
                //测量绘制下一个item
                addView(nextView)
                measureChildWithMargins(nextView, 0, 0)
                val decoratedMeasuredWidth = getDecoratedMeasuredWidth(nextView)
                val decoratedMeasuredHeight = getDecoratedMeasuredHeight(nextView)
                layoutDecorated(
                        nextView,
                        0,
                        lastView.bottom,
                        decoratedMeasuredWidth,
                        lastView.bottom + decoratedMeasuredHeight
                )
            }
        } else {//下
            val firstView = getChildAt(0) ?: return 0
            val firstPosition = getPosition(firstView)
//            "下 dy:$dy childCount:$childCount,firstPosition:$firstPosition".log()
            if (firstView.top >= 0) {
                var preView: View
                if (firstPosition == 0) {
                    if (isLooper) {
                        preView = recycler.getViewForPosition(itemCount - 1)//循环闭环
                    } else {
                        return 0
                    }
                } else {
                    preView = recycler.getViewForPosition(firstPosition - 1)
                }
                //测量绘制上一个item
                addView(preView, 0)
                measureChildWithMargins(preView, 0, 0)
                val decoratedMeasuredWidth = getDecoratedMeasuredWidth(preView)
                val decoratedMeasuredHeight = getDecoratedMeasuredHeight(preView)
                layoutDecorated(
                        preView,
                        0,
                        firstView.top - decoratedMeasuredHeight,
                        decoratedMeasuredWidth,
                        firstView.top
                )
//                "top:${preView.top},bottom:${preView.bottom}".log()
            }
        }
        return dy
    }

}























