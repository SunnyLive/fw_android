package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_login.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/30
 */
public class WriteOffTipsPopwindow extends BasePopupWindow {

    private TextView tvTips;
    private TextView tvSure;
    private ImageView ivClose;

    public WriteOffTipsPopwindow(Context context,String fwId) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        tvTips = findViewById(R.id.tv_tips);
        tvSure = findViewById(R.id.tv_sure);
        ivClose = findViewById(R.id.iv_close);
        tvTips.setText("("+fwId+")账号注销申请完成，七天内蜂窝将为您保留数据，我们真诚期待您的回归。");
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener!=null){
                    onConfirmListener.onConfirm();
                    dismiss();
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_write_off_tips);
    }

    private OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener{
        void onConfirm();
    }
}
