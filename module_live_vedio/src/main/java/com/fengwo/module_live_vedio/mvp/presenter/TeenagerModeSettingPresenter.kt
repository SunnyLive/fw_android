package com.fengwo.module_live_vedio.mvp.presenter

import android.text.TextUtils
import com.fengwo.module_comment.Constants
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_live_vedio.mvp.dto.TeenagerInfoDto
import com.fengwo.module_live_vedio.mvp.ui.iview.ITeenagerModeSettingView

/**
 * 青少年模式设置-Presenter
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
class TeenagerModeSettingPresenter : BaseLivePresenter<ITeenagerModeSettingView>() {

    var isEnable = false

    /**更新UI*/
    fun updateView(userInfo: UserInfo?) {
        userInfo?.let {
            isEnable = TextUtils.equals(it.teenagerMode, Constants.TEENAGER_MODE_ENABLE)
            view.updateUI(isEnable)
        }

    }

    /**请求接口-更新状态*/
    fun exitTeenagerMode(userInfo: UserInfo?) {
        userInfo?.let { info ->
            addNet(service.updateTeenagerModeInfo(TeenagerInfoDto(info.id, 0, Constants.TEENAGER_MODE_UNENABLE, info.teenagerPassword))
                    .compose(handleResult<String>())
                    .subscribeWith(object : LoadingObserver<String>(true, true, view) {
                        override fun _onNext(data: String?) {
                            //退出
                            view?.exitTeenagerMode()
                        }

                        override fun _onError(msg: String?) {
                            val error = msg ?: "请求错误"
                            if (error.contains("onNext called with null")) {
                                //退出
                                view?.exitTeenagerMode()
                            } else {
                                view.toastTip(error)
                            }
                        }
                    }))
        }


    }
}