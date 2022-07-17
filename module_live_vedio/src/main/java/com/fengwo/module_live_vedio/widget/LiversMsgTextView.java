package com.fengwo.module_live_vedio.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fengwo.module_live_vedio.R;

/**
 * @anchor Administrator
 * @date 2020/12/9
 */
public class LiversMsgTextView extends TextView {
    private Paint mPaint;
    RectF mRectF;
    private int StrokeWidth = 5;
    public LiversMsgTextView(Context context) {
        super(context);
    }
    public LiversMsgTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LiversMsgTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float dimension = this.getContext().getResources().getDimension(R.dimen.dp_12);
        mPaint = new Paint();
        mRectF = new RectF();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(StrokeWidth);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mRectF.top = StrokeWidth;
        mRectF.left = StrokeWidth+StrokeWidth;
        mRectF.right = getMeasuredWidth()-StrokeWidth ;
        mRectF.bottom = getMeasuredHeight() -StrokeWidth;
        canvas.drawRoundRect(mRectF, dimension, dimension, mPaint);//第二个参数是x半径，第三个参数是y半径
        int startColor = getContext().getResources().getColor(R.color.colorAccent);
        int endColor = getContext().getResources().getColor(R.color.colorPrimary);
        LinearGradient linearGradient = new LinearGradient(
                0, 0, getMeasuredWidth(), getMeasuredHeight()
                , new int[]{startColor, endColor}
                , new float[]{0, 1f}, Shader.TileMode.REPEAT);
        mPaint.setShader(linearGradient);
        canvas.drawRoundRect(mRectF, dimension, dimension, mPaint);//第二个参数是x半径，第三个参数是y半径

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#33FE3C9C"));
        mRectF.top = StrokeWidth;
        mRectF.left = StrokeWidth+StrokeWidth;
        mRectF.right = getMeasuredWidth()-StrokeWidth -StrokeWidth;
        mRectF.bottom = getMeasuredHeight()-StrokeWidth ;
        mPaint.setShader(null);
        canvas.drawRoundRect(mRectF, dimension, dimension, mPaint);//第二个参数是x半径，第三个参数是y半径


    }
}
