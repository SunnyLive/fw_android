package com.fengwo.module_comment.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

public class RenderScriptGlassBlur {

    /**
     * radius 1-25为模糊度
     *
     * @param context
     * @param originBitmap
     * @param radius
     * @return
     */
    public static Bitmap handleGlassblur(Context context, Bitmap originBitmap, int radius) {
        RenderScript renderScript = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(renderScript, originBitmap);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(originBitmap);

        return originBitmap;
    }

    /**
     * 高斯模糊
     *
     * @param context
     * @param ivBlurBg
     * @param bitmap   param scaleRatio  bitmap分辨率缩小比例，计算速度更快
     */
    public static void bitmapBlur(Context context, ImageView ivBlurBg, Bitmap bitmap, int scaleRatio) {
        if (bitmap == null)
            return;
        int x = (int) ivBlurBg.getX();
        int y = (int) ivBlurBg.getY();
        int bitmapX = bitmap.getWidth();
        int bitmapY = bitmap.getHeight();
        Bitmap bitmapNew = Bitmap.createBitmap(bitmap, x, y, bitmapX - x, bitmapY - y);

        if (bitmap != null) {
            Bitmap overlay = Bitmap.createScaledBitmap(bitmapNew, bitmapNew.getWidth() / scaleRatio, bitmapNew.getHeight() / scaleRatio, false);
            overlay = RenderScriptGlassBlur.handleGlassblur(context, overlay, 25);
            ivBlurBg.setImageBitmap(overlay);
//            overlay.recycle();//导致效果失效
        }
        bitmap.recycle();
    }

}

