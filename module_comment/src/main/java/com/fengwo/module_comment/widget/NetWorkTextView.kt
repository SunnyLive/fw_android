package com.fengwo.module_comment.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.fengwo.module_comment.R

enum class NetWorkState(var attr: IntArray) {
    HIGH(intArrayOf(R.attr.state_high)),
    MIDDLE(intArrayOf(R.attr.state_middle)),
    LOW(intArrayOf(R.attr.state_low))
}

class NetWorkTextView : AppCompatTextView {

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private var mNetWorkState: NetWorkState? = null

    fun setNetWorkState(state: NetWorkState) {
        if (mNetWorkState != state) {
            mNetWorkState = state
            refreshDrawableState()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val sExtraSpace = super.onCreateDrawableState(extraSpace + 1)
        mNetWorkState?.apply {
            mergeDrawableStates(sExtraSpace, attr)
        }
        return sExtraSpace
    }


    fun setNetWorkText(net: Int) {
        val text = if(net >= 1024) "${String.format("%.2f", net / 1024.0)}mb/s" else "${net}kb/s"
        setText(text)
    }

}