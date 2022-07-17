package com.fengwo.module_live_vedio.widget.redpack

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.taobao.windvane.util.DPUtil
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.fengwo.module_comment.ext.log
import com.fengwo.module_live_vedio.R

/**
 * File FallingView.kt
 * Date 12/25/20
 * Author lucas
 * Introduction 飘落物件控件
 *              规则：通过适配器实现
 */
class FallingView : FrameLayout, Runnable {
    private val TAG = FallingView::class.java.simpleName
    private var handlerTask = Handler()
    private var iFallingAdapter: IFallingAdapter<*>? = null
    private var position = 0//当前item
    private var fallingListener: OnFallingListener? = null
    private var lastStartTime = 0L//最后一个item开始显示的延迟时间
    var isFalling = false
    var isForceStop = false//是否是强制结束
    private val touchEventScope by lazy { DPUtil.dip2px(60f) }

    private val cacheHolder = HashSet<Holder>()//缓存holder，用于复用，减少item view创建的个数

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
//        setWillNotDraw(false)//放开注释可显示辅助线
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        (parent as ViewGroup).findViewById<View>(R.id.rv)?.dispatchTouchEvent(ev)
        (parent as ViewGroup).findViewById<View>(R.id.falling_layout)?.dispatchTouchEvent(ev)
        if (height - ev.y <= touchEventScope) {
            (parent as ViewGroup).findViewById<View>(R.id.drawerLayout)?.dispatchTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    //开始飘落
    fun startFalling() {
        if (isFalling) return
        if (iFallingAdapter == null) {
            Log.e(TAG, "iFallingAdapter not be null.")
            return
        }
        isFalling = true
        isForceStop = false
        position = 0
        handlerTask.post(this)
    }

    //停止飘落
    fun stopFalling() {
        if (!isFalling) return
        isForceStop = true
        handlerTask.removeCallbacks(this)
        //停止所有动画
        for (i in 0 until childCount) {
            getChildAt(i).clearAnimation()
        }
        removeAllViews()
        if (isFalling)
            fallingListener?.onStop()
        isFalling = false
    }

    override fun run() {
        iFallingAdapter?.also { adapter ->
            if (adapter.datas.isNullOrEmpty() || position > adapter.datas!!.size - 1) return
//            "position:$position".p()
            showItem(adapter)
            invalidate()
        }
    }

    private fun showItem(adapter: IFallingAdapter<*>) {
        if (position == 0) {
            fallingListener?.onStart()
        }
        var holder: Holder
        if (cacheHolder.isEmpty()) {
            val inflate = LayoutInflater.from(context).inflate(adapter.layoutId, this, false)
            holder = Holder(inflate)
        } else {//从缓存中获取holder
            val iterator = cacheHolder.iterator()
            holder = iterator.next()
            iterator.remove()
        }
        holder.position = position
        addView(holder.view)
        adapter.convert(this, holder)
        holder.config.anim = adapter.convertAnim(this, holder)
        holder.config.anim?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                closeAnim()
            }

            override fun onAnimationCancel(animation: Animator?) {
                closeAnim()
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            private fun closeAnim() {
                //将item加入缓存以复用
                cacheHolder.add(holder)
                removeView(holder.view)
//                "childCount:$childCount,adapter.datas?.size:${adapter.datas?.size},position:${position},holder.position:${holder.position}".log()
                if (childCount == 0 && adapter.datas?.size == position && isFalling) {
                    isFalling = false
                    fallingListener?.onStop()
                }
//                "cacheHolder:${cacheHolder.size}".log()
            }
        })
        holder.config.anim?.start()
        //显示完一个item后准备显示下一个item
        handlerTask.postDelayed(this, holder.config.startTime - lastStartTime)
        lastStartTime = holder.config.startTime
//        "position:$position".log()
        position++
    }

    //设置适配器
    fun <T> setAdapter(adapter: IFallingAdapter<T>) {
        iFallingAdapter = adapter
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //辅助线
        cacheHolder.forEach { enty ->
            enty.config.path?.also { assistLine(it, canvas) }
        }
    }


    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 4f
    }

    //辅助线
    private fun assistLine(path: Path, canvas: Canvas) {
        canvas.drawPath(path, paint)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopFalling()
    }

    class Holder(val view: View) {
        var config: Config = Config()
        var position: Int = 0
    }

    //适配器
    abstract class IFallingAdapter<T>(@LayoutRes val layoutId: Int) {
        var datas: List<T>? = null

        //复用
        abstract fun convert(parent: ViewGroup, holder: Holder)

        //创建动画轨迹
        abstract fun convertAnim(parent: ViewGroup, holder: Holder): Animator

    }

    //初始化配置
    class Config {
        var startTime = 0L//开始发射时间
        var anim: Animator? = null
        var path: Path? = null
    }

    fun setOnFallingListener(onFallingListener: OnFallingListener) {
        fallingListener = onFallingListener
    }

    interface OnFallingListener {
        fun onStart()

        fun onStop()
    }

}