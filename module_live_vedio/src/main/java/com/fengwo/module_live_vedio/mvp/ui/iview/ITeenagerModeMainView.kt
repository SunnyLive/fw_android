package com.fengwo.module_live_vedio.mvp.ui.iview

import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_comment.bean.VensionDto
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_live_vedio.mvp.dto.TeenagerVideoDto

/**
 * 青少年模式主页-View
 *
 * @Author gukaihong
 * @Time 2020/12/9
 */
interface ITeenagerModeMainView : MvpView {
    /**列表数据*/
    fun updateListData(data: List<TeenagerVideoDto.Records>?, isRefresh: Boolean)

    /**更新版本信息*/
    fun updateAPPVersion(data: VensionDto?)

    /**更新用户信息*/
    fun updateUserInfo(data: UserInfo)
}