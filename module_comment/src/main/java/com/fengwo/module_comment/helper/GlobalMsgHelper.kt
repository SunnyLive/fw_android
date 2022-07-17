package com.fengwo.module_comment.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.fengwo.module_comment.Interfaces.IFlirtSocketManager
import com.fengwo.module_comment.event.ActivityWishMessage
import com.fengwo.module_comment.iservice.UserProviderService
import com.fengwo.module_comment.pop.ActPushWishPop
import com.fengwo.module_comment.utils.ArouteUtils
import com.fengwo.module_websocket.EventConstant
import com.fengwo.module_websocket.FWWebSocket1
import com.google.gson.Gson
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.json.JSONObject

/**
 * File GlobalMsgHelper.kt
 * Date 2020/12/12
 * Author lucas
 * Introduction 全局web socket消息处理
 */
object GlobalMsgHelper : FWWebSocket1.OnSocketConnectListener {

    @Autowired
    lateinit var userProviderService: UserProviderService
    @Autowired
    lateinit var flirtSocketManager: IFlirtSocketManager
    var foregroundActivity: RxAppCompatActivity? = null//正在显示的页面
    private val gson = Gson()
    private var onAppBackstage: ((lastActivity: Activity) -> Unit)? = null//当app进入后台
    private var onAppInForeground: (() -> Unit)? = null//当app从后台存活状态进入前台--热启动

    init {
        onAppBackstage = {
//            "进入后台断开链接".logE()
            //断开socket链接
            FWWebSocket1.getInstance().destroy()
            //FWWebSocketWenBo.getInstance().destroy()
            flirtSocketManager.destroy()
        }
        onAppInForeground = {
            userProviderService.userInfo?.also {
//                "后台进入前台重新链接".logE()
                //重连
                FWWebSocket1.getInstance().init(it.id,userProviderService.token)
                FWWebSocket1.getInstance().startRequest()
//                FWWebSocketWenBo.getInstance().init(it.id)
//                FWWebSocketWenBo.getInstance().startRequest()
                flirtSocketManager.connect(it.id)
            }
        }
    }

    fun initHelper(application: Application) {
        ArouteUtils.inject(this)
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            private var foregroundActivityCount = 0//前台activity数量
            private var isBackstage = true

            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                if (isBackstage) {//后台到前台--热启动
                    onAppInForeground?.invoke()
                }
                isBackstage = false
                foregroundActivityCount++
                foregroundActivity = activity as? RxAppCompatActivity
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                foregroundActivityCount--
//                "foregroundActivityCount:$foregroundActivityCount".log()
                if (foregroundActivityCount == 0) {//进入后台
                    isBackstage = true
                    onAppBackstage?.invoke(activity)
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (foregroundActivity == activity)
                    foregroundActivity = null
            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            }

        })
    }

    fun bindMsg() {
        //注册监听
        val instance = FWWebSocket1.getInstance() ?: return
        if (FWWebSocket1.listenerList?.contains(this@GlobalMsgHelper) == true) return
        instance.addOnConnectListener(this@GlobalMsgHelper)
    }

    override fun onReconnect() {
    }

    override fun onMessage(playLoad: String?) {
        if (userProviderService.userInfo == null) return
        if (playLoad != null && foregroundActivity != null) {
            val jsonObject = JSONObject(playLoad)
            if (!jsonObject.has("eventId")) return
            foregroundActivity?.runOnUiThread {
                when (jsonObject.getInt("eventId")) {
                    //   EventConstant.CMD_NOTICEGG -> {//全局许愿通知
                    //       showGlobalWishNotice(jsonObject)
                    //    }
//                    EventConstant.CMD_WISH_MSG -> {//全局许愿弹窗-主播账号
//                        showGlobalWishDialog(jsonObject)
//                    }
//                    EventConstant.CMD_YEAR_MSG -> {//许愿棒通知-主播
//                        showGlobalMakeWishNotice(jsonObject)
//                    }
                }
            }
        }
    }

//    private fun showGlobalMakeWishNotice(jsonObject: JSONObject) {
//        if (!jsonObject.has("data") || foregroundActivity == null) return
//        val msgBean = gson.fromJson(jsonObject.getString("data"), ActivityPropMessage::class.java)
//        GlobalMakeWishDialog(foregroundActivity!!).showDialog(msgBean)
//    }

    private fun showGlobalWishDialog(jsonObject: JSONObject) {
        if (!jsonObject.has("data") || foregroundActivity == null) return
        val msgBean = gson.fromJson(jsonObject.getString("data"), ActivityWishMessage::class.java)
        ActPushWishPop(foregroundActivity!!).showDialog(msgBean)
    }

//    private fun showGlobalWishNotice(jsonObject: JSONObject) {
//        val dataObject = jsonObject.getJSONObject("data")
//        val userNickname = dataObject.getString("userNickname")
//        val anchorNickname = dataObject.getString("anchorNickname")
//        val wishContent = dataObject.getString("wishContent")
//        val content = userNickname.plus(" 对 ").plus(anchorNickname).plus(" 许下了心愿:").plus(wishContent)
//        launchNotice(R.layout.global_notice_view) {
//            it.findViewById<TextView>(R.id.tv_notice_title).also {
//                it.text = content
//                it.text.clearStyle()
//                        .setSpanColor(userNickname, Color.parseColor("#FFFA84"))
//                        .setSpanColor(anchorNickname, Color.parseColor("#FFFA84"))
//                        .into(it)
//                it.isSingleLine = true
//                it.setHorizontallyScrolling(true)
//                it.marqueeRepeatLimit = 1
//                it.isSelected = true
//            }
//        }
//    }



}