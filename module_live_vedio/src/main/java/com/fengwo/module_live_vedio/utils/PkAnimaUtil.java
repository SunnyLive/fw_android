package com.fengwo.module_live_vedio.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.fengwo.module_comment.utils.L;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/18
 */
public class PkAnimaUtil {
    AnimatorSet scanAnimatorSet;
    TranslateAnimation translateAnimation;

    public interface MAnimatorListener {
        void start();

        void end();
    }

    public void cancle() {
        try {
            if(null!=scanAnimatorSet){
                scanAnimatorSet.cancel();
            }
            if(null!=translateAnimation){
                translateAnimation.cancel();
            }
            scanAnimatorSet = null;
            translateAnimation = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(ImageView weStartView, ImageView weEndView, ImageView otherStartView, ImageView otherEndView, MAnimatorListener l) {
        scanAnimatorSet = new AnimatorSet();
        ObjectAnimator scaleWcX = new ObjectAnimator().ofFloat(weStartView, "scaleX", 1f, 12f, 6.25f);
        ObjectAnimator scaleWeY = new ObjectAnimator().ofFloat(weStartView, "scaleY", 1f, 12f, 6.25f);
        ObjectAnimator scaleOtherX = new ObjectAnimator().ofFloat(otherStartView, "scaleX", 1f, 12f, 6.25f);
        ObjectAnimator scaleOtherY = new ObjectAnimator().ofFloat(otherStartView, "scaleY", 1f, 12f, 6.25f);
        scanAnimatorSet.playTogether(scaleWcX, scaleWeY, scaleOtherX, scaleOtherY);
        scanAnimatorSet.setDuration(2000);
        scanAnimatorSet.start();
        scanAnimatorSet.playSequentially();
        scanAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                translate(weStartView, weEndView, l);
                translate(otherStartView, otherEndView, l);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    public void start(ImageView weStartView,  int x,int y, MAnimatorListener l) {
        scanAnimatorSet = new AnimatorSet();
        ObjectAnimator scaleWcX = new ObjectAnimator().ofFloat(weStartView, "scaleX", 1f, 2f, 0.5f);
        ObjectAnimator scaleWeY = new ObjectAnimator().ofFloat(weStartView, "scaleY", 1f, 2f, 0.5f);

        scanAnimatorSet.playTogether(scaleWcX, scaleWeY);
        scanAnimatorSet.setDuration(2000);
        scanAnimatorSet.start();
        scanAnimatorSet.playSequentially();
        scanAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                translate(weStartView, x,y, l);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void translate(ImageView startView, int x,int y, MAnimatorListener listener) {
        int[] startLoc = new int[2];
        startView.getLocationInWindow(startLoc);
        int[] endLoc = new int[2];
        endLoc[0] = x;
        endLoc[1] = y;
        //获得开始点坐标
        float startX = startLoc[0] + startView.getWidth() / 2;
        float startY = startLoc[1] + startView.getHeight() / 2;
        L.e("lgl", "startX:" + startX + "\n" + "startY:" + startY);
        //获得结束点坐标
        float toX = endLoc[0] + 50 / 2;
        float toY = endLoc[1] + 50 / 2;
        L.e("lgl", "toX:" + toX + "\n" + "toY:" + toY);

        if (toX - startX > 0) {//往右移动
            translateAnimation = new TranslateAnimation(0, toX - startX , 0, toY - startY);
        } else {
            translateAnimation = new TranslateAnimation(0, toX - startX , 0, toY - startY);
        }

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startView.setVisibility(View.GONE);
                if (null != listener)
                    listener.end();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translateAnimation.setDuration(1000);
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        startView.startAnimation(translateAnimation);



    }
    public void translate(ImageView startView, View endView, MAnimatorListener listener) {
        int[] startLoc = new int[2];
        startView.getLocationInWindow(startLoc);
        int[] endLoc = new int[2];
        endView.getLocationInWindow(endLoc);
        //获得开始点坐标
        float startX = startLoc[0] + startView.getWidth() / 2;
        float startY = startLoc[1] + startView.getHeight() / 2;
        L.e("lgl", "startX:" + startX + "\n" + "startY:" + startY);
        //获得结束点坐标
        float toX = endLoc[0] + endView.getWidth() / 2;
        float toY = endLoc[1] + endView.getHeight() / 2;
        L.e("lgl", "toX:" + toX + "\n" + "toY:" + toY);


        if (toX - startX > 0) {//往右移动
            translateAnimation = new TranslateAnimation(0, toX - startX - startView.getWidth() * 3, 0, toY - startY);
        } else {
            translateAnimation = new TranslateAnimation(0, toX - startX - startView.getWidth() * 2, 0, toY - startY);
        }
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (null != listener)
                    listener.end();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translateAnimation.setDuration(1000);
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        startView.startAnimation(translateAnimation);
    }

}
