package com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.fengwo.module_comment.base.BaseMvpFragment
import com.fengwo.module_comment.utils.DialogUtil
import com.fengwo.module_comment.utils.RedPacketConfigUtils
import com.fengwo.module_comment.utils.RxBus
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.*
import com.fengwo.module_live_vedio.mvp.ui.activity.BaseLiveingRoomActivity
import com.fengwo.module_live_vedio.mvp.ui.iview.redpackage.IRedPackageSendView
import kotlinx.android.synthetic.main.fragment_red_package_send.*

/**
 * 发送红包
 *
 * @Author gukaihong
 * @Time 2020/12/29
 */
class RedPackageSendFragment : BaseMvpFragment<IRedPackageSendView, RedPackageSendPresenter>(), IRedPackageSendView {


//    @Autowired
//    @JvmField
//    var userProviderService: UserProviderService? = null

    companion object {
        const val COMMON_RED_PACKET_TIPS = "当前为普通红包，<font color=\"#F7B500\">改为拼手气红包</font>"
        const val RANDOM_RED_PACKET_TIPS = "当前为拼手气红包，<font color=\"#F7B500\">改为普通红包</font>"
    }

    override fun immersionBarEnabled() = false

    override fun initPresenter() = RedPackageSendPresenter()

    override fun setContentView() = R.layout.fragment_red_package_send

    var handDialog: DialogFragment? = null//持有dialog

    private var rechargeTipsDialog: AlertDialog? = null


    override fun initUI(savedInstanceState: Bundle?) {
        tv_package_type.text = HtmlCompat.fromHtml(COMMON_RED_PACKET_TIPS, 0)

        tv_package_type.setOnClickListener {
            if (isFastClick()) {
                return@setOnClickListener
            }
            switchUI()
        }

        tv_send.setOnClickListener {
            if (isFastClick()) {
                return@setOnClickListener
            }

            if (!isByCheck()) {
                return@setOnClickListener
            }
            activity?.let {
                if (it is BaseLiveingRoomActivity) {
                    //发送红包
                    p.sendRedPackage(it.channelId, getCount(), getTotalMoney())
                }
            }

        }

        et_money.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    checkEditText(it, et_money)
                    if (p.packageType == TYPE_COMMON) {
                        if (it.length > MONEY_MAX_ONLY_LENGTH) {
                            s.delete(it.length - 1, it.length)
                        }
                    } else {
                        if (it.length > MONEY_MAX_TOTAL_LENGTH) {
                            s.delete(it.length - 1, it.length)
                        }
                    }
                }

                statisticsUpdateUI()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        et_unit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    checkEditText(it, et_unit)
                    //不能超过3位
                    val countMax = if (p.currentSendType == TYPE_SEND_LOCAL) {
                        RedPacketConfigUtils.instance.getRedpacketRoomMaxNum()
                    } else {
                        RedPacketConfigUtils.instance.getRedpacketStationMaxNum()
                    }
                    if (it.length > countMax.toString().length) {
                        s.delete(it.length - 1, it.length)
                    }

