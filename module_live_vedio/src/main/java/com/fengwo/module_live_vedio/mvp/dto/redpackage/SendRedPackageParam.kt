package com.fengwo.module_live_vedio.mvp.dto.redpackage

/**
 * 发送红包参数
 *
 * @Author gukaihong
 * @Time 2021/1/6
 */
data class SendRedPackageParam(var redPacketNum: Long,//红包数量
                               var redpacketChannel: Int,//发送渠道：0-全服红包，1-本房红包
                               var redpacketType: Int,//红包类型：0普通，1拼手气
                               var refAnchorUserId: Int,//主播id - 红包方式为本房红包时必填
                               var totalAmount: Long//总花钻金额
) {
}