package com.fengwo.module_comment.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.R;

public class CircularProgressView extends View {

    private Paint mProgPaint;   // 绘制画笔
    private RectF mRectF;       // 绘制区域
    private int[] mColorArray;  // 圆环渐变色
    private int mProgress;      // 圆环进度(0-100)

    public CircularProgressView(Context context) {
        this(context, null);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);

        // 初始化进度圆环画笔
        mProgPaint = new Paint();
        mProgPaint.setStyle(Paint.Style.STROKE);    // 只描边，不填充
        mProgPaint.setStrokeCap(Paint.Cap.ROUND);   // 设置圆角
        mProgPaint.setAntiAlias(true);              // 设置抗锯齿
        mProgPaint.setDither(true);                 // 设置抖动
        mProgPaint.setStrokeWidth(typedArray.getDimension(R.styleable.CircularProgressView_progWidth, 10));
        mProgPaint.setColor(typedArray.getColor(R.styleable.CircularProgressView_progColor, Color.BLUE));

        // 初始化进度
        mProgress = typedArray.getInteger(R.styleable.CircularProgressView_progress, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWide = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHigh = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int mRectLength = (int) ((viewWide > viewHigh ? viewHigh : viewWide) - mProgPaint.getStrokeWidth());
        int mRectL = getPaddingLeft() + (viewWide - mRectLength) / 2;
        int mRectT = getPaddingTop() + (viewHigh - mRectLength) / 2;
        mRectF = new RectF(mRectL, mRectT, mRectL + mRectLength, mRectT + mRectLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF, 275, -360 * mProgress / 1000F, false, mProgPaint);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * 获取当前进度
     *
     * @return 当前进度（0-1000）
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置当前进度
     *
     * @param progress 当前进度（0-1000）
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    /**
     * 设置进度圆环宽度
     *
     * @param width 进度圆环宽度
     */
    public void setProgWidth(int width) {
        mProgPaint.setStrokeWidth(width);
        invalidate();
    }

    /**
     * 设置进度圆环颜色
     *
     * @param color 景圆环颜色
     */
    public void setProgColor(@ColorRes int color) {
        mProgPaint.setColor(ContextCompat.getColor(getContext(), color));
        mProgPaint.setShader(null);
        invalidate();
    }

    /**
     * 设置进度圆环颜色(支持渐变色)
     *
     * @param colorArray 渐变色集合
     */
    public void setProgColor(@ColorRes int[] colorArray) {
        if (colorArray == null || colorArray.length < 2) return;
        mColorArray = new int[colorArray.length];
        for (int index = 0; index < colorArray.length; index++)
            mColorArray[index] = ContextCompat.getColor(getContext(), colorArray[index]);
        mProgPaint.setShader(new LinearGradient(0, 0, 0, getMeasuredWidth(), mColorArray, null, Shader.TileMode.MIRROR));
        invalidate();
    }
}