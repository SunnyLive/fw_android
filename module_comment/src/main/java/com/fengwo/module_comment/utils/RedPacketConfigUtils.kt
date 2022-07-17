package com.fengwo.module_comment.utils

import android.annotation.SuppressLint
import com.fengwo.module_comment.api.CommentService
import com.fengwo.module_comment.bean.RedPacketConfigBean
import io.reactivex.schedulers.Schedulers

/**
 * 红包配置
 *
 * @Author gukaihong
 * @Time 2021/1/7
 */
class RedPacketConfigUtils private constructor() {

    private val service = RetrofitUtils().createApi(CommentService::class.java)
    private var configData: RedPacketConfigBean? = null
    var isOfficialAccount: Boolean = false

    private object Holder {
        val holder = RedPacketConfigUtils()
    }


    companion object {
        val instance = Holder.holder
    }

    /**
     * 获取配置信息
     *
     */
    @SuppressLint("CheckResult")
    fun initConfigData() {
        service.redPacketConfig
                .subscribeOn(Schedulers.io())
                .subscribe {
                    it?.let {
                        if (it.isSuccess) {
                            configData = it.data
                        }
                    }
                }
        service.checkOfficialAccount()
                .subscribeOn(Schedulers.io())
                .subscribe {
                    it?.let {
                        isOfficialAccount = it.data
                    }
                }
    }

    /**
     * 校验数据
     *
     */
    private fun checkData() {
        if (configData == null) {
            initConfigData()
        }
    }

    /**
     * 本房间普通红包单包最低花钻数
     *
     * @return
     */
    fun getRedPacketRoomNormalAmount(): Int {
        checkData()
        return configData?.REDPACKET_ROOM_NORMAL_AMOUNT?.toIntOrNull() ?: 500
    }

    /**
     * 本房间红包最少个数
     *
     * @return
     */
    fun getRedPacketRoomNum(): Int {
        checkData()
        return configData?.REDPACKET_ROOM_NUM?.toIntOrNull() ?: 20
    }

    /**
     * 本房间拼手气红包总花钻最低数量
     *
     * @return
     */
    fun getRedPacketRoomRandomAmount(): Int {
        checkData()
        return configData?.REDPACKET_ROOM_RANDOM_AMOUNT?.toIntOrNull() ?: 10000
    }

    /**
     * 本房间红包飘屏阈值
     *
     * @return
     */
    fun getRedPacketRoomGlobalAmount(): Int {
        checkData()
        return configData?.REDPACKET_ROOM_GLOBAL_AMOUNT?.toIntOrNull() ?: 10000
    }


    /**
     * 全站普通红包单包最低花钻数
     *
     * @return
     */
    fun getRedPacketStationNormalAmount(): Int {
        checkData()
        return configData?.REDPACKET_STATION_NORMAL_AMOUNT?.toIntOrNull() ?: 500
    }

    /**
     * 全站普通红包单包最低花钻数
     *
     * @return
     */
    fun getRedPacketStationNum(): Int {
        checkData()
        return configData?.REDPACKET_STATION_NUM?.toIntOrNull() ?: 20
    }

    /**
     * 全站拼手气红包总花钻最低数量
     *
     * @return
     */
    fun getRedPacketStationRandomAmount(): Int {
        checkData()
        return configData?.REDPACKET_STATION_RANDOM_AMOUNT?.toIntOrNull() ?: 10000
    }

    /**
     * 全站红包飘屏阈值
     *
     * @return
     */
    fun getRedPacketStationGlobalAmount(): Int {
        checkData()
        return configData?.REDPACKET_STATION_GLOBAL_AMOUNT?.toIntOrNull() ?: 10000
    }

    /**
     * 本房间红包最多个数
     *
     * @return
     */
    fun getRedpacketRoomMaxNum(): Int {
        checkData()
        return configData?.REDPACKET_ROOM_MAX_NUM?.toIntOrNull() ?: 200
    }

    /**
     * 全站红包最多个数
     *
     * @return
     */
    fun getRedpacketStationMaxNum(): Int {
        checkData()
        return configData?.REDPACKET_STATION_MAX_NUM?.toIntOrNull() ?: 200
    }
}