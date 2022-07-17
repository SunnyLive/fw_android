package com.fengwo.module_comment.ext

import com.fengwo.module_comment.base.BaseApplication
import com.fengwo.module_comment.utils.ToastUtils

/**
 * File FormExt.kt
 * Date 2020/12/7
 * Author lucas
 * Introduction  表单验证相关的扩展函数
 */


fun Map<String,Boolean>.checkForm(block:()->Unit){
    forEach {
        if (it.value){
            ToastUtils.showShort(BaseApplication.mApp,it.key)
            return
        }
        if (keys.last() == it.key)
            block.invoke()
    }
}