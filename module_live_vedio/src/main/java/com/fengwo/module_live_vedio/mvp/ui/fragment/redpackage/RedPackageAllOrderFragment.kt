package com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fengwo.module_comment.base.BaseMvpFragment
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackageOrderData
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.ORDER_TYPE_INPUT
import com.fengwo.module_live_vedio.mvp.presenter.redpackage.RedPackageAllOrderPresenter
import com.fengwo.module_live_vedio.mvp.ui.adapter.redpackage.RedPackageOrderAdapter
import com.fengwo.module_live_vedio.mvp.ui.dialog.RedPackageDialog
import com.fengwo.module_live_vedio.mvp.ui.iview.redpackage.IRedPackageAllOrderView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_teenager_mode_main.smart_refresh
import kotlinx.android.synthetic.main.fragment_red_package_all_order.*

/**
 * 红包全部收益-发出的红包-收到的红包
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
class RedPackageAllOrderFragment : BaseMvpFragment<IRedPackageAllOrderView, RedPackageAllOrderPresenter>(), IRedPackageAllOrderView {

    private lateinit var imgHead: CircleImageView
    private lateinit var tvName: TextView
    private lateinit var tvMoney: TextView
    private lateinit var tvTips: TextView

    private val mRedPackageOrderAdapter by lazy { RedPackageOrderAdapter() }

    override fun immersionBarEnabled() = false

    override fun initPresenter() = RedPackageAllOrderPresenter()

    override fun setContentView() = R.layout.fragment_red_package_all_order

    override fun initUI(savedInstanceState: Bundle?) {
        initHeadView()
        SmartRefreshLayoutUtils.setWhiteBlackTextNoNight(requireContext(), smart_refresh)
        smart_refresh.setEnableRefresh(true)
        smart_refresh.setEnableLoadMore(true)
        smart_refresh.setOnLoadMoreListener {
            //加载更多
            p.loadData()
        }
        smart_refresh.setOnRefreshListener {
            p.refreshData(false)
        }

        rv_view.layoutManager = LinearLayoutManager(requireContext())
        mRedPackageOrderAdapter.bindToRecyclerView(rv_view)

        p.currentType = arguments?.getInt(RedPackageDialog.RED_PACKET_ORDER_TYPE)
                ?: ORDER_TYPE_INPUT

        p.refreshData(false)
        //smart_refresh.autoRefresh()
    }

    override fun updateInfo(headUrl: String, name: String, money: String, tips: String) {
        ImageLoader.loadImg(imgHead, headUrl, R.drawable.default_head)
        tvName.text = name
        tvMoney.text = money
        tvTips.text = tips
    }

    override fun updateList(isRefresh: Boolean, listData: List<RedPackageOrderData.Record>) {
        if (isRefresh) {
            mRedPackageOrderAdapter.setNewData(listData)
            rv_view.layoutManager?.scrollToPosition(0)
        } else {
            if (listData.isNullOrEmpty()) {
                smart_refresh.finishLoadMoreWithNoMoreData()
            } else {
                smart_refresh.finishLoadMore()
                mRedPackageOrderAdapter.addData(listData)
            }
        }
        smart_refresh.finishRefresh()
    }

    override fun dismissView() {

    }

    /**
     * 设置类型
     *
     * @param type
     */
    fun setMode(type: Int) {
        if (p.currentType == type) {
            return
        }
        smart_refresh.setNoMoreData(false)
        p.currentType = type
        p.clearHttpRequest()
        p.refreshData(false)
        //smart_refresh.autoRefresh()
    }

    private fun initHeadView() {
        val view = View.inflate(requireContext(), R.layout.include_red_package_all_order_head, null)
        mRedPackageOrderAdapter.addHeaderView(view)
        imgHead = view.findViewById(R.id.img_head)
        tvName = view.findViewById(R.id.tv_name)
        tvMoney = view.findViewById(R.id.tv_money)
        tvTips = view.findViewById(R.id.tv_tips)


        val emptyView = View.inflate(requireContext(), R.layout.include_red_package_empty, null)
        mRedPackageOrderAdapter.setHeaderAndEmpty(true)
        mRedPackageOrderAdapter.emptyView = emptyView
        mRedPackageOrderAdapter.emptyView?.let {
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.requestLayout()
        }
    }
}