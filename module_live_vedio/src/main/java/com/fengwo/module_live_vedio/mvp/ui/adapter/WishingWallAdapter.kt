package com.fengwo.module_live_vedio.mvp.ui.adapter

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_comment.utils.KLog
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.WishingWallDto
import kotlinx.android.synthetic.main.layout_wishing_wall_item.view.*

class WishingWallAdapter : BaseQuickAdapter<WishingWallDto.HourProcessesBean, BaseViewHolder>(R.layout.layout_wishing_wall_item) {

    private var mWishOnClickListener: ((WishingWallDto.HourProcessesBean, View, Int) -> Unit)? = null


    fun setWishOnClickListener(listener: (WishingWallDto.HourProcessesBean, View, Int) -> Unit) {
        this.mWishOnClickListener = listener
    }

    private var offset = 0f

    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: WishingWallDto.HourProcessesBean?) {
        item?.apply {
            helper.itemView.let {
                ImageLoader.loadImg(it.cv_anchor_header, anchorHeadImg)
                ImageLoader.loadImg(it.cv_fans_header, userHeadImg)
                ImageLoader.loadImg(it.iv_wish_gift, icon)
                it.tv_anchor_name.text = anchorName
                it.tv_fans_name.text = userName
                it.tv_wish_gift_name.text = "x$integral"
                chooseStyle(it,false)
                if (TextUtils.isEmpty(content)) {
                    val adapter = WishDateAdapter(mContext)
                    it.rv_wish_result_date.adapter = adapter
                    it.rv_wish_result_date.layoutManager = LinearLayoutManager(mContext)
                    pairingHeader(it,false)
                    it.ll_wish_fans_blessing.alpha = 0f
                    it.iv_wish_gift.visibility = View.VISIBLE
                    it.tv_wish_gift_name.visibility = View.VISIBLE
                    it.rv_wish_result_date.visibility = View.VISIBLE
                    val data = WishingWallDto.ResidueProcessesBean(begin, end)
                    if (it.rv_wish_result_date.tag == null || dateList.isEmpty()) {
                        it.rv_wish_result_date.tag = data
                        dateList.add(0,data)
                    }else{
                        if (dateList.isNotEmpty()) {
                            dateList.removeAt(0)
                            dateList.add(0,data)
                        }
                    }
                    adapter.updateData(dateList)
                    chooseStyle(it,isStatus)
                    adapter.notifyDataSetChanged()
                } else {
                    pairingHeader(it,true)
                    it.ll_wish_fans_blessing.alpha = 1f
                    it.iv_wish_gift.visibility = View.GONE
                    it.tv_wish_gift_name.visibility = View.GONE
                    it.tv_wish_fans_blessing.text = content
                    it.tv_wish_fans_blessing_date.text = "★ $begin - $end ★"
                    it.rv_wish_result_date.visibility = View.GONE
                }
                it.tv_wish_click.setOnClickListener {
                    mWishOnClickListener?.let { it(item, helper.itemView, helper.adapterPosition) }
                }
            }
        }
    }


    ////////////
    //更换背景 隐藏按钮
    ////////////
    private fun chooseStyle(view: View, isChoose: Boolean) {
        if (isChoose) {
            view.iv_wish_result_mvp.setImageResource(R.drawable.icon_wish_result_mvp_click)
            view.tv_wish_click.visibility = View.VISIBLE
        }else{
            view.iv_wish_result_mvp.setImageResource(R.drawable.icon_wish_result_mvp_unclick)
            view.tv_wish_click.visibility = View.GONE
        }
    }



    ////////////
    //合并头像
    ////////////
    private fun pairingHeader(view: View, isPairing: Boolean) {
        if (offset == 0f) {
            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    val space = view.ll_fans_header.x - view.ll_anchor_header.x - view.ll_anchor_header.width
                    val translationX = space + view.resources.getDimension(R.dimen.dp_10)
                    offset = - translationX
                    pairingHeader(view,isPairing)
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
            return
        }

        view.apply {
            if (isPairing) {
               ll_fans_header.translationX = offset
               ll_anchor_header.layoutParams.width = resources.getDimension(R.dimen.dp_47).toInt()
               ll_fans_header.layoutParams.width = resources.getDimension(R.dimen.dp_47).toInt()
            } else {
                ll_fans_header.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                ll_anchor_header.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                ll_fans_header.translationX = 0f
            }
        }
    }


    /////////
    //点击许愿按钮 执行这个动画
    ////////
    fun runAnimatorMatching(view: View, position: Int) {
        view.apply {
            ll_anchor_header.layoutParams.width = resources.getDimension(R.dimen.dp_47).toInt()
            ll_fans_header.layoutParams.width = resources.getDimension(R.dimen.dp_47).toInt()
            tv_anchor_name.layoutParams.width = resources.getDimension(R.dimen.dp_41).toInt()
            tv_fans_name.layoutParams.width = resources.getDimension(R.dimen.dp_41).toInt()
            iv_wish_result_mvp.setImageResource(R.drawable.icon_wish_result_mvp_unclick)
            tv_wish_click.visibility = View.GONE
            val hideGift = ObjectAnimator.ofFloat(iv_wish_gift, "alpha", 1f, 0f)
            val hideGiftName = ObjectAnimator.ofFloat(tv_wish_gift_name, "alpha", 1f, 0f)
            val ast = AnimatorSet()
            ast.interpolator = LinearInterpolator()
            ast.playTogether(hideGift, hideGiftName)
            ast.duration = 500
            ast.start()
            iv_wish_gift.postDelayed({
                //执行粉丝头像动画
                val space = ll_fans_header.x - ll_anchor_header.x - ll_anchor_header.width
                val translationX = space + resources.getDimension(R.dimen.dp_10)
                val fansHeader = ObjectAnimator.ofFloat(ll_fans_header, "translationX", 0f, -translationX)
                fansHeader.interpolator = LinearInterpolator()
                fansHeader.duration = 1000
                fansHeader.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        val blessing = ObjectAnimator.ofFloat(ll_wish_fans_blessing, "alpha", 0f, 1f)
                        blessing.interpolator = LinearInterpolator()
                        blessing.duration = 500
                        blessing.start()
                        ll_wish_fans_blessing.postDelayed({
                            notifyItemChanged(position)
                        }, 500)
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })
                fansHeader.start()
            }, 500)
        }

    }


}