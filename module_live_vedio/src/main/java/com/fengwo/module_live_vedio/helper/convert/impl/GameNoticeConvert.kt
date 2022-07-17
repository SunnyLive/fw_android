package com.fengwo.module_live_vedio.helper.convert.impl

import android.graphics.Color
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import com.fengwo.module_comment.ext.clearStyle
import com.fengwo.module_comment.ext.isShow
import com.fengwo.module_comment.ext.setSpanColor
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_comment.widget.UrlImageSpan
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.MsgHelper
import com.fengwo.module_live_vedio.helper.bean.GameBean
import com.fengwo.module_live_vedio.helper.convert.INoticeConvert
import com.fengwo.module_live_vedio.helper.convert.annotations.Notice
import com.fengwo.module_websocket.EventConstant
import kotlinx.android.synthetic.main.notice_game.view.*

/**
 * File GameNoticeConvert.kt
 * Date 1/13/21
 * Author lucas
 * Introduction 游戏礼物飘屏
 */
@Notice(EventConstant.BIG_GIFT_PIAOPING)
class GameNoticeConvert : INoticeConvert<GameBean>(R.layout.notice_game) {
    private val imgTag = "[img]"

    override fun convertView(bean: GameBean, view: View, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        (view as ViewGroup).run {
            ImageLoader.loadOrigImg(v_bg, bean.data.background)
            v_name.text = bean.data.nickname
            val spanColor = "在「${bean.data.gameName}」中获得 ${bean.data.giftName}${imgTag}x${bean.data.sendNum}".clearStyle()
                    .setSpanColor(bean.data.giftName, Color.parseColor("#FFDE2A"))
            spanColor.setSpan(UrlImageSpan(view.context, bean.data.giftIcon, v_content), spanColor.length - "${imgTag}x${bean.data.sendNum}".length, spanColor.length - "x${bean.data.sendNum}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            v_content.text = spanColor
            v_onlookers.isShow(!liveNoticeConfig.isLiving && bean.data.anchorUserId != liveNoticeConfig.channelId)
            v_onlookers.setOnClickListener {
                removeNoticeView(view)
                //围观
                liveNoticeConfig.changeRoom?.invoke(bean.data.anchorUserId)
            }
        }
    }

    override fun queueId(eventId: Int): Int {
        return MsgHelper.QUEUE_TOP
    }
}