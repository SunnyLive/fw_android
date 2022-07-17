package com.fengwo.module_live_vedio.mvp.ui.adapter.redpackage

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackageOrderData
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.TYPE_COMMON

/**
 * 红包记录
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
class RedPackageOrderAdapter : BaseQuickAdapter<RedPackageOrderData.Record, BaseViewHolder>(R.layout.item_red_package_order_list) {
    override fun convert(helper: BaseViewHolder, item: RedPackageOrderData.Record?) {
        item?.let {
            if (!it.nickname.isNullOrEmpty()) {
                //收益
                helper.setText(R.id.tv_name, it.nickname)
                helper.setText(R.id.tv_money, it.amount?.toString() ?: "")
                helper.setText(R.id.tv_other, "")
            } else if (!it.redpacketType.isNullOrEmpty()) {
                //发出
                helper.setText(R.id.tv_name, if (it.redpacketType.toIntOrNull() ?: TYPE_COMMON == TYPE_COMMON) "普通红包" else "拼手气红包")
                helper.setText(R.id.tv_money, it.totalAmount?.toString() ?: "")
                helper.setText(R.id.tv_other, "${it.receiveNum}/${it.redPacketNum}个")
            }
            helper.setText(R.id.tv_date, it.createDatetime ?: "")
        }
    }
}