package com.fengwo.module_live_vedio.mvp.presenter.redpackage

import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackageOrderData
import com.fengwo.module_live_vedio.mvp.ui.iview.redpackage.IRedPackageAllOrderView
import java.util.*
import kotlin.collections.ArrayList

/**
 * 红包全部收益-发出的红包-收到的红包
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
const val ORDER_TYPE_INPUT = 0
const val ORDER_TYPE_OUTPUT = 1

class RedPackageAllOrderPresenter : BaseRedPackagePresenter<IRedPackageAllOrderView>() {
    companion object {
        private const val PAGE = 1
    }

    var currentPage: Int = PAGE
    var currentType = ORDER_TYPE_INPUT


    /**
     * 刷新数据
     *
     */
    fun refreshData(needLoading: Boolean) {
        currentPage = PAGE
        getData(needLoading)
    }

    /**
     * 加载新数据
     *
     */
    fun loadData() {
        currentPage++
        getData(false)

    }

    private fun getData(needLoading: Boolean) {
        val map = HashMap<String?, Any?>()
        map["current"] = currentPage
        map["isGetTotal"] = 1
        map["size"] = 20
        val body = createRequestBody(map)
        if (currentType == ORDER_TYPE_INPUT) {
            addNet(service.postOrderIn(body)
                    .compose(handleResult())
                    .subscribeWith(object : LoadingObserver<RedPackageOrderData>(needLoading, true, view) {
                        override fun _onNext(data: RedPackageOrderData?) {
                            data?.let {
                                updateUI(it)
                            }

                        }

                        override fun _onError(msg: String?) {
                            view.toastTip(msg ?: "请求错误")
                        }

                    }))
        } else {
            addNet(service.postOrderOut(body)
                    .compose(handleResult())
                    .subscribeWith(object : LoadingObserver<RedPackageOrderData>(needLoading, true, view) {
                        override fun _onNext(data: RedPackageOrderData?) {
                            data?.let {
                                updateUI(it)
                            }

                        }

                        override fun _onError(msg: String?) {
                            view.toastTip(msg ?: "请求错误")
                        }

                    }))
        }


    }

    private fun updateUI(it: RedPackageOrderData) {
        if (currentPage == PAGE) {
            if (currentType == ORDER_TYPE_INPUT) {
                view.updateInfo(it.headImg ?: "", "${it.nickname ?: "未知"}共收到"
                        , it.totalAmount?.toString() ?: "0", "")
            } else {
                view.updateInfo(it.headImg ?: "", "${it.nickname ?: "未知"}共发出"
                        , it.totalAmount?.toString()
                        ?: "0", "发出红包总数${it.totalNum}个")
            }
        }
        view.updateList(currentPage == PAGE, it.itemPage?.records ?: ArrayList())
    }


}