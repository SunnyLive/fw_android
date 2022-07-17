package com.fengwo.module_login.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.fengwo.module_login.R;

public class DotLineView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private float dotWidth;     // 点的长度

    public DotLineView(Context context) {
        this(context, null);
    }

    public DotLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DotLineView);
        int dotColor = ta.getColor(R.styleable.DotLineView_dot_color, getResources().getColor(R.color.normal_bg));
        dotWidth = ta.getDimensionPixelSize(R.styleable.DotLineView_dot_width, 6);
        ta.recycle();

        paint.setColor(dotColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 判断点的高度
        float dotHeight = Math.min(dotWidth / 2, getHeight() / 4);
        // 画边上的半圆
        rect.setEmpty();
        rect.set(-getHeight() / 2, 1, getHeight() / 2, getHeight() - 1);
        canvas.drawArc(rect, -90, 180, true, paint);
        rect.setEmpty();
        rect.set(getWidth() - getHeight() / 2, 1, getWidth() + getHeight() / 2, getHeight() - 1);
        canvas.drawArc(rect, 90, 180, true, paint);
        // 画点
        int count = (int) ((getWidth() - getHeight()) / (dotWidth * 2));
        int startX = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            rect.setEmpty();
            rect.set(startX + (i + 1) * dotWidth * 2 - dotWidth,
                    (getHeight() - dotHeight) / 2,
                    startX + (i + 1) * dotWidth * 2,
                    (getHeight() + dotHeight) / 2);
            canvas.drawRect(rect, paint);
        }
    }
}