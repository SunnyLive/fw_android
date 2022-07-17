package com.fengwo.module_comment.pop

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.fengwo.module_comment.ArouterApi
import com.fengwo.module_comment.R
import com.fengwo.module_comment.event.ActivityWishMessage
import com.fengwo.module_comment.utils.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.pop_act_wish_push.view.*
import razerdp.basepopup.BasePopupWindow

/** 给主播许愿弹框
 * @author GuiLianL
 * @intro
 * @date 2020/6/14
 */
class ActPushWishPop(context: Context?) : BasePopupWindow(context) {
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_act_wish_push)
    }

    init {
        popupGravity = Gravity.CENTER
        contentView.iv_close.setOnClickListener {
            dismiss()
        }
    }

    fun showDialog(bean: ActivityWishMessage) {
        ImageLoader.loadImg(findViewById<CircleImageView>(R.id.ivMember),bean.userHeadImg)
        findViewById<TextView>(R.id.tv_name).text = bean.userNickname
        findViewById<TextView>(R.id.tv_context).text = bean.wishContent
//        findViewById<TextView>(R.id.v_wish_title).text = "对${bean.anchorNickname}许下心愿"
        findViewById<View>(R.id.v_view_wish).setOnClickListener {
            ARouter.getInstance().build(ArouterApi.WISHING_WALL_ACTION).navigation()
        }
        showPopupWindow()
    }
}