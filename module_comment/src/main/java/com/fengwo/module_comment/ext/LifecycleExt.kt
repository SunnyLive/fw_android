package com.fengwo.module_comment.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * File LifecycleExt.kt
 * Date 12/29/20
 * Author lucas
 * Introduction lifecycle扩展
 */
typealias block = () -> Unit
typealias blockBool = () -> Boolean
typealias blockOrNull = (() -> Unit)?

/**
 * 绑定制定生命周期
 *
 * @param e 事件类型
 * @param blockBool 返回值true 移除监听
 */
fun Lifecycle.onEvent(e: Lifecycle.Event, blockBool: blockBool) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == e) {
                if (blockBool.invoke()) {
                    removeObserver(this)
                }
            }
        }
    })
}

/**
 * 绑定生命周期
 *
 * @param e
 * @param blockBool
 */
fun Lifecycle.onAllEvent(blockBool: (Lifecycle.Event)->Boolean) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (blockBool.invoke(event)) {
                removeObserver(this)
            }
        }
    })
}

/**
 * 监听回调事件
 *
 * @param block 回调
 */
fun Lifecycle.onDestroy(block: block) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                removeObserver(this)
                block.invoke()
            }
        }
    })
}