package com.fengwo.module_comment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.fengwo.module_comment.R;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

public class RenderBlurDialog extends BasePopupWindow {

    private ImageView ivBg;
    private Context context;
    private TXCloudVideoView txCloudVideoView;
    private Unbinder bind;
    private int time = 0;

    @SuppressLint("HandlerLeak")
    private SafeHandle handler = new SafeHandle(getContext()) {
        @Override
        public void handleMessage(Message msg) {
            if (txCloudVideoView != null && txCloudVideoView.getVideoView() != null && txCloudVideoView.getVideoView().getBitmap() != null && time <= 16)
                RenderScriptGlassBlur.bitmapBlur(context, ivBg, txCloudVideoView.getVideoView().getBitmap(), time == 0 ? 1 : time);//scaleRatio值越大越模糊
            if (time != 16)
                time += 2;
            handler.sendEmptyMessageDelayed(0, 40);
        }
    };

    public RenderBlurDialog(Context context, TXCloudVideoView txCloudVideoView) {
        super(context);
        this.context = context;
        this.txCloudVideoView = txCloudVideoView;
        setPopupGravity(Gravity.CENTER);
        handler.sendEmptyMessageDelayed(0, 40);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_blur);
        ivBg = v.findViewById(R.id.iv_bg);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        if (bind != null) bind.unbind();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 200);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 200);
    }
}
