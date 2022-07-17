package com.fengwo.module_live_vedio.mvp.presenter

import android.text.TextUtils
import com.fengwo.module_comment.Constants
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_live_vedio.mvp.dto.TeenagerInfoDto
import com.fengwo.module_live_vedio.mvp.ui.iview.ITeenagerModePasswordView

/**
 * 青少年模式密码-设置-校验
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
class TeenagerModePasswordPresenter : BaseLivePresenter<ITeenagerModePasswordView>() {

    var firstPassword: String? = null//第一次输入的密码
    var isFirst = false//是否是第一次


    /**密码校验*/
    fun checkPassword(userInfo: UserInfo?, password: String) {
        userInfo?.let {
            if (isFirst) {
                //第一次
                if (firstPassword.isNullOrEmpty()) {
                    firstPassword = password
                    //再次输入密码
                    view.reTypePassword()
                } else {
                    if (TextUtils.equals(firstPassword, password)) {
                        view.hideInput()
                        //进入青少年模式
                        request(it.id, password, true)
                    } else {
                        //密码错误
                        view.errorTips("第一次密码和第二次密码不一致，请重新输入")
                    }
                }
            } else {
                //不是第一次
                if (TextUtils.equals(firstPassword, password)) {
                    view.hideInput()
                    //启用状态取反，如果已开启，则关闭
                    if (enableTeenager(it)) {
                        //关闭
                        view.exitTeenagerMode()
                    } else {
                        //进入
                        request(it.id, password, true)
                    }

                } else {
                    //密码错误
                    view.errorTips("密码错误，请重新输入")
                }

            }
        }

    }

    /**更新UI*/
    fun updateUI(userInfo: UserInfo?) {
        userInfo?.let {
            isFirst = TextUtils.isEmpty(it.teenagerPassword)
            if (isFirst) {
                //没有密码,需要设置密码
                view.updateUI("设置独立密码开启青少年模式", false)
            } else {
                firstPassword = it.teenagerPassword
                view.updateUI("请输入密码", true)
            }
        }

    }

    /**是否开启青少年模式*/
    private fun enableTeenager(userInfo: UserInfo): Boolean {
        return TextUtils.equals(userInfo.teenagerMode, Constants.TEENAGER_MODE_ENABLE)
    }

    /**请求接口-更新状态*/
    private fun request(id: Int, teenagerPassword: String, isJoin: Boolean) {
        val teenagerMode: String = if (isJoin) {
            Constants.TEENAGER_MODE_ENABLE
        } else {
            Constants.TEENAGER_MODE_UNENABLE
        }
        addNet(service.updateTeenagerModeInfo(TeenagerInfoDto(id, 0, teenagerMode, teenagerPassword))
                .compose(handleResult<String>())
                .subscribeWith(object : LoadingObserver<String>(true, true, view) {
                    override fun _onNext(data: String?) {
                        joinOrExit(isJoin, teenagerPassword)
                    }

                    override fun _onError(msg: String?) {
                        val error = msg ?: "请求错误"
                        if (error.contains("onNext called with null")) {
                            joinOrExit(isJoin, teenagerPassword)
                        } else {
                            view.errorTips(error)
                        }

                    }
                }))
    }

    private fun joinOrExit(isJoin: Boolean, teenagerPassword: String) {
        if (isJoin) {
            //进入
            view?.joinTeenagerMode(teenagerPassword)
        } else {
            //退出
            view?.exitTeenagerMode()
        }
    }

}