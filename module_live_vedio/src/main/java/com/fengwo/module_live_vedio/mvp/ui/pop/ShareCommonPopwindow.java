package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class ShareCommonPopwindow extends BasePopupWindow {

    public ShareCommonPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_activity_share);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @OnClick({R2.id.ll_wx, R2.id.ll_circle})
    public void onViewClicked(View view) {
        if (!CommentUtils.isWeixinAvilible(getContext())) {
            ToastUtils.showShort(getContext(), "您没有安装微信！！！");
            return;
        }
        int id = view.getId();
        if (id == R.id.ll_wx) {
            if (onShareClickListener != null)
                onShareClickListener.onWx();
        } else if (id == R.id.ll_circle) {
            if (onShareClickListener != null)
                onShareClickListener.onWxCircle();
        }
    }

    public OnShareClickListener onShareClickListener;

    public interface OnShareClickListener {
        void onWx();

        void onWxCircle();
    }

    public void addClickListener(OnShareClickListener listener) {
        onShareClickListener = listener;
    }
}
