package com.fengwo.module_live_vedio.mvp.dto.redpackage

/**
 * 收益名单
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
data class RedPackagePerson(
        val nickname: String?,//昵称
        val headImg: String?,//头像
        val receiveAmount: Int?,//已领取花钻
        val receiveNum: Int?,//已领取数量
        val redpacketType: Int?,//0普通，1拼手气
        val totalAmount: Int?,//总花钻
        val totalNum: Int?,//红包总数量
        val itemList:List<Item>?//红包领取详情
) {
    data class Item(val amount: Int?,//花钻数量
                    val createDatetime: String?,//领取时间
                    val headImg: String?,//头像
                    val isMost: Boolean?,//是否是锦鲤
                    val nickname: String?//昵称
    )
}