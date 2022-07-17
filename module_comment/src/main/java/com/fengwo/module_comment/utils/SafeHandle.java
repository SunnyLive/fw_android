package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017-03-08.
 */

/**
 * 避免造成内存泄漏的 handler
 */
public class SafeHandle extends Handler {
    private final WeakReference<Activity> mActivity;

    public SafeHandle(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    protected void mHandleMessage(Message msg) {

    }

    @Override
    public void handleMessage(Message msg) {
        try {
            if (mActivity.get() == null) {
                return;
            }
            mHandleMessage(msg);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
