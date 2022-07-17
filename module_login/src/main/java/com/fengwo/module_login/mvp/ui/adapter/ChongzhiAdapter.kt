package com.fengwo.module_login.mvp.ui.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_login.R
import com.fengwo.module_login.mvp.dto.RechargeDto
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class ChongzhiAdapter(data: List<RechargeDto>) : BaseQuickAdapter<RechargeDto, BaseViewHolder>(R.layout.login_item_chongzhi, data) {
    @JvmField
    var checkPosition = 0
    private val df = DecimalFormat("###.##")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val root = holder.getView<View>(R.id.bg)
        if (position == checkPosition) {
            root.setBackgroundResource(R.drawable.bg_recharge_ture)
        } else {
            root.setBackgroundResource(R.drawable.bg_recharge_false)
        }
        //        root.setOnClickListener(v -> {
//            checkPosition = position;
//            notifyDataSetChanged();
//        });
        holder.addOnClickListener(R.id.bg)
    }

    val selected: RechargeDto
        get() = mData[checkPosition]

    override fun convert(helper: BaseViewHolder, item: RechargeDto) {
        helper.setText(R.id.tv_huazuan, formatDiamond(item.diamondNum + item.diamondGive) + "")
        helper.setText(R.id.tv_price, item.price + "元")
    }

    private fun formatDiamond(diamond: Int): String {
        return if (diamond < 10000 ) diamond.toString() else try {
            val divide = BigDecimal(diamond)
                    .divide(BigDecimal(10000), 2, RoundingMode.HALF_UP)
            df.format(divide) + "W"
        } catch (e: Exception) {
            diamond.toString()
        }
    }
}