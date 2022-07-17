package com.fengwo.module_live_vedio.helper

import android.util.LruCache
import com.fengwo.module_comment.base.BaseApplication
import com.fengwo.module_comment.ext.log
import com.fengwo.module_comment.utils.SPUtils1
import com.fengwo.module_live_vedio.helper.bean.NoticeBean
import com.fengwo.module_live_vedio.helper.convert.gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

/**
 * File MsgHelper.kt
 * Date 1/11/21
 * Author lucas
 * Introduction 消息帮助累
 */
object MsgHelper {
     const val QUEUE_TOP = 1//顶部通知

    private const val SP_TAG = "msgIds"
    private var cacheMsgId = LruCache<String, String>(2_000)

    init {
        val json = SPUtils1.get(BaseApplication.mApp, SP_TAG, "") as? String
        val type = object : TypeToken<LruCache<String, String>>() {}.type
        try {
            if (!json.isNullOrEmpty())
                cacheMsgId = gson.fromJson<LruCache<String, String>>(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        "cacheMsgId.size:${cacheMsgId.size()}".log()
    }

    //判断是否是重复消息
    fun isRepeatMsg(msg: String): Boolean {
        try {
            val jsonObject = JSONObject(msg)
            if (!jsonObject.has("msgId")) {
                return false
            }
            val msgId = jsonObject.getString("msgId")
            val isRepeat = cacheMsgId[msgId] != null
            if (!isRepeat) {
                cacheMsgId.put(msgId, msg)
                //数据持久化
                val json = gson.toJson(cacheMsgId)
                SPUtils1.put(BaseApplication.mApp, SP_TAG, json)
            }
            return isRepeat
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false

    }
}