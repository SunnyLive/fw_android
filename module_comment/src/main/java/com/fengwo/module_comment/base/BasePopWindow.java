package com.fengwo.module_comment.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by 科技 on 2016-04-06.
 */
public abstract class BasePopWindow extends PopupWindow {
    protected LayoutInflater mInflater;
    protected Context mContext;

    public BasePopWindow(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setContentView(initView());
        initWindow();
    }

    public void initWindow() {
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                windowAlpha(mContext, 1f);
            }
        });
    }

    public void windowAlpha(Context context, float alpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        windowAlpha(mContext, 0.4f);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        windowAlpha(mContext, 0.4f);
        super.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAsDropDown(View v) {
        super.showAsDropDown(v);
    }

    protected abstract View initView();

}
