package com.fengwo.module_live_vedio.mvp.ui.pop

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import kotlinx.android.synthetic.main.pop_newyear_success_activity.view.*
import razerdp.basepopup.BasePopupWindow

/** 赠送成功
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
class GivingGiftsSuccessPopWindow(context: Context, private val img: String, private val name: String, private val icon: String, private val number: String) : BasePopupWindow(context) {

    @SuppressLint("SetTextI18n")
    private fun initData() {
        contentView.apply {
            tv_success_btn.setOnClickListener {
                dismiss()
            }
            im_clone.setOnClickListener {
                dismiss()
            }
            ImageLoader.loadImg(iv_user_header,img)
            ImageLoader.loadImg(im3,icon)
            tvName.text = name
            tv4.text = "x$number"
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_newyear_success_activity)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    init {
        popupGravity = Gravity.CENTER
        initData()
    }


}