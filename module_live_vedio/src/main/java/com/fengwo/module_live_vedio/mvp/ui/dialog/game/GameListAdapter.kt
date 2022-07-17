package com.fengwo.module_live_vedio.mvp.ui.dialog.game

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.GameBean

class GameListAdapter : BaseQuickAdapter<GameBean, BaseViewHolder>(R.layout.item_game) {
    override fun convert(helper: BaseViewHolder, item: GameBean) {
        ImageLoader.loadImg(helper.getView(R.id.v_icon),item.gameIcon)
        helper.setText(R.id.v_name,item.gameName)
    }
}