package com.fengwo.module_live_vedio.mvp.ui.dialog.h5game

import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_live_vedio.BuildConfig
import com.fengwo.module_live_vedio.R
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import razerdp.basepopup.BasePopupWindow

/**
 * File H5GameDialog.kt
 * Date 1/6/21
 * Author lucas
 * Introduction h5游戏
 */
class H5GameDialog(val mvpActivity: BaseMvpActivity<*,*>) : BasePopupWindow(mvpActivity, MATCH_PARENT, MATCH_PARENT) {
    override fun onCreateContentView(): View = WebView(context)
    private lateinit var webView: WebView


    override fun onViewCreated(contentView: View) {
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_in)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_out2)
        webView = contentView as WebView
        webView.setBackgroundColor(Color.TRANSPARENT)
        setBackgroundColor(Color.TRANSPARENT)
        webView.addJavascriptInterface(H5GameJSInterface(this,context as MvpView),"android")
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportZoom(true)
            builtInZoomControls = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setAppCacheEnabled(false)
            supportMultipleWindows()
            pluginState = WebSettings.PluginState.ON_DEMAND
            loadsImagesAutomatically = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            allowFileAccess = true
        }

    }

    fun showByUrl(url:String){
        webView.loadUrl(url)
        showPopupWindow()
    }
}