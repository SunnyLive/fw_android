package com.fengwo.module_live_vedio.helper.convert

import androidx.appcompat.app.AppCompatActivity
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.bean.NoticeBean
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import java.lang.reflect.ParameterizedType

val gson = Gson()

/**
 * File IConvert.kt
 * Date 1/13/21
 * Author lucas
 * Introduction eventId只决定适配器样式
 */
abstract class IConvert<T : NoticeBean> {
    var msgContent: String? = null
    var onDismiss: ((IConvert<T>) -> Unit)? = null
    var context: AppCompatActivity? = null
    var compositeDisposable: CompositeDisposable? = null
    val showDuration = 5L//飘屏显示的时间
    var receiverTime: Long = 0L//接收到消息时间--三级优先级
    val bean: T by lazy { parseContent() }//消息体

    //解析数据
    private fun parseContent(): T {
        val genericSuperclass = javaClass.genericSuperclass as ParameterizedType
        val type = genericSuperclass.actualTypeArguments[0]
        return gson.fromJson<T>(msgContent, type)
    }

    //是否过滤消息,被过滤的消息不显示
    open fun isFilter(liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig):Boolean{
        return false
    }

    //二级优先级
    open fun getPriority(): Int {
        return 0
    }

    fun initNotice(liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        convert(bean, liveNoticeConfig)
    }

    abstract fun convert(bean: T, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig)

    //队列ID，相同的队列ID回在同一个队列中
    open fun queueId(eventId: Int) = eventId

}