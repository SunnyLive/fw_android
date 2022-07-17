package com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fengwo.module_comment.base.BaseMvpFragment
import com.fengwo.module_comment.ext.gone
import com.fengwo.module_comment.ext.show
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackagePerson
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.RedPackageOrderPresenter
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.TYPE_COMMON
import com.fengwo.module_live_vedio.mvp.ui.activity.BaseLiveingRoomActivity
import com.fengwo.module_live_vedio.mvp.ui.adapter.redpackage.RedPackagePersonAdapter
import com.fengwo.module_live_vedio.mvp.ui.dialog.RedPackageDialog
import com.fengwo.module_live_vedio.mvp.ui.iview.redpackage.IRedPackageOrderView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_red_package_order.*

/**
 * 红包我的记录
 *
 * @Author gukaihong
 * @Time 2020/12/30
 */
class RedPackageOrderFragment : BaseMvpFragment<IRedPackageOrderView, RedPackageOrderPresenter>(), IRedPackageOrderView {

    private lateinit var imgHead: CircleImageView
    private lateinit var tvName: TextView
    private lateinit var tvMoney: TextView
    private lateinit var tvTips: TextView
    private lateinit var tvNull: TextView
    private lateinit var imgIcon: ImageView
    private lateinit var groupMoney: View

    private val mRedPackagePersonAdapter by lazy { RedPackagePersonAdapter() }

    override fun immersionBarEnabled() = false

    override fun initPresenter() = RedPackageOrderPresenter()

    override fun setContentView() = R.layout.fragment_red_package_order

    override fun initUI(savedInstanceState: Bundle?) {
        intHeadView()
        rv_view.layoutManager = LinearLayoutManager(requireContext())
        mRedPackagePersonAdapter.bindToRecyclerView(rv_view)
        val currentActivity = requireActivity()
        if (currentActivity is BaseLiveingRoomActivity) {
            p.getPersonList(currentActivity.channelId, arguments?.getInt(RedPackageDialog.RED_PACKET_ID)
                    ?: -1)
        }
    }

    override fun updatePersonList(listData: List<RedPackagePerson.Item>) {
        mRedPackagePersonAdapter.setNewData(listData)
    }

    override fun updateRedPackInfo(headUrl: String, name: String, money: Int, packageType: Int, tips: String) {
        ImageLoader.loadImg(imgHead, headUrl, R.drawable.default_head)
        tvName.text = "${name}送出的红包"
        val currentMoney = (arguments?.getInt(RedPackageDialog.MONEY_VALUE) ?: 0)
        if (currentMoney <= 0) {
            tvNull.show()
            groupMoney.gone()
        } else {
            tvNull.gone()
            tvMoney.text = currentMoney.toString()
            groupMoney.show()
        }

        tvTips.text = tips
        if (packageType == TYPE_COMMON) {
            imgIcon.gone()
        } else {
            imgIcon.show()
        }

    }

    override fun dismissView() {

    }

    /**
     * 初始化头部UI
     *
     */
    private fun intHeadView() {
        val view = View.inflate(requireContext(), R.layout.include_red_package_order_head, null)
        mRedPackagePersonAdapter.addHeaderView(view)
        imgHead = view.findViewById(R.id.img_head)
        tvName = view.findViewById(R.id.tv_name)
        tvMoney = view.findViewById(R.id.tv_money)
        imgIcon = view.findViewById(R.id.img_icon)
        tvTips = view.findViewById(R.id.tv_tips)
        groupMoney = view.findViewById(R.id.group_money)
        tvNull = view.findViewById(R.id.tv_null)

        val emptyView = View.inflate(requireContext(), R.layout.include_red_package_empty, null)
        mRedPackagePersonAdapter.setHeaderAndEmpty(true)
        mRedPackagePersonAdapter.emptyView = emptyView
        mRedPackagePersonAdapter.emptyView?.let {
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.requestLayout()
        }
    }


}