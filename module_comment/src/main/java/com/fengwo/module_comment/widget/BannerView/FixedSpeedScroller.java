package com.fengwo.module_comment.widget.BannerView;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @anchor Administrator
 * @date 2021/1/28
 */
public class FixedSpeedScroller  extends Scroller {

    private int mDuration = 1000;//这里是定义切换的时长

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator,
                              boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

}