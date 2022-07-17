package com.fengwo.module_live_vedio.mvp.dto.redpackage

/**
 * 红包记录数据
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
data class RedPackageOrderData(
        val headImg: String?,//领红包人头像
        val nickname: String?,//领红包人昵称
        val totalAmount: Long?,//累计获取花钻 or 累计送出花钻
        val totalNum: Int?,//红包总数
        val itemPage: ItemPage?//列表

) {
    data class ItemPage(
            val current: Long?,
            val pages: Long?,
            val size: Long?,
            val total: Long?,
            val records: List<Record>?//记录列表
    )

    data class Record(
            val amount: Int?,//领取花钻数量
            val createDatetime: String?,//领取时间 or 发送日期
            val nickname: String?,//发红包人昵称
            val receiveNum:Int?,//领取数量
            val redPacketNum:Int?,//红包数量
            val redpacketType:String?,//红包类型
            val totalAmount:Int?//花钻数量
    )
}