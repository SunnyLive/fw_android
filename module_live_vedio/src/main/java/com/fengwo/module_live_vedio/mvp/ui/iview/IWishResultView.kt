package com.fengwo.module_live_vedio.mvp.ui.iview

import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_live_vedio.mvp.dto.BlessingDto
import com.fengwo.module_live_vedio.mvp.dto.WishingWallDto

interface IWishResultView : MvpView {


    fun onWishingSuccess()

    fun onWishList(data: WishingWallDto)


    fun onBlessing(data: BlessingDto)


}