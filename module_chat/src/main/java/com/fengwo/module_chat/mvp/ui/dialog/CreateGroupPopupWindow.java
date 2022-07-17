package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.fengwo.module_chat.R;

import razerdp.basepopup.BasePopupWindow;

public class CreateGroupPopupWindow extends BasePopupWindow {
    public CreateGroupPopupWindow(Context context, OnItemClickListener confirmClickListener) {
        super(context);
        EditText etContent = findViewById(R.id.etContent);
        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            if (confirmClickListener != null)
                confirmClickListener.createGroupClick(etContent.getText());
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_create_group);
    }

    public interface OnItemClickListener {
        void createGroupClick(CharSequence groupName);
    }
}
