package com.fengwo.module_live_vedio.helper.convert

import com.fengwo.module_comment.ext.onDestroy
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.bean.NoticeBean
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * File IDialogConvert.kt
 * Date 1/11/21
 * Author lucas
 * Introduction 飘窗类通知
 */
abstract class IDialogConvert<T : NoticeBean> :IConvert<T>(){

    abstract fun convertView(bean: T, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig)
    abstract fun dismiss()

    override fun convert(bean: T, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        val subscribe = Observable.just(convertView(bean, liveNoticeConfig))
                .delay(showDuration, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                    onDismiss?.invoke(this)
                }
        context?.lifecycle?.onDestroy {
            dismiss()
        }
        compositeDisposable?.add(subscribe)
    }
}