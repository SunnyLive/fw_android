package com.fengwo.module_vedio.mvp.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_vedio.R;

import razerdp.basepopup.BasePopupWindow;

public class SharePopupWindow extends BasePopupWindow {
    public SharePopupWindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.vedio_popwindow_share);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }
}
