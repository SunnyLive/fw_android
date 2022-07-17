package com.fengwo.module_live_vedio.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class ViewGosn {
    private TranslateAnimation mHiddenAction;
    private TranslateAnimation mShowAction;

    /**
     * @param gosn true 显示
     * @param view
     */
    public void Gosn(boolean gosn, View view) {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(600);
        AnimationSet aset = new AnimationSet(true);
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(400);
        // aset.addAnimation(aa);
        aset.addAnimation(mShowAction);
        AnimationSet asets = new AnimationSet(true);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(600);
        asets.addAnimation(aa);
        asets.addAnimation(mHiddenAction);
        if (gosn) {
            view.startAnimation(aset);
            view.setVisibility(View.VISIBLE);
        } else {
            view.startAnimation(asets);
            view.setVisibility(View.INVISIBLE);
        }

    }

    public void paomadeng(final View view) {
        view.setVisibility(View.VISIBLE);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 10.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(10000);
        AnimationSet aset = new AnimationSet(true);
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(400);
        aset.addAnimation(mHiddenAction);
        view.startAnimation(aset);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });
    }

    public void XGosn(final Context context, View view, final View views) {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mShowAction.setDuration(400);
        AnimationSet aset = new AnimationSet(true);
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(200);
        // aset.addAnimation(aa);
        aset.addAnimation(mShowAction);
        view.startAnimation(aset);
        view.setVisibility(View.INVISIBLE);




//        AnimationSet animationSet = new AnimationSet(true);
//        AlphaAnimation animations = new AlphaAnimation(0.1f, 1f);
//        animations.setDuration(900);
//        animations.setFillAfter(true);
//        animationSet.addAnimation(animations);

        AlphaAnimation   appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(1000);
        views.startAnimation(appearAnimation);
        views.setVisibility(View.VISIBLE);



    }
}
