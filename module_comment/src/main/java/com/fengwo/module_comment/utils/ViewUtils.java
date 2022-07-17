package com.fengwo.module_comment.utils;

import android.view.View;

/**
 * @author Zachary
 * @date 2019/12/25
 */
public class ViewUtils {
    public static void throttleClick(View view, View.OnClickListener listener) {
        throttleClick(view, 1000, listener);
    }

    public static void throttleClick(View view, long minMillis, View.OnClickListener listener) {
        view.setOnClickListener(new View.OnClickListener() {

            private long lastClickMillis = -1;

            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - lastClickMillis >= minMillis) {
                    lastClickMillis = currentTimeMillis;
                    listener.onClick(view);
                }
            }
        });
    }
}
