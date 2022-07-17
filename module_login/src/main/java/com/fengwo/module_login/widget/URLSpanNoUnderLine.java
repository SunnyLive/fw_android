package com.fengwo.module_login.widget;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class URLSpanNoUnderLine extends URLSpan {

    private View.OnClickListener mOnClickListener;
    private int mTextColor;

    public URLSpanNoUnderLine(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mTextColor);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(widget);
        }
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
