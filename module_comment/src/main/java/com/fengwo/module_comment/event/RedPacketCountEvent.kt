package com.fengwo.module_comment.event

/**
 * 红包数量
 *
 * @Author gukaihong
 * @Time 2021/1/26
 */
data class RedPacketCountEvent(
        val channelId: Int?,//直播间id
        val count: Int?,//红包个数
        var positionRefresh: Int = -1//需要刷新的位置，-1 为不刷新
) {
    //是否有红包
    fun isHasRedPacket() = count ?: 0 > 0
}