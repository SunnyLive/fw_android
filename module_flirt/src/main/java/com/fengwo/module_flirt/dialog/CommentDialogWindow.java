package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class CommentDialogWindow extends BasePopupWindow {

    @BindView(R2.id.tv_content)
    TextView tv_content;
    @BindView(R2.id.tv_Ok)
    TextView tv_Ok;

    public CommentDialogWindow(Context context, Boolean isOutSide, String okContent, String content) {
        super(context);
        tv_content.setText(content);
        tv_Ok.setText(okContent);
        tv_Ok.setOnClickListener(v -> {
            if (listener != null) listener.click();
        });
        setPopupGravity(Gravity.CENTER);
        setBackPressEnable(false);
        setOutSideDismiss(isOutSide);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_comment_dialog);
        ButterKnife.bind(this, v);
        return v;
    }

    public void addOnClickListener(OnSureClickListener onSureClickListener) {
        listener = onSureClickListener;
    }

    public OnSureClickListener listener;

    public interface OnSureClickListener {
        void click();
    }

}
