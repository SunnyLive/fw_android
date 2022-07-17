package com.fengwo.module_comment.ext

import android.view.MotionEvent
import android.view.View
import com.fengwo.module_comment.R
import com.fengwo.module_comment.utils.KeyBoardUtils
import kotlin.math.abs

/**
 * File ViewExt.kt
 * Date 2020/12/7
 * Author lucas
 * Introduction view 相关的扩展函数
 */

const val customClickTime = 200L

fun customClick(view: View, block: (View) -> Unit): View.OnTouchListener {
    return object : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            //键盘显示时，不响应事件，防止事件穿透
            if (KeyBoardUtils.isSHowKeyboard(v.context, v)) return true
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.setTag(R.id.fast_click_id, System.currentTimeMillis())
                    view.setTag(R.id.fast_click_x, event.x)
                    view.setTag(R.id.fast_click_y, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                    val time = view.getTag(R.id.fast_click_id) as? Long
                    val x = view.getTag(R.id.fast_click_x) as? Float
                    val y = view.getTag(R.id.fast_click_y) as? Float
                    if (time != null && x != null && y != null) {
                        if (abs(time - System.currentTimeMillis()) <= customClickTime
                                && abs(x - event.x) <= 5 && abs(y - event.y) <= 5) {
                            block.invoke(view)
                        }
                    }
                }
            }
            return true
        }
    }
}

//设置自定义点击事件
fun View.setCustomClick(block: (View) -> Unit) {
    val value = object : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            //键盘显示时，不响应事件，防止事件穿透
            if (KeyBoardUtils.isSHowKeyboard(v.context, v)) return true
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    setTag(R.id.fast_click_id, System.currentTimeMillis())
                    setTag(R.id.fast_click_x, event.x)
                    setTag(R.id.fast_click_y, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                    val time = getTag(R.id.fast_click_id) as? Long
                    val x = getTag(R.id.fast_click_x) as? Float
                    val y = getTag(R.id.fast_click_y) as? Float
                    if (time != null && x != null && y != null) {
                        if (abs(time - System.currentTimeMillis()) <= customClickTime
                                && abs(x - event.x) <= 5 && abs(y - event.y) <= 5) {
                            block.invoke(v)
                        }
                    }
                }
            }
            return true
        }
    }
    setOnTouchListener(value)
}

//显示view
fun View.show() {
    visibility = View.VISIBLE
}

//隐藏控件包括空间
fun View.gone() {
    visibility = View.GONE
}

//隐藏view
fun View.invisibility() {
    visibility = View.INVISIBLE
}

//是否显示view
fun View.isShow(isShow: Boolean) {
    if (isShow) show() else gone()
}

private const val clickInterval = 500//点击间隔

//批量设置事件,并且防止暴力点击
fun <T : View> Array<T>.addClicks(onClickListener: View.OnClickListener) {
    forEach {
        it.setOnClickListener {
            val lastClick = it.getTag(R.id.fast_click_id) as? Long
            val currentTimeMillis = System.currentTimeMillis()
            if (lastClick == null || currentTimeMillis - lastClick > clickInterval) {
                it.setTag(R.id.fast_click_id, currentTimeMillis)
                onClickListener.onClick(it)
            }
        }
    }
}

//单个点击事件
fun <T : View> T.addClick(onClickListener: (view: View) -> Unit) {
    setOnClickListener {
        val lastClick = it.getTag(R.id.fast_click_id) as? Long
        val currentTimeMillis = System.currentTimeMillis()
        if (lastClick == null || currentTimeMillis - lastClick > clickInterval) {
            it.setTag(R.id.fast_click_id, currentTimeMillis)
            onClickListener.invoke(it)
        }
    }
}
