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
 * @author GuiLianL
 * @intro
 * @date 2019/12/3
 */
public class CloseFlirtPlayPop extends BasePopupWindow {

    private LivingRoomPresenter livingRoomPresenter;
    private TextView tvMsg;

    public CloseFlirtPlayPop(Context context, ChatRoomPresenter presenter, String anchorId) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvSure = findViewById(R.id.tv_sure);
        tvMsg = findViewById(R.id.tv_msg);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.quitRoom(anchorId, 0);
                dismiss();
            }
        });
    }

    public void setMsg(String msg) {
        tvMsg.setText(msg);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_close_flirt_push);
    }
}
