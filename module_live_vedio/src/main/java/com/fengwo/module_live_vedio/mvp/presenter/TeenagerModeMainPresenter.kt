package com.fengwo.module_live_vedio.mvp.presenter

import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.bean.VensionDto
import com.fengwo.module_comment.utils.KLog
import com.fengwo.module_live_vedio.mvp.dto.TeenagerVideoDto
import com.fengwo.module_live_vedio.mvp.ui.iview.ITeenagerModeMainView

/**
 * 青少年模式主页
 *
 * @Author gukaihong
 * @Time 2020/12/9
 */
class TeenagerModeMainPresenter : BaseLivePresenter<ITeenagerModeMainView>() {
    var page = 1;

    /**获取列表数据*/
    fun getListData(isRefresh: Boolean, needLoading: Boolean = false) {
        if (isRefresh) {
            page = 1
        } else {
            page++
        }
        addNet(service.teenagerVideoList("$page,$PAGE_SIZE")
                .compose(handleResult<TeenagerVideoDto>())
                .subscribeWith(object : LoadingObserver<TeenagerVideoDto>(needLoading, view) {
                    override fun _onNext(data: TeenagerVideoDto?) {
                        val records: List<TeenagerVideoDto.Records>? = data?.records
                        view.updateListData(records, isRefresh)
                    }

                    override fun _onError(msg: String?) {
                        view.toastTip(msg ?: "请求异常")
                    }

                }))

    }


    /**播放次数计数*/
    fun addCount(id: String) {
        addNet(service.putTeenagerAddView(id)
                .compose(io_main())
                .subscribe {
                    it?.let {
                        if (it.isSuccess) {
                            KLog.d("TeenagerModeMainPresenter", "播放次数计数:统计成功")
                        }
                    }
                })
    }

    /**更新用户信息*/
    fun updateUserInfo() {
        addNet(service.getUserCenter()
                .compose(io_main())
                .subscribe {
                    it?.let {
                        if (it.isSuccess) {
                            view.updateUserInfo(it.data)
                        }
                    }
                })

    }

    fun checkVersion() {
        addNet(service.appVersion()
                .compose(handleResult<VensionDto>())
                .subscribeWith(object : LoadingObserver<VensionDto?>() {
                    override fun _onNext(data: VensionDto?) {
                        view.updateAPPVersion(data)
                    }

                    override fun _onError(msg: String) {}
                }))
    }
}