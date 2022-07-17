package com.fengwo.module_comment.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;


import com.fengwo.module_comment.utils.KLog;

import java.math.BigDecimal;

/**
 * Created by xcheng on 2016/6/3.
 */
public class ViewSizeChangeAnimation extends Animation {
    int initialHeight;
    int targetHeight;

    View view;

    public ViewSizeChangeAnimation(View view, int targetHeight) {
        this.view = view;
        this.targetHeight = targetHeight;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = initialHeight + (int) ((targetHeight - initialHeight) * interpolatedTime);
      //  KLog.e("tag", "" + view.getLayoutParams().height);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.initialHeight = height;
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}