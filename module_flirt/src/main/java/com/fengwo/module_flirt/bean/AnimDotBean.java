package com.fengwo.module_flirt.bean;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.DimenRes;

public class AnimDotBean {

    private int dotR;
    private int[] dotColorStatus;
    private Paint mDotPaint;

    public int getDotR() {
        return dotR;
    }

    public AnimDotBean(Context context, @DimenRes int r, int colorStatus) {
        dotR = context.getResources().getDimensionPixelOffset(r);
        TypedArray typedArray = context.getResources().obtainTypedArray(colorStatus);
        dotColorStatus = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            dotColorStatus[i] = context.getResources().getColor(typedArray.getResourceId(i, 0));
        }
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);
    }


    private int currentStatus;

    public void update(Canvas canvas) {
        mDotPaint.setColor(dotColorStatus[currentStatus % dotColorStatus.length]);
        canvas.drawCircle(0, 0, dotR-3, mDotPaint);
        currentStatus++;
    }
}
