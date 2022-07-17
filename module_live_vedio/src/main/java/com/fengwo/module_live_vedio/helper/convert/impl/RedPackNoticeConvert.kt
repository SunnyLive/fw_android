package com.fengwo.module_live_vedio.helper.convert.impl

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.fengwo.module_comment.ext.gone
import com.fengwo.module_comment.ext.isShow
import com.fengwo.module_comment.ext.log
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.MsgHelper
import com.fengwo.module_live_vedio.helper.bean.RedPackNotice
import com.fengwo.module_live_vedio.helper.convert.INoticeConvert
import com.fengwo.module_live_vedio.helper.convert.annotations.Notice
import com.fengwo.module_websocket.EventConstant
import io.reactivex.Observable
import io.reactivex.Observer
import kotlinx.android.synthetic.main.notice_global_redpack.view.*
import java.util.concurrent.TimeUnit

/**
 * File RedPackConvert.kt
 * Date 1/11/21
 * Author lucas
 * Introduction 红包飘屏
 */
@Notice(EventConstant.CMD_PACKET_NOTICE)
class RedPackNoticeConvert : INoticeConvert<RedPackNotice>(R.layout.notice_global_redpack) {
    override fun convertView(bean: RedPackNotice, view: View, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
//        "show type:${bean.data.type}".log()
        (view as ViewGroup).run {
            v_name.text = bean.data.entity.refUserName
            ImageLoader.loadOrigImg(v_bg, bean.data.entity.background)
            val refRoomId = bean.data.entity.refRoomId
            when (bean.data.type) {
                1 -> {//本房间红包
                    v_content.text = "发出红包，快去试试手气吧！"
                    v_onlookers.isShow(!liveNoticeConfig.isLiving && liveNoticeConfig.channelId != refRoomId)
                }
                4 -> {//全栈红包
                    v_content.text = "发出全服红包，快去试试手气吧! "
                    v_onlookers.gone()
                }
            }
            v_onlookers.setOnClickListener {
                //移除通知
                removeNoticeView(view)
                //围观
                liveNoticeConfig.changeRoom?.invoke(refRoomId)
            }
        }
    }

    override fun isFilter(liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig): Boolean {
        return !(bean.data.type == 1 || bean.data.type == 4)
    }


    override fun getPriority() = when (bean.data.type) {
        4 -> 1
        else -> 0
    }

    override fun queueId(eventId: Int): Int {
        return MsgHelper.QUEUE_TOP
    }


}