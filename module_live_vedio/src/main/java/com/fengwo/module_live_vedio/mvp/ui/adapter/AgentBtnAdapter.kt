package com.fengwo.module_live_vedio.mvp.ui.adapter

import android.content.res.Resources
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_comment.utils.TimeUtils
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.AgentInviteDto

/**
 * @anchor Administrator
 * @date 2021/1/15
 */
class AgentBtnAdapter: BaseQuickAdapter<AgentInviteDto, BaseViewHolder>(R.layout.item_agent_list) {
    

    override fun convert(helper: BaseViewHolder, item: AgentInviteDto?) {
        val position = helper.adapterPosition
        if (position < 3) {
            helper.setVisible(R.id.tv_position, false)
            helper.setVisible(R.id.iv_position, true)
            val res = ImageLoader.getResId("ic_agent" + (position + 1), R.drawable::class.java)
            try {
                helper.setImageResource(R.id.iv_position, res)
            } catch (e: Resources.NotFoundException) {
            }
        } else {
            helper.setVisible(R.id.tv_position, true)
            helper.setVisible(R.id.iv_position, false)
            var index = position+1;
            helper.setText(R.id.tv_position, index.toString() )
        }
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item!!.userHeadImg)
        helper.setText(R.id.tv_name, item!!.userNickname)
        helper.setText(R.id.tv_time, "入团：" + TimeUtils.dealInstanToYYYYMMDDWithPoint(item!!.createTime))
    }
}