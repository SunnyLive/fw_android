package com.fengwo.module_comment.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File

/**
 * File GlideExt.kt
 * Date 12/29/20
 * Author lucas
 * Introduction 图片加载扩展
 */
fun ImageView.loadGif(res: Any, animationCallback: Animatable2Compat.AnimationCallback? = null) {
    if (res is String || res is Int || res is Bitmap || res is Drawable || res is File) {
        Glide.with(this)
                .asGif()
                .load(res)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .addListener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean = false

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        resource?.also {
                            resource.setLoopCount(1)
                            if (animationCallback != null)
                                resource.registerAnimationCallback(animationCallback)
                        }
                        return false
                    }
                }).into(this)
    }
}