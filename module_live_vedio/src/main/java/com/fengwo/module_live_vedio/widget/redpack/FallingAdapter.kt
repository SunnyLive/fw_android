package com.fengwo.module_live_vedio.widget.redpack

import android.animation.Animator
import android.graphics.Path
import android.taobao.windvane.util.DPUtil
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import com.fengwo.module_comment.ext.gone
import com.fengwo.module_comment.ext.log
import com.fengwo.module_comment.ext.show
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.PacketCountBean
import com.opensource.svgaplayer.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList

/**
 * File FallingAdapter.kt
 * Date 1/8/21
 * Author lucas
 * Introduction 红包雨适配器
 */
class FallingAdapter : FallingView.IFallingAdapter<Int>(R.layout.item_redpack) {
    private val random = Random()
    private val animDuration = 6000L//物件动画时长
    private val count = 50//一屏显示物件的个数
    private val maxGiveCount = 3//最多可领取数量
    private var currentGiveCount = 0//当前已领取数量
    private val buriedPointCount = 10//埋点数量
    var clickIconCount = 0//点击红包个数
    private val buriedPoints = ConcurrentLinkedQueue<Int>()
    private val animInterval = ArrayList<Interval>()
    var onClickBuried: (PacketCountBean) -> Unit = {}//击中埋点回调
    var packetBean: PacketCountBean? = null

    fun setData(data: List<Int>) {
        datas = data
        //初始化埋点
        currentGiveCount = 0
        clickIconCount = 0
        buriedPoints.clear()
        val list = List(data.size) { it }.toMutableList()
        for (i in 0 until buriedPointCount) {
            val point = list[random.nextInt(list.size)]
            buriedPoints.add(point)
            list.remove(point)
        }
//        "埋点：${buriedPoints}".log()
    }

    private fun createPath(parent: ViewGroup, position: Int, view: View): Path =
            Path().apply {
                view.measure(0, 0)
                val width = parent.width - view.measuredWidth
                val height = parent.height
                if (width <= 0 || height <= 0) return@apply
                val swing = width / 3f//x轴摆动范围
                //限制动画区间使物件分布均匀
                if (animInterval.isEmpty()) {
                    animInterval.add(Interval(view.measuredWidth / 2f, swing))
                    animInterval.add(Interval(swing, swing * 2))
                    animInterval.add(Interval(swing * 2, parent.width - view.measuredWidth / 2f))
                }
//            "animInterval:${animInterval.size}".p()
                val interval: Interval
                if (animInterval.size == 1) {
                    interval = animInterval[0]
                } else {
                    interval = animInterval[random.nextInt(animInterval.size)]
                }
                animInterval.remove(interval)
                val startPointX = random.nextInt(width).toFloat()
                moveTo(startPointX, -view.measuredHeight.toFloat())

                //控制点
                val point1X = random.nextInt(interval.getLength().toInt()) + interval.start
                val point1Y = random.nextInt(height / 2).toFloat()

                val point2X = random.nextInt(interval.getLength().toInt()) + interval.start
                val point2Y = random.nextInt(height / 2).toFloat() + height / 2

                val point3X = random.nextInt(interval.getLength().toInt()) + interval.start

                cubicTo(point1X, point1Y, point2X, point2Y, point3X, height.toFloat())
            }


    override fun convert(parent: ViewGroup, holder: FallingView.Holder) {
        val svgaImageView = holder.view.findViewById<SVGAImageView>(R.id.v_img)
        if (holder.position % 2 == 0) {
            svgaImageView.setImageResource(R.drawable.ic_redpack1)
        } else {
            svgaImageView.setImageResource(R.drawable.ic_redpack2)
        }
        holder.config.startTime = holder.position * (animDuration / count)
        holder.view.show()
        holder.view.setOnClickListener {
            clickIconCount++
            //领取红包
            if (buriedPoints.contains(holder.position) && packetBean != null && currentGiveCount < maxGiveCount) {
//                "holder.position:${holder.position}".log()
                buriedPoints.remove(holder.position)
                onClickBuried.invoke(packetBean!!)
                currentGiveCount++
            }
            //播放领取动画
            playGoneAnim(svgaImageView, holder)
        }
    }

    private fun playGoneAnim(svgaImageView: SVGAImageView, holder: FallingView.Holder) {
        holder.config.anim?.pause()
        val dip2px = DPUtil.dip2px(60f)
        svgaImageView.layoutParams = FrameLayout.LayoutParams(dip2px, dip2px)
        SVGAParser(svgaImageView.context).decodeFromAssets("smoke.svga", object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                svgaImageView.setImageDrawable(SVGADrawable(videoItem))
                svgaImageView.callback = object : SVGACallback {
                    override fun onFinished() {
                        holder.config.anim?.end()
                        holder.view.gone()
                        svgaImageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                    }

                    override fun onPause() {
                    }

                    override fun onRepeat() {
                    }

                    override fun onStep(frame: Int, percentage: Double) {
                    }
                }
                svgaImageView.startAnimation()
            }

            override fun onError() {
                holder.config.anim?.end()
                holder.view.gone()
            }
        })
    }

    override fun convertAnim(parent: ViewGroup, holder: FallingView.Holder): Animator {
        val path = createPath(parent, holder.position, holder.view)
        holder.config.path = path
        //旋转方向
        val rotation: Float
        if (random.nextInt(2) == 0) {
            rotation = 30f * random.nextFloat()
        } else {
            rotation = -30f * random.nextFloat()
        }
        val duration = (animDuration * (0.6 + random.nextInt(4) * 0.1)).toLong()
        return RedPackAnim(path, rotation, holder.view, duration)
    }

    //区间
    class Interval(val start: Float, val end: Float) {
        fun getLength() = end - start
    }
}