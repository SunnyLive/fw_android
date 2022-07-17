package com.fengwo.module_live_vedio.mvp.ui.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.DataFormatUtils
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.TeenagerVideoDto

/**
 * 青少年视频列表适配器
 *
 * @Author gukaihong
 * @Time 2020/12/9
 */
class TeenagerVideoListAdapter() : BaseQuickAdapter<TeenagerVideoDto.Records, BaseViewHolder>(R.layout.item_teenager_mode_video) {

    override fun convert(helper: BaseViewHolder, item: TeenagerVideoDto.Records?) {
        //设置gif
        //ImageLoader.loadGif(helper.getView(R.id.img_gif), R.drawable.live_cell_gif)
        item?.let {
            //设置封面
            ImageLoader.loadImg(helper.getView(R.id.img_poster), it.videoThumb)
            //设置标题
            helper.setText(R.id.tv_title, it.videoTitle)
            //设置标签
            //helper.setText(R.id.tv_view_count, DataFormatUtils.formatNumbersChinaUnit(it.viewCount))
        }
        helper.addOnClickListener(R.id.card_poster)
    }
}