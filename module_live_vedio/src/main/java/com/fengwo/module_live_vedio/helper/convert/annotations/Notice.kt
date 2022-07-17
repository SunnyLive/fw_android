package com.fengwo.module_live_vedio.helper.convert.annotations

/**
 * File EventId.kt
 * Date 1/13/21
 * Author lucas
 * Introduction 标记事件ID
 * @property eventId 事件ID
 * @property scope 作用域
 * @property priority 一级优先级 值越大优先级越高
 */
annotation class Notice(val eventId: Int, val scope: Scope = Scope.SINGLE, val priority: Int = 0) {
    enum class Scope {
        //作用域
        GLOBAL,//全局
        SINGLE//指定的view
    }
}


