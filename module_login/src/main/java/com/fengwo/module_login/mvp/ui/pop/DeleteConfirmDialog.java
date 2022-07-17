package com.fengwo.module_login.mvp.ui.pop;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_login.R;

public class DeleteConfirmDialog extends DialogFragment {

    private OnDeleteConformCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_delete_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.tvConfirm).setOnClickListener(v -> {
            if (callback != null) callback.confirm();
            dismiss();
        });
        view.findViewById(R.id.tvCancel).setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.width = ScreenUtils.getScreenWidth(getContext()) - DensityUtils.dp2px(getContext(), 16);
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attr.gravity = Gravity.BOTTOM;
        window.setAttributes(attr);
    }

    public void setCallback(OnDeleteConformCallback callback) {
        this.callback = callback;
    }

    public interface OnDeleteConformCallback {
        void confirm();
    }
}