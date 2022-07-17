package com.fengwo.module_flirt.widget.starrysky;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class OvalEvaluator implements TypeEvaluator<PointF> {
    /**
     * 椭圆的圆点
     */
    private final PointF centerPoint;

    /**
     * 椭圆x上的轴
     */
    private final int a;

    /**
     * 椭圆y上的轴
     */
    private final int b;

    public OvalEvaluator(PointF centerPoint, int a, int b) {
        this.centerPoint = centerPoint;
        this.a = a;
        this.b = b;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x, y;
        if (fraction <= 0.25) {
            x = centerPoint.x + fraction * 4 * a;
            y = (float) (b * Math.sqrt(1 - x * x / a * a));
        } else if (fraction <= 0.5) {
            x = centerPoint.x + a * (1 - (fraction - 0.25F) * 4F);
            y = (float) (-b * Math.sqrt(1 - x * x / a * a));
        } else if (fraction <= 0.75) {
            x = centerPoint.x - (fraction - 0.5F) * 4 * a;
            y = (float) (-b * Math.sqrt(1 - x * x / a * a));
        } else {
            x = centerPoint.x - a * (1 - (fraction - 0.75F) * 4F);
            y = (float) (b * Math.sqrt(1 - x * x / a * a));
        }
        return new PointF(x, y);
    }
}
