package com.fengwo.module_comment.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.fengwo.module_comment.R
import com.fengwo.module_comment.base.BaseApplication

/**
 * 红包页面
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
class CommonItemDecoration : RecyclerView.ItemDecoration() {

    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.parseColor("#F3F3F3")
        mPaint.style = Paint.Style.FILL
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        if (childCount > 0) {
            val merge = BaseApplication.mApp.resources.getDimension(R.dimen.dp_20)
            for (i in 1 until childCount) {
                val child = parent.getChildAt(i)
                val left = child.left + merge
                val right = child.right - merge
                val top = child.top
                val bottom = child.top + BaseApplication.mApp.resources.getDimension(R.dimen.dp_1)
                c.drawRect(left, top.toFloat(), right.toFloat(), bottom, mPaint)
            }
        }
    }


}