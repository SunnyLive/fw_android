package com.fengwo.module_login.mvp.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_login.R
import com.fengwo.module_comment.bean.BackpackDto

class BackpackAdapter : BaseQuickAdapter<BackpackDto.GiftsBean,BaseViewHolder>(R.layout.layout_item_backpack) {

     var isMore:Boolean = false
        set(value) {
            field = value
        }

    override fun getItemCount(): Int {
        return if (isMore) {
            super.getItemCount()
        }else{
            if (8 > super.getItemCount()) super.getItemCount() else 8
        }
    }


    override fun convert(helper: BaseViewHolder, item: BackpackDto.GiftsBean?) {
        item?.apply {
            ImageLoader.loadImg(helper.getView(R.id.iv_gift_image),goodsIcon)
            helper.getView<TextView>(R.id.tv_gift_name).text = "$goodsName*$goodsCount"
            helper.getView<TextView>(R.id.tv_gift_time).text = "coming soon"
        }
    }
}