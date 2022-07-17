package com.fengwo.module_live_vedio.helper.convert.impl

import com.fengwo.module_comment.ext.log
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.bean.RedPackNotice
import com.fengwo.module_live_vedio.helper.convert.IDialogConvert
import com.fengwo.module_live_vedio.helper.convert.annotations.Notice
import com.fengwo.module_live_vedio.mvp.ui.dialog.GlobalRedPackDialog
import com.fengwo.module_websocket.EventConstant

/**
 * File RedPackDialogCovert.kt
 * Date 1/11/21
 * Author lucas
 * Introduction 红包飘窗
 */
@Notice(EventConstant.CMD_PACKET_DIALOG)
class RedPackDialogCovert : IDialogConvert<RedPackNotice>() {
    var globalRedPackDialog: GlobalRedPackDialog? = null
    override fun convertView(bean: RedPackNotice, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
//        "dialog show type:${bean.data.type}".log()
        context?.also {
            globalRedPackDialog = GlobalRedPackDialog(it)
            globalRedPackDialog?.showContent(bean, liveNoticeConfig)
        }
    }

    override fun isFilter(liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig): Boolean {
        return !(bean.data.type == 2 || bean.data.type == 5) || liveNoticeConfig.isLiving
    }

    override fun getPriority() = when (bean.data.type) {
        5 -> 1
        else -> 0
    }

    override fun dismiss() {
        globalRedPackDialog?.dismiss()
        globalRedPackDialog = null
    }

}