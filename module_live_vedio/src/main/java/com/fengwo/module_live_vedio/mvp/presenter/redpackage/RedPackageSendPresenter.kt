package com.fengwo.module_live_vedio.mvp.presenter.redpackage

import android.text.TextUtils
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.bean.Param
import com.fengwo.module_live_vedio.mvp.dto.redpackage.SendRedPackageParam
import com.fengwo.module_live_vedio.mvp.ui.iview.redpackage.IRedPackageSendView

/**
 * 发红包
 *
 * @Author gukaihong
 * @Time 2020/12/29
 */
const val TYPE_COMMON = 0 //普通红包
const val TYPE_RANDOM = 1 //拼手气
const val TYPE_SEND_LOCAL = 1 //房间红包
const val TYPE_SEND_ALL = 0 //全站红包

const val MONEY_MAX_ONLY_LENGTH = 7
const val MONEY_MAX_TOTAL_LENGTH = 11

class RedPackageSendPresenter : BaseRedPackagePresenter<IRedPackageSendView>() {
    //红包类型
    var packageType: Int = TYPE_COMMON

    //当前发送类型
    var currentSendType: Int = TYPE_SEND_LOCAL

    //总价格
    var totalMoney: Long = 0

    /**
     * 发红包
     *
     * @param anchorId 主播id
     * @param packageCount 红包个数
     * @param totalAmount 金钱
     */
    fun sendRedPackage(anchorId: Int, packageCount: Long, totalAmount: Long) {
        addNet(serviceEncryption.postRedPackageSend(Param(SendRedPackageParam(packageCount, currentSendType, packageType, anchorId, totalAmount)))
                .compose(handleResult())
                .subscribeWith(object : LoadingObserver<String>(view) {
                    override fun _onNext(data: String?) {
                        view.toastTip("发送成功")
                        view.dismissView()
                    }

                    override fun _onError(msg: String?) {
                        if (TextUtils.equals("余额不足", msg)) {
                            view.showRechargeTips()
                        } else {
                            view.toastTip(msg ?: "请求错误")
                        }
                    }

                }))
    }
}