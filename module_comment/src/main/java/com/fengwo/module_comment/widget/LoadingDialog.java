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
import android.widget.TextView;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.utils.UploadHelper;


/**
 * 加载进度提示框
 */
public class LoadingDialog extends Dialog {
    private ImageView loadingCircle;
    private Animation animation;
    private Context mContext;
    private TextView tvPercent;

    public LoadingDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(mContext).inflate(R.layout.loading_dialog, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(v, params);// 设置布局 );

        loadingCircle = (ImageView) findViewById(R.id.loading_circle);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.loading_rotate);
        tvPercent = findViewById(R.id.tv_progress);
        this.setCanceledOnTouchOutside(false);
        this.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (animation != null) {
                    loadingCircle.startAnimation(animation);
                }
            }
        });
        this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (animation != null) {
                    animation.cancel();
                    UploadHelper.getInstance(BaseApplication.mApp).cancleUpLoad();
                }
            }
        });
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

    @Override
    public void dismiss() {
        if (mContext != null)
            super.dismiss();
    }

    public void destory() {
        mContext = null;
    }

    public void setProgressPercent(String percent) {
        if (null != tvPercent)
            tvPercent.setText(percent);
    }
}