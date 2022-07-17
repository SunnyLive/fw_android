package com.fengwo.module_live_vedio.mvp.presenter

import android.annotation.SuppressLint
import com.fengwo.module_live_vedio.mvp.ui.iview.IWishResultView

class WishResultPresenter : BaseLivePresenter<IWishResultView>() {


    @SuppressLint("CheckResult")
    fun requestWishing(anchor: String, time: String, content: String) {
        service.requestWishing(ParamsBuilder()
                .put("timeFmtStr", time)
                .put("wishAnchorId", anchor)
                .put("wishContent", content)
                .build()).compose(io_main())
                .subscribe {
                    if (it.isSuccess) {
                        view.onWishingSuccess()
                    }else{
                        view.toastTip(it.description)
                    }
                }
    }


    @SuppressLint("CheckResult")
    fun getWishList() {
        service.requestWishingWallList()
                .compose(io_main())
                .subscribe {
                    if (it.isSuccess) {
                        view.onWishList(it.data)
                    }else{
                        view.toastTip(it.description)
                    }
                }
    }


    @SuppressLint("CheckResult")
    fun getBlessing(pageParam: Int) {
        service.getBlessing("$pageParam,30").compose(io_main())
                .subscribe {
                    if (it.isSuccess) {
                        view.onBlessing(it.data)
                    }
                }
    }


}