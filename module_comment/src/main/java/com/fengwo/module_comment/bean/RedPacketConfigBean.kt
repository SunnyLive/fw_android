package com.fengwo.module_comment.bean

/**
 * 红包配置信息
 *
 * @Author gukaihong
 * @Time 2021/1/7
 */
data class RedPacketConfigBean(
        //本房间普通红包单包最低花钻数
        val REDPACKET_ROOM_NORMAL_AMOUNT: String?,
        //本房间红包最少个数
        val REDPACKET_ROOM_NUM: String?,
        //本房间拼手气红包总花钻最低数量
        val REDPACKET_ROOM_RANDOM_AMOUNT: String?,
        //本房间红包飘屏阈值
        val REDPACKET_ROOM_GLOBAL_AMOUNT: String?,
        //本房间红包最多个数
        val REDPACKET_ROOM_MAX_NUM: String?,
        //全站普通红包单包最低花钻数
        val REDPACKET_STATION_NORMAL_AMOUNT: String?,
        //全站红包最少个数
        val REDPACKET_STATION_NUM: String?,
        //全站拼手气红包总花钻最低数量
        val REDPACKET_STATION_RANDOM_AMOUNT: String?,
        //全站红包飘屏阈值
        val REDPACKET_STATION_GLOBAL_AMOUNT: String?,
        //全站红包最多个数
        val REDPACKET_STATION_MAX_NUM: String?
) {
}