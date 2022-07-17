package com.fengwo.module_live_vedio.mvp.ui.adapter

import android.view.ViewGroup.MarginLayoutParams
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.AgentInviteDto

/**
 * @anchor Administrator
 * @date 2021/1/15
 */
class AgentTopAdapter: BaseQuickAdapter<AgentInviteDto, BaseViewHolder>(R.layout.layout_agent_invite) {


    override fun convert(helper: BaseViewHolder, item: AgentInviteDto?) {
        if (helper.adapterPosition == 0) {
            val lp = helper.itemView.layoutParams as MarginLayoutParams
            lp.leftMargin = 0
            helper.itemView.layoutParams = lp
        }
        ImageLoader.loadImg(helper.getView(R.id.ivMember), item!!.userHeadImg)
    }
}