package com.fengwo.module_comment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.R;


/**
 * 标签TextView可以设置渐变样式
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/10
 */
public class TagTextView extends AppCompatTextView {

    private static final float STROKE_WIDTH = 3;


    public TagTextView(Context context) {
        this(context, null);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Paint mPaint;

    private int startColor;
    private int endColor;

    private Paint.Style paintStyle = Paint.Style.STROKE;
    private float strokeWidth = STROKE_WIDTH;
    private int radius = -1;

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagTextView);
        startColor = ta.getColor(R.styleable.TagTextView_ttv_start_color, ContextCompat.getColor(context, R.color.purple_9B7CF1));
        endColor = ta.getColor(R.styleable.TagTextView_ttv_start_color, ContextCompat.getColor(context, R.color.pink_E65BFF));
        int style = ta.getInt(R.styleable.TagTextView_background_type, 0);
        if (style == 0) {
            paintStyle = Paint.Style.STROKE;
        } else {
            paintStyle = Paint.Style.FILL;
        }
    }

    public void setRadio(int radius) {
        this.radius = radius;
        invalidate();
    }

    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(paintStyle);
            mPaint.setAntiAlias(true);
        }
    }


    public void setBackGroudStyle(Paint.Style ps) {
        paintStyle = ps;
        mPaint.setStyle(ps);
        invalidate();
    }

    public void setFullColor(@ColorRes int color) {
        setGradientColorRes(color, color);
    }


    public void setGradientColorRes(@ColorRes int sc, @ColorRes int ec) {
        int start = ContextCompat.getColor(getContext(), sc);
        int end = ContextCompat.getColor(getContext(), ec);
        setGradientColor(start, end);
    }

    public void setGradientColor(int sc, int ec) {
        startColor = sc;
        endColor = ec;
        invalidate();
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(strokeWidth);
        LinearGradient linearGradient = new LinearGradient(
                0, 0, getMeasuredWidth(), getMeasuredHeight()
                , new int[]{startColor, endColor}
                , new float[]{0, 1f}, Shader.TileMode.REPEAT);
        mPaint.setShader(linearGradient);
        RectF rf = new RectF();
        rf.left = strokeWidth / 2;
        rf.top = strokeWidth / 2;
        rf.right = getMeasuredWidth() - (strokeWidth / 2);
        rf.bottom = getMeasuredHeight() - (strokeWidth / 2);
        int dp100 = (int) getContext().getResources().getDimension(R.dimen.dp_100);
        this.radius = this.radius > 0 ? this.radius : 100;
        canvas.drawRoundRect(rf, dp100, dp100, mPaint);
        super.onDraw(canvas);
    }
}
