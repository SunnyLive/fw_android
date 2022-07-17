package com.fengwo.module_comment.ext

import android.util.Log
import com.fengwo.module_comment.BuildConfig

/**
 * @package    com.luan.core.ext
 * @author     luan
 * @date       2020/9/15
 * @des        日志
 */
private val TAG = "lucas"

fun Any?.log() {
    var s = this?.toString() ?: "null"
    if (BuildConfig.DEBUG) {
       s = linkSource().plus(s)
    }
    Log.d(TAG, s)
}

fun Any?.logE() {
    var s = this?.toString() ?: "null"
    if (BuildConfig.DEBUG) {
        s = linkSource().plus(s)
    }
    Log.e(TAG, s)
}

fun linkSource(): String {
    val element = Thread.currentThread().stackTrace[4]
    return element.methodName.plus("(").plus(element.fileName).plus(":").plus(element.lineNumber).plus(")")
}