package com.fengwo.module_comment.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_comment.R;

import razerdp.basepopup.BasePopupWindow;

public class LogoutPopwindow extends BasePopupWindow implements View.OnClickListener {

    private View btnLogout,  btnCancle;
    OnLogoutClickListener l;

    public void setOnLogoutListener(OnLogoutClickListener l) {
        this.l = l;
    }

    public interface OnLogoutClickListener {
        void onLogout();

    }


    public LogoutPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        btnLogout = findViewById(R.id.btn_logout);
        btnCancle = findViewById(R.id.btn_cancle);
        btnLogout.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_logout);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_logout) {
            if (null != l) {
                l.onLogout();
            }
            dismiss();
        } else if (id == R.id.btn_cancle) {
            dismiss();
        }
    }
}
