package com.fengwo.module_flirt.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_flirt.R;

import androidx.annotation.NonNull;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/29
 */
public class CertificationDialog extends Dialog {

    private Context context;
    private String des;
    private String sureText;
    private onPositiveListener onPositiveListener;

    public CertificationDialog(@NonNull Context context, String des, String sureText, onPositiveListener listener) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.des = des;
        this.sureText = sureText;
        this.onPositiveListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flirt_certification);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView tvDes = findViewById(R.id.tv_des);
        TextView tvSure = findViewById(R.id.tv_sure);
        tvDes.setText(des);
        tvSure.setText(sureText);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPositiveListener != null) {
                    onPositiveListener.onPositive();
                }
                dismiss();
            }
        });
    }

    public interface onPositiveListener {
        void onPositive();
    }

}
