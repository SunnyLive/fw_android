package com.fengwo.module_login.mvp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_login.R
import com.fengwo.module_login.mvp.dto.MineImageDto
import de.hdodenhof.circleimageview.CircleImageView

class MineImageAdapter(context: Context) : BaseQuickAdapter<MineImageDto,BaseViewHolder>(R.layout.layout_images) {



    init {
        val mEmptyView: View = LayoutInflater.from(context).inflate(R.layout.layout_mine_item_empty, null, true)
        emptyView = mEmptyView
    }

    var isAnchor:Boolean = true

    var isHeader: Boolean = false

    override fun convert(helper: BaseViewHolder, item: MineImageDto?) {
        val countView : TextView = helper.getView(R.id.tv_number)
        val header: CircleImageView = helper.getView(R.id.cv_header_image)
        val photoFrame: View = helper.getView(R.id.fl_is_living)
        val living : ImageView = helper.getView(R.id.iv_living)
        val image : ImageView = helper.getView(R.id.iv_gift_image)
        item?.mGuard?.apply {
            countView.visibility = View.VISIBLE
            countView.text = "$quantity"
        }
        item?.apply {
            if (isHeader) {
                header.borderColor = if (isAnchor) ContextCompat.getColor(mContext,R.color.red_FE3C9C)
                else ContextCompat.getColor(mContext,R.color.color_ffc147)
                photoFrame.isEnabled = isAnchor
                header.visibility = View.VISIBLE
                ImageLoader.loadImg(header,imageUrl)
                if (isLiving) {
                    ImageLoader.loadGif(living, R.drawable.ic_anchor_living)
                    photoFrame.visibility = View.VISIBLE
                }else{
                    photoFrame.visibility = View.GONE
                }
            }else{
                image.visibility = View.VISIBLE
                ImageLoader.loadImg(image,imageUrl)
            }
        }


    }
}