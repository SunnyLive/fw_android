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
import com.fengwo.module_login.R;

public class ShareCodeDialog extends DialogFragment {

    private OnItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_share_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.llWeiXin).setOnClickListener(v -> {
            if (listener != null) listener.WeiXinShare();
            dismiss();
        });
        view.findViewById(R.id.llWeiXinCircle).setOnClickListener(v -> {
            if (listener != null) listener.WeiXinCircleShare();
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.gravity = Gravity.BOTTOM;
        attr.windowAnimations = R.style.picker_view_slide_anim;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        attr.height = DensityUtils.dp2px(getContext(), 160F);
        window.setAttributes(attr);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void WeiXinShare();

        void WeiXinCircleShare();
    }
}
