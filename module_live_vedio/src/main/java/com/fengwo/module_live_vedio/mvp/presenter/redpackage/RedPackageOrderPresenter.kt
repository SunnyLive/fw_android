package com.fengwo.module_live_vedio.mvp.presenter.redpackage

import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackagePerson
import com.fengwo.module_live_vedio.mvp.ui.iview.redpackage.IRedPackageOrderView

/**
 * 红包我的记录
 *
 * @Author gukaihong
 * @Time 2020/12/30
 */
class RedPackageOrderPresenter : BaseRedPackagePresenter<IRedPackageOrderView>() {

    /**
     * 获取记录名单
     *
     */
    fun getPersonList(anchorId: Int, redpacketId: Int) {
        addNet(service.getRedPackageSend(anchorId.toString(), redpacketId.toString())
                .compose(handleResult())
                .subscribeWith(object : LoadingObserver<RedPackagePerson>(false, view) {
                    override fun _onNext(data: RedPackagePerson?) {
                        data?.let {
                            val tips = "送出${data.totalAmount ?: 0}花钻，共${data.totalNum ?: 0}个红包"
                            view.updateRedPackInfo(it.headImg ?: "", it.nickname
                                    ?: "未知", it.receiveAmount ?: 0, it.redpacketType
                                    ?: TYPE_COMMON, tips)
                            view.updatePersonList(data.itemList
                                    ?: ArrayList())
                        }
                    }

                    override fun _onError(msg: String?) {
                        view.toastTip(msg ?: "请求错误")
                    }

                }))
    }

}