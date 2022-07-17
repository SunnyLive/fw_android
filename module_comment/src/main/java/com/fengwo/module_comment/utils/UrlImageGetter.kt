package com.fengwo.module_comment.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import okhttp3.*
import java.io.IOException
import java.util.*

/**
 * @author
 * @version 1.0
 * @Project ShanDui
 * @date 2020/6/23  17:09
 */
class UrlImageGetter(var mContext: Context) : Html.ImageGetter {
    lateinit var mydrawable: Drawable;

    override fun getDrawable(source: String): Drawable? {
        try {
            //请求获取图片
            val request = Request.Builder().url(source).build()
            val response = OkHttpClient().newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
                    var drawable: Drawable = BitmapDrawable(mContext.resources, bitmap)
                    //调整图片大小
                    val drawableUtil = DrawableUtilHtml(mContext)
                    mydrawable = drawableUtil.utils(drawable)

                }
            })

            return mydrawable
        } catch (e: IOException) {
            e.printStackTrace()
        }
       return null
    }

}