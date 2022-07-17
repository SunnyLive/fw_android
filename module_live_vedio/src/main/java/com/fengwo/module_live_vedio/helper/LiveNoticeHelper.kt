package com.fengwo.module_live_vedio.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.fengwo.module_comment.ext.*
import com.fengwo.module_live_vedio.helper.bean.NoticeBean
import com.fengwo.module_live_vedio.helper.convert.*
import com.fengwo.module_live_vedio.helper.convert.annotations.Notice
import com.fengwo.module_live_vedio.helper.convert.impl.*
import com.google.gson.Gson
import com.zhy.view.flowlayout.TagFlowLayout.dip2px

import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * File LiveNoticeHelper.kt
 * Date 1/8/21
 * Author lucas
 * Introduction 直播飘屏
 */
object LiveNoticeHelper {
    //通知队列
    private val noticeQueueMap = HashMap<Int, ConcurrentLinkedQueue<IConvert<out NoticeBean>>>()
    //正在显示的通知
    private val runningNotice = ArrayList<IConvert<out NoticeBean>>()
    private val gson = Gson()
    private val convertList = HashMap<Notice, Class<out IConvert<out NoticeBean>>>()//适配器
    private var containerView: LinearLayout? = null
    private var globalContainerView: LinearLayout? = null
    private val handler = Handler(Looper.getMainLooper())
    private val compositeDisposable = CompositeDisposable()
    private val taskList = ArrayList<NoticeTask>()//任务
    val liveNoticeConfig = LiveNoticeConfig()//直播间参数/回调
    private var activity: AppCompatActivity? = null
    private var foregroundActivity: AppCompatActivity? = null

    init {
        //注册适配器
        addConvertType(RedPackNoticeConvert::class.java)
        addConvertType(RedPackDialogCovert::class.java)
        addConvertType(GameNoticeConvert::class.java)
        addConvertType(MakeWishNoticeConvert::class.java)
        addConvertType(WishDialogConvert::class.java)
    }

    fun initHelper(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                foregroundActivity = activity as? AppCompatActivity
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                if (activity == foregroundActivity) {
                    foregroundActivity = null
                }
                globalContainerView = null
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }

    //添加适配器，读取事件ID
    private fun addConvertType(iConvert: Class<out IConvert<out NoticeBean>>) {
        val eventId = iConvert.annotations.find { it is Notice } as? Notice
        if (eventId != null) {
            convertList[eventId] = iConvert
        } else {
            throw IllegalArgumentException("${iConvert}缺少Notice注解")
        }
    }

    //设置公告容器--Scope为single的通知必须要调用改方法
    fun setNoticeContainer(activity: AppCompatActivity, container: LinearLayout) {
        containerView = container
        this.activity = activity
        activity.lifecycle.onDestroy {//释放资源
            releaseSingleScope()
        }
    }


    //添加新的公告
    fun sendNotice(msg: String) {
        val ibean = gson.fromJson<NoticeBean>(msg, NoticeBean::class.java)
        //找到对应的消息适配器
        var iConvert: IConvert<out NoticeBean>? = null
        var notice: Notice? = null
        convertList.find { it.key.eventId.toString() == ibean.eventId }?.let {
            notice = it.key
            iConvert = it.value.newInstance()
        }
        iConvert?.also { convert ->
            convert.msgContent = msg
            //过滤消息
            if (convert.isFilter(liveNoticeConfig)) return
            //找到通知类型对应的队列
            val queueId = convert.queueId(notice?.eventId ?: 0)
            if (!noticeQueueMap.containsKey(queueId)) {
                noticeQueueMap[queueId] = ConcurrentLinkedQueue()
            }
            //将任务加入队列
            pushNotice(queueId, convert, ibean, notice)
            if (containerView != null && containerView!!.childCount == 0) {
                var task = taskList.find { it.queueId == queueId }
                if (task == null) {
                    //创建新的任务
                    task = NoticeTask(queueId)
                    taskList.add(task)
//                    "queueId:${queueId} 创建新的任务".log()
                }
                if (!task.isRunning) {
                    //开始任务
                    task.isRunning = true
                    handler.post(task)
//                    "queueId:${queueId} 开始任务".log()
                }
            }
        }
    }

    //将任务加入队列
    private fun pushNotice(queueId: Int, convert: IConvert<out NoticeBean>, ibean: NoticeBean, notice: Notice?) {
        convert.receiverTime = System.currentTimeMillis()
        noticeQueueMap[queueId]?.add(convert)
        //多重排序-先按照优先级降序排序，再按照接收到的时间升序排序
        noticeQueueMap[queueId]?.sortedWith(Comparator<IConvert<out NoticeBean>> { bean1, bean2 ->
            val noticeAnnotation1 = getNoticeAnnotation(bean1)
            val noticeAnnotation2 = getNoticeAnnotation(bean2)
            var result = noticeAnnotation2.priority - noticeAnnotation1.priority
            result = if (result == 0) (bean2.getPriority() - bean1.getPriority()) else result
            result = if (result == 0) (bean1.receiverTime - bean2.receiverTime).toInt() else result
            return@Comparator result
        })?.let { noticeQueueMap[queueId] = ConcurrentLinkedQueue(it) }

    }

