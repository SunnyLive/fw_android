package com.fengwo.module_live_vedio.mvp.ui.iview

import com.fengwo.module_comment.base.MvpView

/**
 * 青少年模式设置-IView
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
interface ITeenagerModeSettingView : MvpView {
    /**更新UI*/
    fun updateUI(enable: Boolean)
    /**退出青少年模式*/
    fun exitTeenagerMode()
}