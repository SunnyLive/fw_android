package com.fengwo.module_comment.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.fengwo.module_comment.R;

public class GradientTextView extends AppCompatTextView {

    Paint mPaint;

    private Context mContext;
    private int startColor;
    private int endColor;
    private int angle;
    private float radius ;
    private int textColor;
    GradientDrawable drawable;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        int startColor = ta.getColor(R.styleable.GradientTextView_gtv_start_color, mContext.getResources().getColor(R.color.colorAccent));
        int endColor = ta.getColor(R.styleable.GradientTextView_gtv_end_color, mContext.getResources().getColor(R.color.colorPrimary));
        int mid = ta.getColor(R.styleable.GradientTextView_gtv_mid_color, -1);
        angle = ta.getInt(R.styleable.GradientTextView_gtv_angle, 0);
        textColor = ta.getColor(R.styleable.GradientTextView_gtv_text_color, context.getResources().getColor(R.color.text_white));
        radius = ta.getDimension(R.styleable.GradientTextView_gtv_radius, -1);
        ta.recycle();
        drawable = new GradientDrawable();
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置线性渐变，除此之外还有：GradientDrawable.SWEEP_GRADIENT（扫描式渐变），GradientDrawable.RADIAL_GRADIENT（圆形渐变）
        int[] colors;
        if (mid != -1) {
            colors = new int[]{startColor, mid, endColor};
        } else {
            colors = new int[]{startColor, endColor};
        }
        drawable.setColors(colors);
        drawable.setGradientRadius(angle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (radius != -1) {
            drawable.setCornerRadius(radius);
        } else {
            int radio = getMeasuredHeight() ;
            drawable.setCornerRadius(radio);
        }
        setBackground(drawable);
    }

    public void setColors(int... colors) {
        drawable.setColors(colors);
        postInvalidate();
    }

    public void setStroke(int color, int width) {
        drawable.setStroke(width, color);
        postInvalidate();
    }

    public void setRadio(int radio) {
        drawable.setCornerRadius(radius);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
