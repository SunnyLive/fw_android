package com.fengwo.module_comment.ext

import java.lang.StringBuilder

/**
 * File TransfromExt.kt
 * Date 12/29/20
 * Author lucas
 * Introduction 显示格式转换相关扩展
 */
/**
 * 金额格式化 fixme 函数算法有点问题
 *  eg:21,346
 */
fun Long.formatMoney(): String {
    val unit = 1000
    if (this < unit) return this.toString()
    var num = this
    val result = StringBuilder()
    while (num % unit > 0) {
        val other = num % unit
        num -= other
        num /= unit
        if (result.isNotEmpty())
            result.insert(0, ",")
        if (num>1000){
            //补零
            if (other<10){
                result.insert(0, "00".plus(other))
            }else if (other<100){
                result.insert(0, "0".plus(other))
            }else{
                result.insert(0,other)
            }
        }else{
            result.insert(0,other)
        }
    }
    return result.toString()
}