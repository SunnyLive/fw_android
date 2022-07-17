package com.fengwo.module_comment.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fengwo.module_comment.R;


/**
 * 加载进度提示框
 */
public class LoadingProgressDialog extends Dialog {
    private RoundProgressBar progressbar;
    private Context mContext;


    public LoadingProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(mContext).inflate(R.layout.loading_progress_dialog, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(v, params);// 设置布局 );

        progressbar = (RoundProgressBar) findViewById(R.id.progressbar);
        progressbar.setCircleColor(0xffD1D1D1);//设置圆环的颜色
        progressbar.setCircleProgressColor(0xff000000);//设置圆环进度的颜色
        progressbar.setTextColor(0xff9A32CD);//设置中间进度百分比的字符串的颜色
        progressbar.setRoundWidth(10);//设置圆环的宽度
        progressbar.setTextSize(18);//设置中间进度百分比的字符串的字体大小
        this.setCanceledOnTouchOutside(false);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#60000000"));
        getWindow().setBackgroundDrawable(colorDrawable);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void show() {
        if (mContext != null)
            super.show();
    }

    public void setProgressbarMax(long max) {
        progressbar.setMax((int) max);
    }

    public void setProgress(long cur) {
        progressbar.setProgress((int) cur);
    }

    @Override
    public void dismiss() {
        if (mContext != null)
            super.dismiss();
    }

    public void destory() {
        mContext = null;
    }
}