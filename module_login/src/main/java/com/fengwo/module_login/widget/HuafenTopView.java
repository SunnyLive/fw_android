package com.fengwo.module_login.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fengwo.module_login.R;

public class HuafenTopView extends LinearLayout {

    private int startColor;
    private int endColor;
    private Paint mPaint;

    public HuafenTopView(Context context) {
        this(context, null);
    }

    public HuafenTopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HuafenTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.huafen_top);
        startColor = ta.getColor(R.styleable.huafen_top_startcolor, ContextCompat.getColor(context, R.color.purple_9B7CF1));
        endColor = ta.getColor(R.styleable.huafen_top_endcolor, ContextCompat.getColor(context, R.color.pink_E65BFF));
    }

    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        LinearGradient linearGradient = new LinearGradient(
                0, getMeasuredHeight(), 0, 0
                , new int[]{startColor, endColor}
                , new float[]{0, 1f}, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        RectF rf = new RectF();
        rf.left = 0;
        rf.top = 0;
        rf.right = getMeasuredWidth();
        rf.bottom = getMeasuredHeight();
        canvas.drawRoundRect(rf, 0, 0, mPaint);


        super.dispatchDraw(canvas);
    }
}
