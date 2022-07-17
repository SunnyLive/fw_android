package com.fengwo.module_live_vedio.helper.convert

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.fengwo.module_comment.ext.intoAndStart
import com.fengwo.module_comment.ext.loadAnim
import com.fengwo.module_comment.ext.log
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper
import com.fengwo.module_live_vedio.helper.bean.NoticeBean
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * File INoticeConvert.kt
 * Date 1/11/21
 * Author lucas
 * Introduction 飘屏类通知
 */
abstract class INoticeConvert<T : NoticeBean>(@LayoutRes val layoutIdRes: Int) : IConvert<T>() {
    var showAnimId = R.anim.anim_global_make_wish_show
    var hideAnimId = R.anim.anim_global_make_wish_dismiss
    var isEnableTouchRemove = true//是否开启手势移除

    var containerView: ViewGroup? = null
    var disSubscribe: Disposable? = null
    var onShowAnimEnd: (() -> Unit)? = null//入场动画结束
    var onHideAnimEnd: (() -> Unit)? = null//出场动画结束

    abstract fun convertView(bean: T, view: View, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig)

    override fun convert(bean: T, liveNoticeConfig: LiveNoticeHelper.LiveNoticeConfig) {
        containerView?.let {container->
            val inflate = LayoutInflater.from(container.context).inflate(layoutIdRes, container, false)
            convertView(bean, inflate, liveNoticeConfig)
            if (isEnableTouchRemove) {
                touchRemove(inflate, container)
            }

            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            container.addView(inflate, layoutParams)
            container.context?.loadAnim(showAnimId, onStop = { showAnim ->
                onShowAnimEnd?.invoke()
                disSubscribe = Observable.just(dismissViewAnim(inflate))
                        .delay(showDuration, TimeUnit.SECONDS)
                        .subscribe {
                            containerView?.post {
                                inflate.clearAnimation()
                                it?.intoAndStart(inflate)
                            }
                        }
                disSubscribe?.let { compositeDisposable?.add(it) }
            })?.intoAndStart(inflate)
        }

    }

    //移除通知
    fun removeNoticeView(noticeView: View) {
        //移除通知
        containerView?.post {
            noticeView.clearAnimation()
            containerView?.removeView(noticeView)
            onDismiss?.invoke(this)
        }
    }

    //移除item
    private fun dismissViewAnim(inflate: View?): Animation? {
        if (containerView == null) {
//            "onDismiss:$onDismiss".log()
            onDismiss?.invoke(this)
            return null
        }
        return containerView?.context?.loadAnim(hideAnimId, onStop = {
            onHideAnimEnd?.invoke()
            containerView?.post {
                containerView?.removeView(inflate)
//                "${containerView?.childCount},remove view :$inflate".log()
                onDismiss?.invoke(this)
            }
        })
    }

    var lastX = 0f
    var lastY = 0f

    //处理手势移除
    private fun touchRemove(inflate: View, containerView: ViewGroup?) {
        inflate.setOnTouchListener { v, event ->
//            "touchRemove：${event.action}".log()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (abs(lastX - event.x) < abs(lastY - event.y) && abs(lastY - event.y) > 20) {//滑动移除
//                        "滑动移除".log()
                        dismissViewAnim(inflate)?.also {
                            containerView?.post {
                                disSubscribe?.dispose()
                                inflate.clearAnimation()
                                it.intoAndStart(inflate)
                            }
                        }
                        inflate.setOnTouchListener(null)
                        return@setOnTouchListener false
                    }
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                    return@setOnTouchListener false
                }
            }
            return@setOnTouchListener false
        }
    }


}