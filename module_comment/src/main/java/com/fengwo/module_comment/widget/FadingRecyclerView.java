package com.fengwo.module_comment.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.utils.DensityUtils;

public class FadingRecyclerView extends RecyclerView {
    /**
     * 只有顶部有阴影
     */
    public static final int MODE_TOP = 1;
    /**
     * 只有底部有阴影
     */
    public static final int MODE_BOTTOM = 2;
    /**
     * 顶部和底部有阴影
     */
    public static final int MODE_TOP_AND_BOTTOM = 3;

    private Paint paint;
    private int height;
    private int width;
    private int spanPixel = 100;
    //默认只有顶部阴影
    private int fadingMode = MODE_TOP;

    public FadingRecyclerView(Context context) {
        super(context);
        init();
    }

    public FadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        spanPixel = DensityUtils.dp2px(getContext(), 40);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    public void setSpanPixel(int spanPixel) {
        this.spanPixel = spanPixel;
    }

    public void setFadingMode(int fadingMode) {
        this.fadingMode = fadingMode;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        LinearGradient linearGradient;
        switch (fadingMode) {
            case MODE_TOP:
                linearGradient = new LinearGradient(0, 0, 0, height,
                        new int[]{0x00000000, 0xff000000, 0xff000000}, new float[]{0, (float) spanPixel / height, 1f}, Shader.TileMode.MIRROR);
                break;
            case MODE_BOTTOM:
                linearGradient = new LinearGradient(0, 0, 0, height,
                        new int[]{0xff000000, 0xff000000, 0x00000000}, new float[]{0, 1F - (float) spanPixel / height, 1f}, Shader.TileMode.MIRROR);
                break;
            case MODE_TOP_AND_BOTTOM:
                linearGradient = new LinearGradient(0, 0, 0, height / 2F,
                        new int[]{0x00000000, 0xff000000, 0xff000000}, new float[]{0, spanPixel / (height / 2F), 1f}, Shader.TileMode.MIRROR);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + fadingMode);
        }
        paint.setShader(linearGradient);
    }

    @Override
    public void draw(Canvas c) {
        if(height==0||width==0){
            super.draw(c);
            return;
        }
        c.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        super.draw(c);

        c.drawRect(0, 0, width, height, paint);
        c.restore();
    }
}
