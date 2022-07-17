package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.View;

import com.fengwo.module_login.R;

import razerdp.basepopup.BasePopupWindow;

public class SendHistoryPop extends BasePopupWindow {

    public SendHistoryPop(Context context, OnClearCallback callback) {
        super(context);
        findViewById(R.id.tv_quit).setOnClickListener(v -> dismiss());
        findViewById(R.id.tv_clear).setOnClickListener(v -> {
            dismiss();
            if (callback != null) callback.onClear();
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_send_history);
    }

    public interface OnClearCallback {
        void onClear();
    }
}
