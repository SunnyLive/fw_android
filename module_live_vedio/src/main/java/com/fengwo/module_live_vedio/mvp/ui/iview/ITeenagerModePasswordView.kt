package com.fengwo.module_live_vedio.mvp.ui.iview

import com.fengwo.module_comment.base.MvpView

/**
 * 青少年模式密码-设置-校验
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
interface ITeenagerModePasswordView : MvpView {
    /**退出青少年模式*/
    fun exitTeenagerMode()

    /**进入青少年模式*/
    fun joinTeenagerMode(password: String)

    /**错误提示*/
    fun errorTips(msg: String)

    /**设置UI*/
    fun updateUI(title: String, showTips: Boolean)

    /**再次输入密码*/
    fun reTypePassword()

    /**隐藏键盘*/
    fun hideInput()
}