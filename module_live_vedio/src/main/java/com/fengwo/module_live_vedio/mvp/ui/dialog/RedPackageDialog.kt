package com.fengwo.module_live_vedio.mvp.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fengwo.module_comment.dialog.BottomSheetDialog
import com.fengwo.module_comment.utils.KLog
import com.fengwo.module_comment.utils.RedPacketConfigUtils
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.ORDER_TYPE_INPUT
import com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage.RedPackageAllOrderFragment
import com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage.RedPackageOrderFragment
import com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage.RedPackageRuleFragment
import com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage.RedPackageSendFragment
import com.trello.rxlifecycle2.components.support.RxDialogFragment
import kotlinx.android.synthetic.main.dialog_red_package.*

/**
 * 发送红包
 *
 * @Author gukaihong
 * @Time 2020/12/29
 */
class RedPackageDialog(private var typeMode: Int = TYPE_MODE_SEND, private var redPacketId: Int = -1, private var money: Int = 0) : RxDialogFragment() {

    companion object {
        const val TYPE_MODE_SEND = 0
        const val TYPE_MODE_INFO = 1
        const val RED_PACKET_ID = "redPacketId"
        const val RED_PACKET_ORDER_TYPE = "redPacketType"
        const val MONEY_VALUE = "moneyValue"

        private const val KEY_BUTTON_RULE = 0 //规则
        private const val KEY_BUTTON_BACK = 1 //返回
        private const val KEY_BUTTON_MY_ORDER = 2 //我的记录
        private const val KEY_BUTTON_MORE = 3 //更多
        private const val KEY_BUTTON_ALL_ORDER = 4 //全部记录
    }

//    @Autowired
//    @JvmField
//    var userProviderService: UserProviderService? = null

    var currentOrderFragment: RedPackageAllOrderFragment? = null

    var lastFragment: Fragment? = null

    override fun onStart() {
        super.onStart()
        val win = dialog?.window
        //win?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // win?.attributes?.windowAnimations = R.style.livePopWindowStyle
        // win?.setDimAmount(0.5f)
        val params = win?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        var height = resources.getDimension(R.dimen.dp_422).toInt()
        //是否是官方账号
        if (RedPacketConfigUtils.instance.isOfficialAccount) {
            height += resources.getDimension(R.dimen.dp_32).toInt()
        }
        params?.height = height
        win?.attributes = params
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //  ARouter.getInstance().inject(this)
        return inflater.inflate(R.layout.dialog_red_package, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.RedPacketDialogStyle)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.addOnBackStackChangedListener(mOnBackStackChangedListener)
        initView()
        tv_left.setOnClickListener { it ->
            if (it.tag is Int) {
                when (it.tag) {
                    KEY_BUTTON_RULE -> showRule()
                    KEY_BUTTON_BACK -> back()
                }
            }
        }
        tv_right.setOnClickListener {
            if (it.tag is Int) {
                when (it.tag) {
                    KEY_BUTTON_MY_ORDER -> showMyOrder()
                    KEY_BUTTON_ALL_ORDER -> showAllOrder()
                    KEY_BUTTON_MORE -> showMenu()
                }
            }
        }


    }


