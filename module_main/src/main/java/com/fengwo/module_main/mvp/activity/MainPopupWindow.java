package com.fengwo.module_main.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;

import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.StartLiveActivity;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_main.R;

import razerdp.basepopup.BasePopupWindow;

public class MainPopupWindow extends BasePopupWindow {
    private Context mContext;

    public MainPopupWindow(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.main_pup_menu);
        View btnLive = v.findViewById(R.id.btn_startlive);
        btnLive.setOnClickListener(view -> {
            UserInfo userInfo = UserManager.getInstance().getUser();
            Intent i;
            i = new Intent(mContext, StartLiveActivity.class);
//            if (userInfo.isReanName()) {
//            } else {
//                i = new Intent(mContext, ApplyForzhuboActivity.class);
//            }
            mContext.startActivity(i);
            MainPopupWindow.this.dismiss();
        });
        View llTest = v.findViewById(R.id.ll_test);
        llTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
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
