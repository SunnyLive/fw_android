package com.fengwo.module_live_vedio.mvp.presenter.redpackage

import com.fengwo.module_comment.base.BasePresenter
import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_comment.utils.RetrofitUtils
import com.fengwo.module_live_vedio.api.RedPackageApiService

/**
 * 发红包基础类
 *
 * @Author gukaihong
 * @Time 2021/1/6
 */
open class BaseRedPackagePresenter<T : MvpView> : BasePresenter<T>() {
    val service: RedPackageApiService = RetrofitUtils().createApi(RedPackageApiService::class.java)
    val serviceEncryption: RedPackageApiService = RetrofitUtils().createWenboApi(RedPackageApiService::class.java)//加密
}