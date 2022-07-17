package com.fengwo.module_live_vedio.mvp.ui.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import com.fengwo.module_comment.utils.RxBus
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent
import kotlinx.android.synthetic.main.live_pop_room_more.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
class LiveRoomMorewindow(private val context: Context) : BasePopupWindow(context) {
    private var isAnimate = true
    private fun initData() {
        getContentView().im_sx.setOnClickListener{
            onItemClickListener?.onItemClick(0)
            dismiss()
        }
        getContentView().im_fx.setOnClickListener{
            onItemClickListener?.onItemClick(1)
            dismiss()
        }
        getContentView().im_dh.setOnClickListener{
            isAnimate = !isAnimate
            if (isAnimate) {
                getContentView().im_dh.setImageResource(R.drawable.pic_dh_clone)
                getContentView(). tv_dh.text="关闭动画"
            } else {
                getContentView().im_dh.setImageResource(R.drawable.pic_dh)
                getContentView(). tv_dh.text="开启动画"
            }
            RxBus.get().post(ChangeAnimateEvent(isAnimate))
        }

    }
    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 300)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, 1f, 300)
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.live_pop_room_more)
    }
    public fun setSixin(type : Int){
        if(type==1){
            getContentView().im_sx.visibility = View.VISIBLE
            getContentView().tv_sx.visibility = View.VISIBLE

        }else{
            getContentView().im_sx.visibility = View.GONE
            getContentView().tv_sx.visibility = View.GONE
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    init {
        popupGravity = Gravity.BOTTOM
        initData()
    }



}