package com.fengwo.module_comment.ext

/**
 * File CommentExt.kt
 * Date 1/14/21
 * Author lucas
 * Introduction 通用扩展
 */
fun <K, V> HashMap<K, V>.find(block: (Map.Entry<K, V>) -> Boolean): Map.Entry<K, V>? {
    forEach {
        if (block(it)) return it
    }
    return null
}