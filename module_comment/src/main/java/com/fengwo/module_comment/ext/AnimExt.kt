package com.fengwo.module_comment.ext

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.lifecycle.Lifecycle
import com.fengwo.module_comment.utils.KLog


fun Context.loadAnim(animId: Int, onStart: (Animation) -> Unit = {}, onStop: (Animation) -> Unit = {}): Animation {
    val anim = AnimationUtils.loadAnimation(this, animId)
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation) {
        }

        override fun onAnimationEnd(p0: Animation) {
            onStop(p0)
        }

        override fun onAnimationStart(p0: Animation) {
            onStart(p0)
        }
    })
    return anim
}

fun Animation.intoAndStart(view: View) {
    view.animation = this
    start()
}

//摇摆动画
fun View.startRotateAnim() {
    val animation = TranslateAnimation(8f, -8f, 0f, 0f)
    animation.interpolator = LinearInterpolator()
    animation.duration = 200
    animation.repeatCount = -1
    animation.repeatMode = Animation.REVERSE
    startAnimation(animation)
}

//旋转摇摆
fun View.startRotateValueAnimator(mLifecycle: Lifecycle) {
    val mValueAnimator = ValueAnimator()
    mValueAnimator.setFloatValues(25f, -25f, 25f)
    val mRotate = ValueAnimator.AnimatorUpdateListener { animation ->
        val value = animation.animatedValue as Float
        rotation = value
    }
    mValueAnimator.interpolator = LinearInterpolator()
    mValueAnimator.duration = 1000
    mValueAnimator.repeatCount = -1
    mValueAnimator.addUpdateListener(mRotate)
    mValueAnimator.start()
    mLifecycle.onDestroy {
        KLog.i("startRotateValueAnimator", "释放startRotateValueAnimator")
        mValueAnimator.pause()
        mValueAnimator.removeAllListeners()
    }
}