    /**
     * 初始化
     *
     */
    private fun initView() {
        when (typeMode) {
            TYPE_MODE_SEND -> showSend()
            TYPE_MODE_INFO -> showMyOrder()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // userProviderService = null
        currentOrderFragment = null
        lastFragment = null
        childFragmentManager.removeOnBackStackChangedListener(mOnBackStackChangedListener)
        KLog.i("gkh", "onDestroyView")
    }

    /**
     * 发红包
     *
     */
    private fun showSend(isBack: Boolean = false) {
        tv_left.tag = KEY_BUTTON_RULE
        tv_right.tag = KEY_BUTTON_ALL_ORDER
        tv_title.text = "直播间红包"
        setLeftButton("规则介绍")
        setRightButton("我的记录")
        if (!isBack) {
            val sendFragment = RedPackageSendFragment()
            sendFragment.handDialog = this
            showFragment(sendFragment)
        }
    }


    /**
     * 红包规则
     *
     */
    private fun showRule(isBack: Boolean = false) {
        tv_title.text = "规则介绍"
        tv_left.tag = KEY_BUTTON_BACK
        setLeftButton(resourceId = R.drawable.icon_redpackage_back_button)
        setRightButton()
        if (!isBack) {
            val rule = RedPackageRuleFragment()
            showFragment(rule)
        }
    }

    /**
     * 我的记录
     *
     * @param isBack
     */
    private fun showMyOrder(isBack: Boolean = false) {
        tv_title.text = "我的记录"
        tv_left.tag = KEY_BUTTON_BACK
        tv_right.tag = KEY_BUTTON_MORE
        setLeftButton(resourceId = R.drawable.icon_redpackage_back_button)
        setRightButton(resourceId = R.drawable.icon_red_package_more)
        if (!isBack) {
            val order = RedPackageOrderFragment()
            val bundle = Bundle()
            bundle.putInt(RED_PACKET_ID, redPacketId)
            bundle.putInt(MONEY_VALUE, money)
            order.arguments = bundle
            showFragment(order)
        }
    }

    /**
     * 全部收益信息
     *
     * @param isBack
     */
    private fun showAllOrder(isBack: Boolean = false, type: Int = ORDER_TYPE_INPUT) {
        tv_title.text = "收到的红包"
        tv_left.tag = KEY_BUTTON_BACK
        tv_right.tag = KEY_BUTTON_MORE
        setLeftButton(resourceId = R.drawable.icon_redpackage_back_button)
        setRightButton(resourceId = R.drawable.icon_red_package_more)
        if (!isBack) {
            currentOrderFragment = RedPackageAllOrderFragment()
            val bundle = Bundle()
            bundle.putInt(RED_PACKET_ORDER_TYPE, type)
            currentOrderFragment!!.arguments = bundle
            showFragment(currentOrderFragment!!)
        }
    }

    /**
     * 展示页面
     *
     * @param fragment
     */
    private fun showFragment(frag: Fragment) {
        childFragmentManager.let {
            val transaction = it.beginTransaction()
            lastFragment?.let { last ->
                //隐藏上一个
                transaction.hide(last)
            }
            transaction.add(fragment.id, frag)
            transaction.addToBackStack(null)
            transaction.show(frag)
            transaction.commit()
            lastFragment = frag
        }
    }

    /**
     * 返回
     *
     */
    private fun back() {
        childFragmentManager.let {
            KLog.i("gkh", "backStackEntryCount : ${it.backStackEntryCount}")
            if (it.backStackEntryCount <= 1) {
                dismiss()
            } else {
                it.popBackStack()
            }
        }
    }

    /**
     * 设置左边按钮
     *
     * @param text 文本
     * @param resourceId 图片id
     */
    private fun setLeftButton(text: String = "", @DrawableRes resourceId: Int = -1) {
        setTextView(tv_left, text, resourceId)

    }

    /**
     * 设置右边按钮
     *
     * @param text 文本
     * @param resourceId 图片id
     */
    private fun setRightButton(text: String = "", @DrawableRes resourceId: Int = -1) {
        setTextView(tv_right, text, resourceId)
    }

    /**
     *  设置文本
     *
     * @param textView
     * @param text
     * @param resourceId
     */
    private fun setTextView(textView: TextView, text: String = "", @DrawableRes resourceId: Int = -1) {
        textView.text = text
        if (resourceId != -1) {
            val drawable = ContextCompat.getDrawable(requireContext(), resourceId)
            drawable?.let {
                it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
                textView.setCompoundDrawables(null, null, it, null)
            }
        } else {
            textView.setCompoundDrawables(null, null, null, null)
        }
    }

    private val mOnBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
        val fragment = childFragmentManager.findFragmentById(fragment.id)
        lastFragment = fragment
        if (fragment is RedPackageSendFragment) {
            showSend(true)
            currentOrderFragment = null;
        } else if (fragment is RedPackageOrderFragment) {
            showMyOrder(true)
            currentOrderFragment = null;
        }
    }

    /**
     * 显示菜单
     *
     */
    private fun showMenu() {
        val strings = listOf("收到的红包", "发出的红包")
        BottomSheetDialog(requireContext()).setOnItemClick(object : BottomSheetDialog.OnItemClickListen {
            override fun onItemClick(position: Int) {
                if (currentOrderFragment == null) {
                    showAllOrder(type = position)
                } else {
                    currentOrderFragment?.setMode(position)
                }
                tv_title.text = strings[position]
            }

        }).show(view, strings)
    }

}