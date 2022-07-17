package com.fengwo.module_comment.ext

import java.text.SimpleDateFormat
import java.util.*


/**
 * File TimeExt.kt
 * Date 2020/12/8
 * Author lucas
 * Introduction 时间转换工具
 */

fun Long.formatTime():String{
    val simpleDateFormat = SimpleDateFormat.getInstance() as SimpleDateFormat
    simpleDateFormat.applyPattern("aa HH:mm")
    return simpleDateFormat.format(Date(this))
}