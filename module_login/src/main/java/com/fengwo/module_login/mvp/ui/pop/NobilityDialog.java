package com.fengwo.module_login.mvp.ui.pop;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fengwo.module_login.R;

public class NobilityDialog extends DialogFragment {

    public static final int TYPE_PAY_CONFIRM = 1;
    public static final int TYPE_PAY_FAILURE = 2;

    private int type = TYPE_PAY_CONFIRM;

    private OnPositiveItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_nobility, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvConfirm = view.findViewById(R.id.tvConfirm);
        view.findViewById(R.id.tvCancel).setOnClickListener(v -> dismiss());
        if (type == TYPE_PAY_CONFIRM) {
            tvTitle.setText("是否开通");
            tvConfirm.setText("立即支付");
        } else {
            tvTitle.setText("余额不足");
            tvConfirm.setText("去充值");
        }
        tvConfirm.setOnClickListener(v -> {
                if (listener != null) {
                    if (type == TYPE_PAY_CONFIRM) listener.onItemConfirm();
                    else if (type == TYPE_PAY_FAILURE) listener.onItemPay();
                }
            dismiss();
        });
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setListener(OnPositiveItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnPositiveItemClickListener {
        void onItemConfirm();

        void onItemPay();
    }
}