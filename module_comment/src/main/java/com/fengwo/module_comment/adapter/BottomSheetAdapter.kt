package com.fengwo.module_comment.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.R

/**
 * BottomSheet弹出
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
class BottomSheetAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_bottom_sheet_dialog) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setText(R.id.tv_name, item)
        helper.addOnClickListener(R.id.tv_name)
    }
}