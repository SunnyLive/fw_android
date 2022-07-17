package com.fengwo.module_live_vedio.mvp.ui.adapter

import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.DataFormatUtils
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.RankZhuboDto
import kotlinx.android.synthetic.main.live_item_ranktop.view.*

class ZhuboRankAdapter : BaseQuickAdapter<RankZhuboDto, BaseViewHolder>(R.layout.live_item_ranktop) {


    var isChooseViewState: Boolean = false

    private var mAttentionChecked: ((Int, Boolean) -> Unit)? = null

    fun setAttentionListener(attentionChecked: (Int, Boolean) -> Unit) {
        mAttentionChecked = attentionChecked
    }


    override fun convert(helper: BaseViewHolder, item: RankZhuboDto?) {
        val position: Int = helper.adapterPosition
        val ranking = position + 1
        item?.apply {
            helper.itemView.let {

                // 这个是排名标志
                it.iv_ranking.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                it.iv_ranking.text = ""
                val mRankingResId = ImageLoader.getResId("icon_ranking$ranking", R.drawable::class.java)
                // 这个是排名的相框
                val mPhotoFrameResId = ImageLoader.getResId("icon_photo_frame_ranking$ranking", R.drawable::class.java)
                it.iv_photo_frame.background = ContextCompat.getDrawable(mContext,R.color.transparent)
                if (mRankingResId != -1) {
                    it.iv_ranking.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, mRankingResId), null, null, null)
                    it.iv_photo_frame.background = ContextCompat.getDrawable(mContext, mPhotoFrameResId)
                    it.civ_header.borderWidth = 0
                }else{
                    it.civ_header.borderColor = ContextCompat.getColor(mContext, R.color.red_FE3C9C)
                    it.civ_header.borderWidth = mContext.resources.getDimension(R.dimen.dp_1).toInt()
                    it.iv_ranking.text = "$ranking"
                }

                if (liveStatus == 2) {
                    it.im_dw.setImageResource(R.drawable.pic_dw_one)
                }else{
                    it.im_dw.setImageResource(R.color.transparent)
                }

                //主播等级
                if (0 < anchorLevel){
                    it.iv_level_anchor.setImageResource(ImageLoader.getResId("login_ic_type3_v${anchorLevel}",R.drawable::class.java))
                }

                //名字
                it.tv_rank_name.text = nickname
                //用户等级
                val levelRes = ImageLoader.getResId("login_ic_v$userLevel", R.drawable::class.java)
                if (levelRes != -1) {
                    it.iv_level.setImageResource(levelRes)
                }
                //是否关注
                it.cb_attention.apply {
                    isSelected = isAttension == 1
                    text = if (isSelected) "已关注" else "关注"
                    visibility = if (!isChooseViewState) View.VISIBLE else View.GONE
                    setOnClickListener { _ ->
                        mAttentionChecked?.let { it -> it(position, isSelected) }
                    }
                }

                //头像
                ImageLoader.loadImg(it.civ_header, item.headImg)
                //花蜜值
                it.tv_nectar_value.text = DataFormatUtils.formatNumbers(score)
                //花蜜值的title
                it.tv_nectar_label.apply {
                    val mDrawableRecord = ContextCompat.getDrawable(context, R.drawable.icon_record)
                    if (isChooseViewState) {
                        text = ""
                        setCompoundDrawablesWithIntrinsicBounds(mDrawableRecord, null, null, null)
                    } else text = "花蜜值"
                }
            }
            helper.addOnClickListener(R.id.root)
        }
    }


}