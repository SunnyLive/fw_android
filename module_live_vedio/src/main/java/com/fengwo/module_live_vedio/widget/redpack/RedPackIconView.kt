package com.fengwo.module_live_vedio.widget.redpack

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.fengwo.module_comment.base.HttpResult
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.bean.Param
import com.fengwo.module_comment.ext.gone
import com.fengwo.module_comment.ext.log
import com.fengwo.module_comment.ext.show
import com.fengwo.module_comment.ext.startRotateAnim
import com.fengwo.module_comment.utils.RetrofitUtils
import com.fengwo.module_comment.utils.RxUtils
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.api.LiveApiService
import com.fengwo.module_live_vedio.api.param.GivingPacketParam
import com.fengwo.module_live_vedio.api.param.PacketCountParam
import com.fengwo.module_live_vedio.api.param.RedPacketClickParam
import com.fengwo.module_live_vedio.mvp.dto.PacketCountBean
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_red_pack_icon.view.*
import java.util.concurrent.TimeUnit

/**
 * File RedPackIconView.kt
 * Date 1/19/21
 * Author lucas
 * Introduction 红包图标
 */
class RedPackIconView : FrameLayout {
    companion object {
        const val GLOBAL_COUNTDOWN_TIME = 30_000//倒计时时间
    }

    private var roomId = 0
    private val compositeDisposable = CompositeDisposable()
    private val packetList = ArrayList<PacketCountBean>()
    private var subscribeCountDown: Disposable? = null
    var onPlayFalling: ((PacketCountBean) -> Unit)? = null
//    var isPlayGlobalPacket = false//是否在

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
        addView(LayoutInflater.from(context).inflate(R.layout.view_red_pack_icon, this, false))
        setOnClickListener {
            val globalPacket = getGlobalPacket()
            if (globalPacket == null && packetList.isNotEmpty()) {//如果没有全服红包
                val packet = packetList.removeAt(0)
                redPacketClick(packet.serialNo)
                onPlayFalling?.invoke(packet)
            }
        }
    }

    //绑定直播间
    fun attachRoom(roomId: Int) {
        this.roomId = roomId
        //初始化
        refreshPackList()
    }

    fun refreshView(data: List<PacketCountBean>) {
        packetList.clear()
        packetList.addAll(data)
        if (packetList.isEmpty()) {
            clearAnimation()
            post { gone() }
        } else {
            val globalPacket = getGlobalPacket()
            if (globalPacket != null) {//全栈红包
                clearAnimation()
                v_redpack_icon_bg.setImageResource(R.drawable.ic_global_icon)
                v_redpack_count.gone()
                v_redpack_countdonw.show()
                if (getResidueTime(globalPacket) < 0) {//倒计时已结束
                    if (getResidueTime(globalPacket) + 10_000 > 0) {//正在播放红包雨
                        onPlayFalling?.invoke(globalPacket)
                    }
                    post { gone() }
                    return
                }
                //倒计时
                countDown(globalPacket)
            } else {//普通红包
                v_redpack_icon_bg.setImageResource(R.drawable.ic_un_send_redpack)
                v_redpack_count.show()
                v_redpack_countdonw.gone()
                startRotateAnim()
            }
            show()
            v_redpack_count.text = if (packetList.size > 99) "99+" else packetList.size.toString()
        }
    }

    private fun countDown(globalPacket: PacketCountBean) {
        if (subscribeCountDown != null && !subscribeCountDown!!.isDisposed) return
        val countTime = getResidueTime(globalPacket) / 1000
//        ("countTime:" + (globalPacket.currentTimestamp - globalPacket.createDatetimestamp)).log()
        subscribeCountDown = Flowable
                .interval(0, 1, TimeUnit.SECONDS)
                .take(countTime + 1)
                .compose(RxUtils.applySchedulers())
                .map { along: Long -> countTime - along }
                .subscribe(
                        { num: Long -> v_redpack_countdonw.text = num.toString().plus("s") },
                        { throwable: Throwable? -> }
                ) {
                    clearAnimation()
                    post { gone() }
                    //倒计时结束播放红包雨
                    onPlayFalling?.invoke(globalPacket)
                }
        subscribeCountDown?.let { compositeDisposable.add(it) }
    }

    //获取剩余时间
    fun getResidueTime(globalPacket: PacketCountBean) =
            (GLOBAL_COUNTDOWN_TIME - (globalPacket.currentTimestamp - globalPacket.createDatetimestamp))

    //获取全服红包
    fun getGlobalPacket(): PacketCountBean? {
        return packetList.find { isGlobalPacket(it) }
    }

    fun isGlobalPacket(it: PacketCountBean) =
            it.redpacketChannel == 0

    //红包拆包
    fun redPacketClick(serialNo: String) {
        compositeDisposable.add(RetrofitUtils()
                .createApi(LiveApiService::class.java)
                .redPacketClick(RedPacketClickParam(serialNo))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(object : LoadingObserver<HttpResult<*>>() {
                    override fun _onNext(data: HttpResult<*>) {
                        if (data.isSuccess){
                            refreshPackList()
                        }
                    }

                    override fun _onError(msg: String?) {
                    }
                }))
    }

    //刷新红包列表
    fun refreshPackList() {
        if (roomId <= 0) return
        compositeDisposable.add(RetrofitUtils()
                .createWenboApi(LiveApiService::class.java)
                .getPacketCount(PacketCountParam(roomId.toString()))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(object : LoadingObserver<HttpResult<List<PacketCountBean>>>() {
                    override fun _onNext(data: HttpResult<List<PacketCountBean>>) {
                        if (data.isSuccess)
                            refreshView(data.data)
                    }

                    override fun _onError(msg: String?) {
                    }
                }))
    }

    //领取红包
    fun givingPacket(serialNo: String) {
        if (roomId <= 0) return
        compositeDisposable.add(RetrofitUtils()
                .createWenboApi(LiveApiService::class.java)
                .givingPacket(Param(GivingPacketParam(roomId, serialNo)))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(object : LoadingObserver<HttpResult<*>>() {
                    override fun _onNext(data: HttpResult<*>) {
                        refreshPackList()
                    }

                    override fun _onError(msg: String?) {
                    }
                }))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    /**
     * 红包数量
     *
     * @return
     */
    fun redPacketCount(): Int {
        return packetList.size
    }

}