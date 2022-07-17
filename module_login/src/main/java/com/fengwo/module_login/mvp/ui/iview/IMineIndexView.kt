package com.fengwo.module_login.mvp.ui.iview

import com.fengwo.module_comment.base.BaseListDto
import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_login.mvp.dto.GiftWallDto
import com.fengwo.module_login.mvp.dto.MyCarDto
import com.fengwo.module_login.mvp.dto.WatchHistoryDto

interface IMineIndexView : MvpView{

    fun updateUser(user: UserInfo)

    fun onWatchHistory(ws: BaseListDto<WatchHistoryDto>)

    fun onCar(car: MyCarDto)

    fun onGiftWall(gift: GiftWallDto)

    fun reSetRefresh()
}