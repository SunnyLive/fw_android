package com.fengwo.module_live_vedio.helper.convert.impl

import android.view.View
import android.view.ViewGroup
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.MsgHelper
import com.fengwo.module_live_vedio.helper.bean.MakeWishBean
import com.fengwo.module_live_vedio.helper.convert.INoticeConvert
import com.fengwo.module_live_vedio.helper.convert.annotations.Notice
import com.fengwo.module_websocket.EventConstant
import kotlinx.android.synthetic.main.global_window_make_wish.view.*

/**
 * File MakeWishNoticeConvert.kt
 * Date 1/13/21
 * Author lucas
 * Introduction 许愿棒
 */
@Notice(EventConstant.CMD_YEAR_MSG, scope = Notice.Scope.GLOBAL)
class MakeWishNoticeConvert : INoticeConvert<MakeWishBean>(R.layout.global_window_make_wish) {
    override fun convertView(bean: MakeWishBean, view: View, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        (view as ViewGroup).run {
            ImageLoader.loadImg(v_user_icon, bean.data.userHeadImg)
            ImageLoader.loadImg(v_gift_icon, bean.data.propIcon)
            v_user_name.text = bean.data.userNickname
            v_gift_name.text = "送的".plus(bean.data.propName)
            v_gift_num.text = "x".plus(bean.data.propQuantity)
        }
    }

    override fun queueId(eventId: Int): Int {
        return MsgHelper.QUEUE_TOP
    }
}