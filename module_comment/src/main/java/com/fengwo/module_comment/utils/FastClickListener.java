package com.fengwo.module_comment.utils;

import android.view.View;

public abstract class FastClickListener implements View.OnClickListener {
    private static final long DOUBLE_TIME = 1000;
    private static long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > DOUBLE_TIME) {
            onNoFastClick(v);
        }
        lastClickTime = currentTimeMillis;
    }

    public abstract void onNoFastClick(View v);
}