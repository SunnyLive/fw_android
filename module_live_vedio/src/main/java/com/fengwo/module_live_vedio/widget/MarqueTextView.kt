package com.fengwo.module_live_vedio.widget

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.fengwo.module_live_vedio.R

/**
 * File MarqueTextView.kt
 * Date 1/27/21
 * Author lucas
 * Introduction
 */
class MarqueTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        attrs?.apply {
            val obtain = context.obtainStyledAttributes(attrs, R.styleable.MarqueTextView)
            val delayTime = obtain.getInt(R.styleable.MarqueTextView_delayTime, 0)
            obtain.recycle()
            postDelayed({
                startMarquee()
            }, delayTime.toLong())
        }
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.END
    }

    fun startMarquee() {
        ellipsize = TextUtils.TruncateAt.MARQUEE
    }

    override fun isFocused(): Boolean {
        return true
    }
}