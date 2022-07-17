package com.fengwo.module_live_vedio.mvp.ui.adapter.redpackage

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.DataFormatUtils
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackagePerson
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 红包人数收益列表
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
class RedPackagePersonAdapter() : BaseQuickAdapter<RedPackagePerson.Item, BaseViewHolder>(R.layout.item_red_package_person) {
    override fun convert(helper: BaseViewHolder, item: RedPackagePerson.Item?) {
        item?.let {
            val imgHead = helper.getView<CircleImageView>(R.id.img_head)
            ImageLoader.loadImg(imgHead, it.headImg, R.drawable.default_head)
            helper.setText(R.id.tv_name, it.nickname ?: "未知")
            helper.setText(R.id.tv_date, it.createDatetime ?: "")
            val money: String? = DataFormatUtils.formatNumbers(it.amount ?: 0)
            helper.setText(R.id.tv_money, money ?: "")
            helper.setVisible(R.id.img_best, it.isMost == true)
        }

    }
}