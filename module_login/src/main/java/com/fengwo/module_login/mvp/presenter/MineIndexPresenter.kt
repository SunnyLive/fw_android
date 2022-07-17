package com.fengwo.module_login.mvp.presenter

import android.annotation.SuppressLint
import android.text.TextUtils
import com.fengwo.module_comment.base.HttpResult
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_comment.utils.RetrofitUtils
import com.fengwo.module_login.api.LoginApiService
import com.fengwo.module_login.mvp.dto.GiftWallDto
import com.fengwo.module_login.mvp.dto.MyCarDto
import com.fengwo.module_login.mvp.ui.iview.IMineIndexView
import com.fengwo.module_login.utils.UserManager

class MineIndexPresenter : BaseLoginPresenter<IMineIndexView>() {


    /**
     * 获取历史观看记录
     */
    @SuppressLint("CheckResult")
    fun getWatchHistory() {
        service.getWatchHistory("1,3")
                .compose(io_main())
                .subscribe({
                    view.reSetRefresh()
                    if (it.isSuccess) {
                        view.onWatchHistory(it.data)
                    }
                }, {
                    view.reSetRefresh()
                })
    }

    /**
     * 跟新用户信息
     */
    fun updateUser() {
        UserManager.getInstance().updateUser(object : LoadingObserver<UserInfo?>() {
            override fun _onNext(data: UserInfo?) {
                data?.apply {
                    view.updateUser(this)
                }
            }

            override fun _onError(msg: String) {
                view.reSetRefresh()
                if (!TextUtils.isEmpty(msg) && msg.contains("重新登录")) {
                    view.tokenIInvalid()
                }
            }
        })
    }


    /**
     * 获取我的座驾
     */
    @SuppressLint("CheckResult")
    fun getCar() {
        service.myCarList
                .compose<HttpResult<MyCarDto>>(io_main())
                .subscribe({
                    view.reSetRefresh()
                    if (it.isSuccess) {
                        view.onCar(it.data)
                    }
                }, {
                    view.reSetRefresh()
                })
    }


    /**
     *
     * 我的礼物
     *
     */
    @SuppressLint("CheckResult")
    fun getGiftWall() {
        val params = "1,3"
        val userId = UserManager.getInstance().user.id
        service.getGiftWall(params, userId).compose(io_main()).subscribe({
            view.reSetRefresh()
            if (it.isSuccess) {
                view.onGiftWall(it.data)
            }
        }, {
            view.reSetRefresh()
        })
    }

    /**
     * 获取达人礼物
     */
    fun getExpertGiftWall() {
        val userId = UserManager.getInstance().user.id
        val wenboApi = RetrofitUtils().createWenboApi(LoginApiService::class.java)
        val params = com.fengwo.module_comment.base.WenboParamsBuilder()
                .put("current", "0")
                .put("userId",  "$userId")
                .put("size", "3")
                .build()
        addNet(wenboApi.getExpertGiftWall(params).compose(io_main())
                .subscribe({
                    view.reSetRefresh()
                    if (it.isSuccess) {
                        view.onGiftWall(it.data)
                    }
                },{
                    view.reSetRefresh()
                })
        )
    }


}