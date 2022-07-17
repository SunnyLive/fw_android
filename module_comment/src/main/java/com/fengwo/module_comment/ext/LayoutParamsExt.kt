package com.fengwo.module_comment.ext

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * @package    com.luan.core.ext
 * @author     luan
 * @date       2020/9/16
 * @des
 */

enum class LParam {
    MM, MW, WM, WW, MS, WS, SM, SW
}

fun createViewGroupLP(
    lParam: LParam,
    widthDp: Int = 0,
    heightDp: Int = 0
): ViewGroup.LayoutParams =
    ViewGroup.LayoutParams(0, 0)
        .setLayoutParamsWH(lParam, heightDp, widthDp)

fun createLinearLP(
    lParam: LParam,
    widthDp: Int = 0,
    heightDp: Int = 0
): LinearLayout.LayoutParams =
    LinearLayout.LayoutParams(0, 0)
        .setLayoutParamsWH(lParam, heightDp, widthDp) as LinearLayout.LayoutParams

fun createRelativeLP(
    lParam: LParam,
    widthDp: Int = 0,
    heightDp: Int = 0
): RelativeLayout.LayoutParams =
    RelativeLayout.LayoutParams(0, 0)
        .setLayoutParamsWH(lParam, heightDp, widthDp) as RelativeLayout.LayoutParams

fun createFrameLP(
    lParam: LParam,
    widthDp: Int = 0,
    heightDp: Int = 0
): FrameLayout.LayoutParams =
    FrameLayout.LayoutParams(0, 0)
        .setLayoutParamsWH(lParam, heightDp, widthDp) as FrameLayout.LayoutParams

fun View.setNewLayoutParams(
    lParam: LParam,
    widthDp: Int = 0,
    heightDp: Int = 0
): ViewGroup.LayoutParams? {
    if (parent == null) return null
    var lp: ViewGroup.LayoutParams? = null
    if (parent is FrameLayout) {
        lp = FrameLayout.LayoutParams(0,0).setLayoutParamsWH(lParam,widthDp,heightDp)
    } else if (parent is LinearLayout) {
        lp = LinearLayout.LayoutParams(0,0).setLayoutParamsWH(lParam,widthDp,heightDp)
    } else if (parent is RelativeLayout) {
        lp = RelativeLayout.LayoutParams(0,0).setLayoutParamsWH(lParam,widthDp,heightDp)
    } else if (parent is ViewGroup) {
        lp = ViewGroup.LayoutParams(0,0).setLayoutParamsWH(lParam,widthDp,heightDp)
    }
    layoutParams = lp
    return requireNotNull(lp)
}


private fun ViewGroup.LayoutParams.setLayoutParamsWH(
    lParam: LParam,
    widthDp: Int = 0,
    heightDp: Int = 0
): ViewGroup.LayoutParams {
    when (lParam) {
        LParam.MM -> {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.MATCH_PARENT
        }
        LParam.MW -> {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        LParam.WM -> {
            width = LinearLayout.LayoutParams.WRAP_CONTENT
            height = LinearLayout.LayoutParams.MATCH_PARENT
        }
        LParam.WW -> {
            width = LinearLayout.LayoutParams.WRAP_CONTENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        LParam.MS -> {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = heightDp
        }
        LParam.WS -> {
            width = LinearLayout.LayoutParams.WRAP_CONTENT
            height = heightDp
        }
        LParam.SM -> {
            width = widthDp
            height = LinearLayout.LayoutParams.MATCH_PARENT
        }
        LParam.SW -> {
            width = widthDp
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
    }
    return this
}