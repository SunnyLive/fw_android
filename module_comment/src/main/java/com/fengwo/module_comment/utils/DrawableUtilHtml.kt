package com.fengwo.module_comment.utils

import android.content.Context
import android.graphics.drawable.Drawable

/**
 * @author ruowuming
 * @version 1.0
 * @Project ShanDui
 * @date 2020/6/23  17:10
 */
class DrawableUtilHtml(var mContext: Context) {
    fun utils(drawable: Drawable): Drawable {
        val displayMetrics = mContext.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val draWidth = drawable.intrinsicWidth
        val draHeight = drawable.intrinsicHeight
        var resWidth = draWidth
        var resHeight = draHeight
        if (draWidth < width && draHeight < height) {
            resWidth = (draWidth * 2.5).toInt()
            resHeight = (draHeight * 2.5).toInt()
        } else if (draHeight > width || draHeight > height) {
            val value = draWidth / width
            resWidth = draWidth / value
            resHeight = draHeight / value
        }
        drawable.setBounds(0, 0, resWidth, resHeight)
        return drawable
    }

}