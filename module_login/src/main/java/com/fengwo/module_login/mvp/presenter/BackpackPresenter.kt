package com.fengwo.module_login.mvp.presenter

import android.annotation.SuppressLint
import com.fengwo.module_login.mvp.ui.iview.IBackpackView

class BackpackPresenter : BaseLoginPresenter<IBackpackView>(){

    @SuppressLint("CheckResult")
    fun getBackpack(){
        service.backpack.compose(io_main())
                .subscribe {
                    if (it.isSuccess) {
                        view.onBackpack(it.data)
                    }
                }
    }
}