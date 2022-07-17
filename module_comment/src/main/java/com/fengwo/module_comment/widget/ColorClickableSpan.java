package com.fengwo.module_comment.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class ColorClickableSpan extends ClickableSpan {

    private Context mContext;
    private String mColor;

    public ColorClickableSpan(Context context, String color) {
        mContext = context;
        mColor = color;
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor(mColor));
    }
}