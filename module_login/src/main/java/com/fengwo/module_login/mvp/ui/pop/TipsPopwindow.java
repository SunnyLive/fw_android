package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_login.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/30
 */
public class TipsPopwindow extends BasePopupWindow {

    private TextView tvTips;
    private TextView tvSure;

    public TipsPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        tvTips = findViewById(R.id.tv_tips);
        tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener!=null){
                    onConfirmListener.onConfirm();
                    dismiss();
                }
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_tips);
    }

    private OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener{
        void onConfirm();
    }
}
