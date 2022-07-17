package com.fengwo.module_live_vedio.mvp.ui.dialog.h5game

import android.webkit.JavascriptInterface
import com.fengwo.module_comment.api.JSInterface
import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_comment.utils.RxBus
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent
import razerdp.basepopup.BasePopupWindow

/**
 * File H5GameJSInterface.kt
 * Date 1/6/21
 * Author lucas
 * Introduction h5游戏桥接
 */
class H5GameJSInterface(val dialog:BasePopupWindow,val mvpView: MvpView): JSInterface() {

    private val bus = RxBus.get()

    //关闭当前弹窗
    @JavascriptInterface
    fun dismissDialog(){
        dialog.dismiss()
    }

    //打开充值弹窗
    @JavascriptInterface
    fun showRechargeDialog(){
        bus.post(ShowRechargePopEvent())
    }

    //登录过期
    @JavascriptInterface
    fun tokenInvalid(){
        mvpView.tokenIInvalid()
    }

}