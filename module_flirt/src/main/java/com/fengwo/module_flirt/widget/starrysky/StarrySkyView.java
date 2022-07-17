package com.fengwo.module_flirt.widget.starrysky;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 根据椭圆画路径
 * 椭圆方程 x^2/a^2+y^2/b^2=1
 */
public class StarrySkyView extends FrameLayout {


    /**
     * 椭圆长轴
     */
    private int a = 800;
    /**
     * 椭圆短轴
     */
    private int b = 400;

    /**
     * 所有子view
     */
    private List<View> childViewList = new ArrayList<>();

    /**
     * layout的宽
     */
    private int width;
    /**
     * layout的高
     */
    private int height;

    /**
     * 中心点
     */
    private PointF centerPoint;

    private Random mRandom;

    public StarrySkyView(@NonNull Context context) {
        this(context, null);
    }

    public StarrySkyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarrySkyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredWidth();
        centerPoint = new PointF(width >> 1, height >> 1);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    private void startAnim() {
        for (int i = 0; i < childViewList.size(); i++) {
            View childView = childViewList.get(i);
            ValueAnimator anim = ValueAnimator.ofObject(new OvalEvaluator(centerPoint, mRandom.nextInt(width / 2), mRandom.nextInt(height / 2)), 0, 1F);
            anim.setDuration(mRandom.nextInt(5000));
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    PointF point = (PointF) animation.getAnimatedValue();
                    childView.setX(point.x);
                    childView.setY(point.y);
                }
            });
        }
    }

    public void setChildViewList(List<View> childViewList) {
        this.childViewList.clear();
        removeAllViews();
        this.childViewList.addAll(childViewList);
        for (int i = 0; i < childViewList.size(); i++) {
            addView(childViewList.get(i));
        }
    }
}
