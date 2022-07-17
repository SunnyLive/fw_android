package com.fengwo.module_live_vedio.mvp.ui.iview.redpackage

import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackagePerson

/**
 * 红包我的记录
 *
 * @Author gukaihong
 * @Time 2020/12/30
 */
interface IRedPackageOrderView : IBaseRedPackageView {
    /**
     * 更新人员名单
     *
     */
    fun updatePersonList(listData: List<RedPackagePerson.Item>)

    /**
     * 更新红包详情信息
     *
     * @param headUrl 头像Url
     * @param name 用户昵称
     * @param money 花砖数量
     * @param packageType 红包类型
     */
    fun updateRedPackInfo(headUrl: String, name: String, money: Int, packageType: Int, tips: String)
}