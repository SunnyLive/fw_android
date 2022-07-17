package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_live_vedio.R;

import razerdp.basepopup.BasePopupWindow;

public class EmptyShouhuPopwindow extends BasePopupWindow {


    public EmptyShouhuPopwindow(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_empty_shouhu);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }
}
