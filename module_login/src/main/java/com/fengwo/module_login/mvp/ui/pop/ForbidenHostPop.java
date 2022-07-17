package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.fengwo.module_login.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/4
 */
public class ForbidenHostPop extends BasePopupWindow {

    public ForbidenHostPop(Context context) {
        super(context);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_forbiden_host_pop);
    }
}