    //每个queueId对应一个通知任务
    private class NoticeTask(val queueId: Int) : Runnable {
        var isRunning = false//任务是否正在运行

        override fun run() {
            val noticeQueue = noticeQueueMap[queueId]
            if (noticeQueue == null) {
//                "停止任务1,queueId:${queueId}".log()
                isRunning = false
                handler.removeCallbacks(this)
                return
            } else {
                val notice = noticeQueue.poll()
                if (foregroundActivity == null || notice == null) {
//                    "停止任务2,queueId:${queueId},noticeQueue:${noticeQueue.size}".log()
                    isRunning = false
                    handler.removeCallbacks(this)
                    return
                }
                if (getNoticeAnnotation(notice).scope == Notice.Scope.SINGLE) {
                    if (containerView == null) {
                        handler.post(this)
                    }
                }
                isRunning = true
                runningNotice.add(notice)
//                "show notice,queueId:${queueId} noticeQueue size:${noticeQueue.size},containerView:$containerView,notice:$notice".log()
                showNotice(notice) {
//                    "dis callback".log()
                    runningNotice.remove(notice)
                    releaseConvert(notice)
                    handler.post(this)
                }
            }
        }
    }

    //显示公告
    private fun showNotice(notice: IConvert<*>, onDismiss: (IConvert<*>) -> Unit) {
        val noticeAnnotation = getNoticeAnnotation(notice)
        var noticeContainer: ViewGroup? = null
        if (noticeAnnotation.scope == Notice.Scope.GLOBAL) {//全局通知
            notice.context = foregroundActivity
            if (globalContainerView == null) {
                globalContainerView = createNoticeContainerByActivity(foregroundActivity)
            }
            noticeContainer = globalContainerView
        } else if (noticeAnnotation.scope == Notice.Scope.SINGLE) {//单界面通知
            if (containerView == null) {
                onDismiss.invoke(notice)
                return
            }
            notice.context = activity
            noticeContainer = containerView
        }

        notice.compositeDisposable = compositeDisposable
        notice.onDismiss = onDismiss
        when (notice) {
            is INoticeConvert -> {//飘屏
                notice.containerView = noticeContainer
                notice.initNotice(liveNoticeConfig)
            }
            is IDialogConvert -> {//飘窗
                notice.initNotice(liveNoticeConfig)
            }
        }
    }

    //在制定的activity里创建一个view容器
    private fun createNoticeContainerByActivity(foregroundActivity: AppCompatActivity?): LinearLayout? {
        foregroundActivity?.also { activity ->
            val rootView = activity.findViewById<FrameLayout>(android.R.id.content)
            val linearLayout = LinearLayout(activity)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.gravity = Gravity.CENTER
            linearLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            linearLayout.setPadding(0, dip2px(activity, 20f), 0, 0)
            rootView.addView(linearLayout)
            return linearLayout
        }
        return null
    }

    private fun getNoticeAnnotation(notice: IConvert<*>): Notice {
        return convertList.find { it.value.name == notice.javaClass.name }!!.key
    }

    private fun releaseConvert(it: IConvert<*>) {
        it.context = null
        it.onDismiss = null
        if (it is INoticeConvert) {
            it.containerView = null
            it.disSubscribe?.also {
                compositeDisposable.remove(it)
                it.dispose()
            }
            it.disSubscribe = null
        }
    }

    //释放作用域为single的资源
    private fun releaseSingleScope() {
        //释放队列中的部分通知
        noticeQueueMap.forEach { map ->
            map.value.forEach {
                if (getNoticeAnnotation(it).scope == Notice.Scope.SINGLE) {
                    releaseConvert(it)//释放资源，防止内存泄漏
                }
            }
            //将消息移除队列
            map.value.removeAll(map.value.filter { getNoticeAnnotation(it).scope == Notice.Scope.SINGLE })
            //释放single通知资源后回导致回调清空，task停止，这里需要重写触发下任务
            taskList.forEach { handler.post(it) }
        }
        //释放正在显示的通知
        runningNotice.forEach {
            releaseConvert(it)
        }
        runningNotice.clear()
        for (i in 0 until (containerView?.childCount ?: 0)) {
            containerView?.getChildAt(i)?.clearAnimation()
        }
        containerView?.removeAllViews()
        containerView = null
        liveNoticeConfig.changeRoom = null
        this.activity = null

    }

    class LiveNoticeConfig {
        var isLiving = false//当前是否正在直播
        var channelId: Int = 0//主播ID
        var changeRoom: ((channelId: Int) -> Unit)? = null//切换直播间
    }
}