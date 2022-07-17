package com.fengwo.module_live_vedio.mvp.ui.iview.redpackage

import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_live_vedio.mvp.dto.redpackage.RedPackageOrderData

/**
 * 红包全部收益-发出的红包-收到的红包
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
interface IRedPackageAllOrderView : IBaseRedPackageView {

    /**
     * 更新信息
     *
     * @param headUrl 头像
     * @param name 名称
     * @param money 数量
     * @param tips 提示
     */
    fun updateInfo(headUrl: String, name: String, money: String, tips: String)


    /**
     * 更新列表数据
     *
     * @param isRefresh 是否是刷新
     * @param listData 数据
     */
    fun updateList(isRefresh: Boolean, listData: List<RedPackageOrderData.Record>)
}