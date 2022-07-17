package com.fengwo.module_live_vedio.mvp.ui.dialog

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.fengwo.module_comment.ext.clearStyle
import com.fengwo.module_comment.ext.setSpanColor
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.bean.RedPackNotice
import kotlinx.android.synthetic.main.dialog_global_redpack.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * File GlobalRedPackDialog.kt
 * Date 1/4/21
 * Author lucas
 * Introduction 全服红包弹窗
 */
class GlobalRedPackDialog(val c: Context) : BasePopupWindow(c, ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT) {
    override fun onCreateContentView(): View = createPopupById(R.layout.dialog_global_redpack)

    init {
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_out2)
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_in)
        popupGravity = Gravity.CENTER
        setBackgroundColor(context.getColor(R.color.alpha_50_black))
        setOutSideDismiss(true)
        isOutSideTouchable = true
    }

    override fun onViewCreated(contentView: View) {
    }

    fun showContent(bean: RedPackNotice, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        ImageLoader.loadImg(contentView.v_user_icon, bean.data.entity.refUserHeadImg)
        contentView.v_user_name.text = bean.data.entity.refUserName
        val money = bean.data.entity.totalAmount.toString()
        contentView.v_content.run {
            text = "送出${money}花钻的红包"
            text = text.clearStyle().setSpanColor(money, Color.parseColor("#FE3C9C"))
        }
        val refRoomId = bean.data.entity.refRoomId
        if (refRoomId == 0 || refRoomId == liveNoticeConfig.channelId || liveNoticeConfig.isLiving) {
            contentView.v_receiver.text = "我知道了"
            contentView.v_receiver.setOnClickListener { dismiss() }
        } else {
            contentView.v_receiver.text = "前往领取"
            contentView.v_receiver.setOnClickListener {
                dismiss()
                liveNoticeConfig.changeRoom?.invoke(refRoomId)
            }
        }
        showPopupWindow()
    }
}