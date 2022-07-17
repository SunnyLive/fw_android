/*
 *  这是一个支持任意方向滚动的跑马灯view
 *  更具枚举 来指定里面的子view滚动的方向
 */
package com.fengwo.module_comment.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.fengwo.module_comment.R


/**
 *
 * 目前定义这几个值
 * 控制滚动的方向
 *
 */
const val TO_TOP = 0
const val TO_LEFT = 1
const val TO_RIGHT = 2
const val TO_BOTTOM = 3

class MarqueeViewGroup @JvmOverloads constructor(context: Context, var attrs: AttributeSet? = null, var defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init()
    }

    private fun init() {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeViewGroup)
        mDirection = a.getInteger(R.styleable.MarqueeViewGroup_mvg_direction, 0)
        mDuration = a.getInteger(R.styleable.MarqueeViewGroup_mvg_duration, 0)
        isLooper = a.getBoolean(R.styleable.MarqueeViewGroup_isLooper, isLooper)
        isFixed = a.getBoolean(R.styleable.MarqueeViewGroup_isFixed, isFixed)
        a.recycle()
    }


    private lateinit var mAnimator: ObjectAnimator
    private var mChildView: View? = null
    private var mStartLocation = 0f   //开始位置
    private var mEndLocation = 0f   //结束位置
    private var mDirection = 0   //向那个方向滚动
    private var mPropertyName = "translationX"
    private var mDuration = 0   //动画时长
    private var isLooper = false //是否循环滚动
    private var isFixed = false //动画执行结束 是否需要让view固定位置

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount != 1) {
            throw RuntimeException("这个里面只能放一个你需要滚动的子view，这个view可以是任何view，不能没有")
        }
        mChildView = getChildAt(0)
        initLocation()
        startMarquee()
    }

    private fun initLocation() {
        val ls: MarginLayoutParams = layoutParams as MarginLayoutParams
        when (mDirection) {
            TO_TOP -> {
                mStartLocation = bottom.toFloat()
                mEndLocation = (top - if (isFixed) ls.topMargin else bottom).toFloat()
                mPropertyName = "translationY"
            }
            TO_LEFT -> {
                mStartLocation = right.toFloat()
                mEndLocation = (left - if (isFixed) ls.marginStart else right).toFloat()
                mPropertyName = "translationX"
            }
            TO_RIGHT -> {
                mStartLocation = left.toFloat()
                mEndLocation = (right - if (isFixed) ls.marginEnd else left).toFloat()
                mPropertyName = "translationX"
            }
            TO_BOTTOM -> {
                mStartLocation = top.toFloat()
                mEndLocation = (bottom - if (isFixed) ls.bottomMargin else top).toFloat()
                mPropertyName = "translationY"
            }
        }
    }


    @SuppressLint("WrongConstant")
    private fun startMarquee() {
        mAnimator = ObjectAnimator.ofFloat(mChildView, mPropertyName, mStartLocation, mEndLocation)
        mAnimator.apply {
            interpolator = LinearInterpolator()
            duration = mDuration.toLong()
            if (isLooper) {
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.INFINITE
            }
            start()
        }
    }


    /**
     *
     * 当这个view跟组件分离时
     *
     * 在这个地方将动画结束掉
     *
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mChildView?.apply {
            mAnimator.cancel()
            clearAnimation()
        }
    }

}