package com.fengwo.module_live_vedio.mvp.ui.pop

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.H5addressBean
import com.fengwo.module_live_vedio.mvp.ui.activity.BrowserActActivity
import kotlinx.android.synthetic.main.pop_act_tips_newyear.view.*
import razerdp.basepopup.BasePopupWindow

/** ActTransformationPop
 * @author GuiLianL
 * @intro
 * @date 2020/6/14
 */
class ActTipsPopnewyear(context: Context?, data: H5addressBean) : BasePopupWindow(context) {
    private val url: String


    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_act_tips_newyear)
    }

    init {
        url = data.addressLink
        popupGravity = Gravity.CENTER

        // ImageLoader.loadRouteImg(getContentView().im_pic, data.homePageActivity)
        getContentView().iv_close.setOnClickListener { dismiss() }
        ImageLoader.loadImg(contentView.im_old, data.exchangeActivityPropInfo.prevPropIcon)
        contentView.tv_old.text = data.exchangeActivityPropInfo.prevPropName
        contentView.tv_old_sum.text = "x"+data.exchangeActivityPropInfo.prevPropQuantity.toString()

        ImageLoader.loadImg(contentView.im_new, data.exchangeActivityPropInfo.propIcon)
        contentView.tv_new.text = data.exchangeActivityPropInfo.propName
        contentView.tv_new_sum.text = "x"+data.exchangeActivityPropInfo.propQuantity.toString()
        contentView.tv_zdl.setOnClickListener { dismiss() }
//        getContentView().im_pic.setOnClickListener {
//            dismiss()
//            if (!TextUtils.isEmpty(url)) BrowserActActivity.start(context, data.title, url)
//        }
    }
}