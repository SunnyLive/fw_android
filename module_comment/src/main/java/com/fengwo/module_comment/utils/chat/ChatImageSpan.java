package com.fengwo.module_comment.utils.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

import com.fengwo.module_comment.utils.DensityUtils;

public class ChatImageSpan extends ImageSpan {

    private final int imageSize;

    public ChatImageSpan(Context context, final int drawableRes) {
        super(context, drawableRes);
        imageSize = DensityUtils.dp2px(context, 20);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        drawable.setBounds(0, 0, imageSize, imageSize);
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            //对于这里我表示,我不知道为啥是这样。不应该是fontHeight/2?但是只有fontHeight/4才能对齐
            //难道是因为TextView的draw的时候top和bottom是大于实际的？具体请看下图
            //所以fontHeight/4是去除偏差?
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // image to draw
        Drawable b = getDrawable();
        b.setBounds(0, 0, imageSize, imageSize);
        int transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}