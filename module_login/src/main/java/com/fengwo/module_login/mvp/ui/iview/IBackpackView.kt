package com.fengwo.module_login.mvp.ui.iview

import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_comment.bean.BackpackDto

interface IBackpackView : MvpView {

    fun onBackpack(bp: BackpackDto)

}