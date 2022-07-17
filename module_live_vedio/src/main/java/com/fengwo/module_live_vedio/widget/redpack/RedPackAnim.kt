package com.fengwo.module_live_vedio.widget.redpack

import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import com.fengwo.module_comment.ext.log

/**
 * File RedPackAnim.kt
 * Date 1/20/21
 * Author lucas
 * Introduction 动画实现
 */
class RedPackAnim(val path: Path, val rotation: Float, val view: View,val animTime:Long) : ValueAnimator() {
    val pathMeasure = PathMeasure(path, false)
    val point = FloatArray(2)
    val tan = FloatArray(2)

    init {
        duration = animTime
        setFloatValues(0f,1f)
        addUpdateListener {
            val value = it.animatedValue as Float
            pathMeasure.getPosTan(pathMeasure.length * value, point, tan)
            view.x = point[0] - view.measuredWidth / 2
            view.y = point[1]
            view.rotation = rotation * value
//            "point:${point.toList()}".log()
        }
    }


}