                    if ((it.toString().toIntOrNull() ?: 0) > countMax) {
                        requireActivity().runOnUiThread {
                            toastTip("发送红包个数不能高出$countMax")
                        }
                        et_unit.setTextColor(Color.parseColor("#FF2828"))
                    } else {
                        et_unit.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_333333))
                    }
                }
                statisticsUpdateUI()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        //是否是官方账号
        if (RedPacketConfigUtils.instance.isOfficialAccount) {
            rg_switch.visibility = View.VISIBLE
            send_layout.setBackgroundResource(R.drawable.bg_red_package_send)
        } else {
            rg_switch.visibility = View.GONE
            send_layout.setBackgroundResource(R.drawable.bg_red_package_rule)
        }

        rg_switch.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                rb_local.id -> p.currentSendType = TYPE_SEND_LOCAL
                rb_all.id -> p.currentSendType = TYPE_SEND_ALL
            }
            setUITips()
        }
        setUITips();
    }

    /**
     * 校验输入数字的正确性
     *
     * @param s
     * @param editText
     */
    private fun checkEditText(s: Editable, editText: EditText) {
        if (s.isNotEmpty() && s[0] == '0') {
            s.delete(0, 1)
            editText.setSelection(s.length)
        }
    }

    /**
     * 切换UI
     *
     */
    private fun switchUI() {
        if (p.packageType == TYPE_COMMON) {
            p.packageType = TYPE_RANDOM
            setUITips()
            et_money.setText("${p.totalMoney}")
        } else {
            p.packageType = TYPE_COMMON
            setUITips()
            val count = getCount()
            if (count != 0L) {
                val money = p.totalMoney / count
                et_money.setText("$money")
            }

        }


    }

    /**
     * 设置提示
     *
     */
    private fun setUITips() {
        if (p.packageType == TYPE_COMMON) {
            tv_package_type.text = HtmlCompat.fromHtml(COMMON_RED_PACKET_TIPS, 0)
            text_view.setCompoundDrawables(null, null, null, null)
            text_view.text = "单个花钻："
            if (p.currentSendType == TYPE_SEND_LOCAL) {
                //本房间
                et_money.hint = "${RedPacketConfigUtils.instance.getRedPacketRoomNormalAmount()}起"
                et_unit.hint = "${RedPacketConfigUtils.instance.getRedPacketRoomNum()}起"
            } else {
                //全站
                et_money.hint = "${RedPacketConfigUtils.instance.getRedPacketStationNormalAmount()}起"
                et_unit.hint = "${RedPacketConfigUtils.instance.getRedPacketStationNum()}起"
            }
        } else {
            tv_package_type.text = HtmlCompat.fromHtml(RANDOM_RED_PACKET_TIPS, 0)
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_red_package_random)
            drawable?.let {
                it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
                text_view.setCompoundDrawables(it, null, null, null)
            }
            text_view.text = "总花钻："
            if (p.currentSendType == TYPE_SEND_LOCAL) {
                //本房间
                et_money.hint = "${RedPacketConfigUtils.instance.getRedPacketRoomRandomAmount()}起"
                et_unit.hint = "${RedPacketConfigUtils.instance.getRedPacketRoomNum()}起"
            } else {
                //全站
                et_money.hint = "${RedPacketConfigUtils.instance.getRedPacketStationRandomAmount()}起"
                et_unit.hint = "${RedPacketConfigUtils.instance.getRedPacketStationNum()}起"
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        handDialog = null
        rechargeTipsDialog = null
    }

    /**
     * 统计计算UI变化
     *
     */
    private fun statisticsUpdateUI() {
        val total = getMoney()
        val unit = getCount()
        if (p.packageType == TYPE_COMMON) {
            p.totalMoney = total * unit
        } else {
            p.totalMoney = total
        }
        tv_money.text = p.totalMoney.toString()
        tv_send.isEnabled = p.totalMoney > 0
    }

    /**
     * 获取单价
     *
     * @return
     */
    private fun getMoney(): Long = et_money.text.toString().toLongOrNull() ?: 0L

    /**
     * 获取个数
     *
     * @return
     */
    private fun getCount(): Long = et_unit.text.toString().toLongOrNull() ?: 0L

    /**
     * 获取总价
     *
     * @return
     */
    private fun getTotalMoney(): Long = tv_money.text.toString().toLongOrNull() ?: 0L

    /**
     * 是否通过要求
     *
     * @return
     */
    private fun isByCheck(): Boolean {
        val money = getMoney()
        val count = getCount()
        val countMin = if (p.currentSendType == TYPE_SEND_LOCAL) {
            RedPacketConfigUtils.instance.getRedPacketRoomNum()
        } else {
            RedPacketConfigUtils.instance.getRedPacketStationNum()
        }
        val countMax = if (p.currentSendType == TYPE_SEND_LOCAL) {
            RedPacketConfigUtils.instance.getRedpacketRoomMaxNum()
        } else {
            RedPacketConfigUtils.instance.getRedpacketStationMaxNum()
        }
        val moneyMin = if (p.currentSendType == TYPE_SEND_LOCAL) {
            if (p.packageType == TYPE_COMMON) {
                RedPacketConfigUtils.instance.getRedPacketRoomNormalAmount()
            } else {
                RedPacketConfigUtils.instance.getRedPacketRoomRandomAmount()
            }
        } else {
            if (p.packageType == TYPE_COMMON) {
                RedPacketConfigUtils.instance.getRedPacketStationNormalAmount()
            } else {
                RedPacketConfigUtils.instance.getRedPacketStationRandomAmount()
            }
        }

        if (count < countMin || money < moneyMin) {
            toastTip("发送花钻数量低于最低值")
            return false
        }

        if (count > countMax) {
            toastTip("发送红包个数不能高出$countMax")
            return false
        }

        if (getTotalMoney() > Integer.MAX_VALUE) {
            toastTip("发送花钻总数不能超过${Integer.MAX_VALUE}")
            return false
        }

        return true
    }

    override fun showRechargeTips() {
        if (rechargeTipsDialog?.isShowing != true) {
            rechargeTipsDialog = DialogUtil.showNoMoneyDialog(activity, object : DialogUtil.AlertDialogBtnClickListener {
                override fun clickPositive() {
                    handDialog?.dismiss()
                    RxBus.get().post(ShowRechargePopEvent())
                }

                override fun clickNegative() {

                }

            })
        }
    }

    override fun dismissView() {
        handDialog?.dismiss()
    }
}