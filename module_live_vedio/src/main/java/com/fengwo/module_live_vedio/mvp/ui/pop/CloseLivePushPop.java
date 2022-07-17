package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/3
 */
public class CloseLivePushPop extends BasePopupWindow {

    private LivingRoomPresenter livingRoomPresenter;

    public CloseLivePushPop(Context context,LivingRoomPresenter presenter) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvSure = findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeLivePush(null);
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_close_live_push);
    }
}
