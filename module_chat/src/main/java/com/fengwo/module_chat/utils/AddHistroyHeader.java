/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.fengwo.module_chat.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fengwo.module_chat.R;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * WaterDropHeader
 * Created by SCWANG on 2017/5/31.
 * from https://github.com/THEONE10211024/WaterDropListView
 */
public class AddHistroyHeader extends LinearLayout implements RefreshHeader {

    private  ProgressBar progressBar;

    public AddHistroyHeader(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        setBackgroundColor(context.getResources().getColor(R.color.transparent));
        progressBar = new ProgressBar(context);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress_bar));
        addView(progressBar);
    }

    public AddHistroyHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddHistroyHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
                progressBar.setVisibility(GONE);
            case PullDownToRefresh:
                progressBar.setVisibility(VISIBLE);
                break;
        }
    }

}