package com.fengwo.module_login.mvp.ui.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ScreenUtils
import com.fengwo.module_login.R
import com.fengwo.module_login.mvp.dto.MineServerDto

class MineServerAdapter : BaseQuickAdapter<MineServerDto, BaseViewHolder>(R.layout.layout_server) {


    override fun convert(helper: BaseViewHolder, item: MineServerDto?) {
        item?.apply {
            val server = helper.getView<TextView>(R.id.tv_item_server)
            val dp30 = mContext.resources.getDimension(R.dimen.dp_30).toInt()
            server.layoutParams.width = (ScreenUtils.getScreenWidth(mContext) - dp30) / 4
            server.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, image), null, null)
            server.text = title
            server.setOnClickListener { view ->
                ol?.onClick(view)
            }
        }
    }
}