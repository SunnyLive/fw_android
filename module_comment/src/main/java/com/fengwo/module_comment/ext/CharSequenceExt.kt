package com.fengwo.module_comment.ext

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import com.fengwo.module_comment.base.BaseApplication

/**
 * @package    com.cj.impl.ext
 * @author     luan
 * @date       2020/8/24
 * @des
 */

//清除样式
fun CharSequence.clearStyle(): SpannableStringBuilder {
    val builder = SpannableStringBuilder(this)
    builder.clearSpans()
    return builder
}

fun CharSequence.into(textView: TextView){
    textView.text = this
}

//如果调用过SpannableStringBuilder.setClick 就必须调用该方法，否则会导致事件失效
fun CharSequence.intoAndClickable(textView: TextView){
    textView.movementMethod = LinkMovementMethod.getInstance()
    textView.highlightColor = Color.TRANSPARENT
    textView.text = this
}

//设置颜色
fun SpannableStringBuilder.setSpanColor(spanText: String, color: Int): SpannableStringBuilder {
    if (!contains(spanText)) return this
    this.setSpan(
            ForegroundColorSpan(color),
            indexOf(spanText),
            indexOf(spanText) + spanText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return this
}

//设置事件
fun SpannableStringBuilder.setClick(spanText: String, block: ((text: String) -> Unit)? = null): SpannableStringBuilder {
    if (!contains(spanText)) return this
    setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            block?.invoke(spanText)
        }

        override fun updateDrawState(ds: TextPaint) {
//            super.updateDrawState(ds)
            ds.isUnderlineText = false//去掉下划线
        }
    }, indexOf(spanText),
            indexOf(spanText) + spanText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

//设置大小
fun SpannableStringBuilder.setSpanSize(spanText: String, sizeSp: Int): SpannableStringBuilder {
    if (!contains(spanText)) return this
    this.setSpan(
            AbsoluteSizeSpan(sizeSp, true),
            indexOf(spanText),
            indexOf(spanText) + spanText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

/**
 *  设置样式
 * @receiver SpannableStringBuilder
 * @param spanText String
 * @param style Int {@link Typeface}
 * @return SpannableStringBuilder
 */
fun SpannableStringBuilder.setSpanStyle(spanText: String, style: Int): SpannableStringBuilder {
    if (!contains(spanText)) return this
    this.setSpan(
            StyleSpan(style),
            indexOf(spanText),
            indexOf(spanText) + spanText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}
