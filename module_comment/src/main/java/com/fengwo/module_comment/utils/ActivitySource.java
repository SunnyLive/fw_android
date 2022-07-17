package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * @anchor Administrator
 * @date 2021/1/9
 */
public class ActivitySource  extends Source {

    private Activity mActivity;

    public ActivitySource(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean isShowRationalePermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        return mActivity.shouldShowRequestPermissionRationale(permission);
    }
}
