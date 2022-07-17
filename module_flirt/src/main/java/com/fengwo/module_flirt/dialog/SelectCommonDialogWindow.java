package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

/**
 * 长按两个按钮 或者一个按钮的dialog -》1.删除评论/点赞 2.删除消息会话和置顶
 */
public class SelectCommonDialogWindow extends BasePopupWindow {

    @BindView(R2.id.tv_content)
    TextView tv_content;
    @BindView(R2.id.tv_first)
    TextView tv_first;
    @BindView(R2.id.tv_second)
    TextView tv_second;

    public SelectCommonDialogWindow(Context context, Boolean isOutSide, String title, String firstContent, String secondContent) {
        super(context);
        tv_content.setText(title);
        tv_content.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        tv_first.setText(firstContent);
        tv_first.setVisibility(TextUtils.isEmpty(firstContent) ? View.GONE : View.VISIBLE);
        tv_second.setText(secondContent);
        tv_second.setVisibility(TextUtils.isEmpty(secondContent) ? View.GONE : View.VISIBLE);
        tv_first.setOnClickListener(v -> {
            if (listener != null) listener.onFirstclick();
        });
        tv_second.setOnClickListener(v -> {
            if (listener != null) listener.onSecondclick();
        });

        setPopupGravity(Gravity.CENTER);
        setBackPressEnable(false);
        setOutSideDismiss(isOutSide);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_select_common_dialog);
        ButterKnife.bind(this, v);
        return v;
    }

    public void addOnClickListener(OnSureClickListener onSureClickListener) {
        listener = onSureClickListener;
    }

    public OnSureClickListener listener;

    public interface OnSureClickListener {
        void onFirstclick();

        void onSecondclick();
    }

}
