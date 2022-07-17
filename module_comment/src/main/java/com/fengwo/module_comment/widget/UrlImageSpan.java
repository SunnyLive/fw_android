package com.fengwo.module_comment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;

import java.lang.reflect.Field;

/**
 * 获取网络图片的ImageSpan
 * Created by Yomii on 2016/10/13.
 */
public class UrlImageSpan extends ImageSpan {

    private String url;
    private TextView tv;
    private boolean picShowed;
    private int width;
    private int height;

    public UrlImageSpan(Context context, String url, TextView tv) {
        super(context, R.drawable.ic_span_holder);
        this.url = url;
        KLog.e("tag",url);
        this.tv = tv;
        width = 45;
        height = 45;
    }

    public UrlImageSpan(Context context, String url, TextView tv, int width, int height) {
        super(context, R.drawable.ic_span_holder);
        this.url = url;
        this.tv = tv;
        this.width = width;
        this.height = height;
    }

    @Override
    public Drawable getDrawable() {
        if (!picShowed) {
            ImageLoader.downloadImg(tv.getContext(), url, width, height, new ImageLoader.DownloadBitmapSuccessListener() {
                @Override
                public void success(Bitmap bp) {
                    Resources resources = tv.getContext().getResources();
                    BitmapDrawable b = new BitmapDrawable(resources, bp);
                    b.setBounds(0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight());
                    Field mDrawable;
                    Field mDrawableRef;
                    try {
                        mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
                        mDrawable.setAccessible(true);
                        mDrawable.set(UrlImageSpan.this, b);
                        mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
                        mDrawableRef.setAccessible(true);
                        mDrawableRef.set(UrlImageSpan.this, null);
                        picShowed = true;
                        tv.setText(tv.getText());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return super.getDrawable();
    }

    /**
     * 按宽度缩放图片
     *
     * @param bmp  需要缩放的图片源
     * @param newW 需要缩放成的图片宽度
     * @return 缩放后的图片
     */
    public static Bitmap zoom(@NonNull Bitmap bmp, int newW) {

        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // 计算缩放比例
        float scale = ((float) newW) / width;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        return newbm;
    }
}