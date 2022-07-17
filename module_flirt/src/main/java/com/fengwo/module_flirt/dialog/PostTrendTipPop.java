package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_flirt.P.ChatRoomPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter;

import razerdp.basepopup.BasePopupWindow;

/**
 * 提示pop
 */
public class PostTrendTipPop extends BasePopupWindow {


    public PostTrendTipPop(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_post_trend_tip);
    }
}
