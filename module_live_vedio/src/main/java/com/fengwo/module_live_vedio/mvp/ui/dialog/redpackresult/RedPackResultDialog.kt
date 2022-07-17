package com.fengwo.module_live_vedio.mvp.ui.dialog.redpackresult

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.fengwo.module_comment.base.HttpResult
import com.fengwo.module_comment.ext.*
import com.fengwo.module_comment.utils.RetrofitUtils
import com.fengwo.module_comment.utils.RxUtils
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.api.LiveApiService
import com.fengwo.module_live_vedio.mvp.dto.PacketResultBean
import com.fengwo.module_live_vedio.mvp.ui.dialog.RedPackageDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_redpack_result.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * File RedPackResultDialog.kt
 * Date 12/30/20
 * Author lucas
 * Introduction 红包雨结果
 */
class RedPackResultDialog(val c: Context) : BasePopupWindow(c) {
    lateinit var compositeDisposable: CompositeDisposable
    private var redPacketId: Int = -1
    private var money: Int = 0 //领取金额

    override fun onCreateContentView(): View = createPopupById(R.layout.dialog_redpack_result)

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)
        compositeDisposable = CompositeDisposable()
        setBackgroundColor(context.getColor(R.color.alpha_50_black))
        popupGravity = Gravity.CENTER
        contentView.apply {
            v_receiver.setOnClickListener { dismiss() }
            v_receiver_info.setOnClickListener {
                if (c is AppCompatActivity) {
                    RedPackageDialog(RedPackageDialog.TYPE_MODE_INFO, redPacketId, money).show(c.supportFragmentManager, "redPacket")
                }
                dismiss()
            }
        }
    }

    fun show(packedId: Int) {
        redPacketId = packedId
        val disposable = RetrofitUtils().createApi(LiveApiService::class.java).getPacketResult(packedId)
                .compose(RxUtils.applySchedulers()).subscribe {
                    changeView(it)
                }
        compositeDisposable.add(disposable)
    }

    private fun changeView(bean: HttpResult<PacketResultBean>) {
        contentView.apply {
            if (bean.isSuccess && bean.data.id > 0) {
                money = bean.data.amount
                v_name.show()
                v_light.show()
                v_num.show()
                val refMasterUserName = bean.data.refMasterUserName
                v_name.text = "恭喜抢到${refMasterUserName}派发的${bean.data.nums}个红包"
                v_name.text.clearStyle().setSpanColor(refMasterUserName, Color.WHITE).into(v_name)
                v_num.text = bean.data.amount.toString()
                val loadAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
                loadAnimation.interpolator = LinearInterpolator()
                v_light.startAnimation(loadAnimation)
                v_bg.setImageResource(R.drawable.ic_redpack_success)
                v_receiver.text = "开心收下"
            } else {
                money = 0
                v_name.gone()
                v_light.gone()
                v_num.gone()
                v_bg.setImageResource(R.drawable.ic_redpack_fail)
                v_receiver.text = "红包已抢光"
            }
        }
        showPopupWindow()
    }

    override fun onDestroy() {
        contentView.v_light.clearAnimation()
        super.onDestroy()
    }
}