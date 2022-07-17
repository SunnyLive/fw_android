package com.fengwo.module_live_vedio.helper.convert.impl

import com.fengwo.module_comment.helper.GlobalMsgHelper
import com.fengwo.module_comment.pop.ActPushWishPop
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.bean.WishNoticeBean
import com.fengwo.module_live_vedio.helper.convert.IDialogConvert
import com.fengwo.module_live_vedio.helper.convert.annotations.Notice
import com.fengwo.module_websocket.EventConstant

/**
 * File WishDialogConvert.kt
 * Date 2/5/21
 * Author lucas
 * Introduction 全局许愿弹窗-主播账号
 */
@Notice(eventId = EventConstant.CMD_WISH_MSG, scope = Notice.Scope.GLOBAL)
class WishDialogConvert : IDialogConvert<WishNoticeBean>() {
    private var dialog: ActPushWishPop? = null

    override fun convertView(bean: WishNoticeBean, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        dialog = ActPushWishPop(context)
        dialog?.showDialog(bean.data)
    }

    override fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}