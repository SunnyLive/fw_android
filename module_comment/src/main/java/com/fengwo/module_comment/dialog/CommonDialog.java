package com.fengwo.module_comment.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.R;

/**
 * 弹窗
 *
 * @Author BLCS
 * @Time 2020/4/1 18:18
 */
public class CommonDialog extends BaseDialogFragment implements View.OnClickListener {
    public static final String KEY_CONTENT = "KEY_CONTENT";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_CANCEL = "KEY_CANCEL";
    public static final String KEY_SURE = "KEY_SURE";
    public static final String KEY_SURE_CONTENT = "KEY_SURE_CONTENT";
    public static final String KEY_SHOW_TIP = "KEY_SHOW_TIP";

    public static final String IS_OUTSIDE_CLICK = "IS_OUTSIDE_CLICK";

    private String content;
    private String sureContent;
    private String cancel;
    private String sure;
    private String title;
    private boolean showTip;

    /**
     * 确定 弹窗
     */
    public static CommonDialog getInstance(String content, String sureContent, String title) {
        CommonDialog commonDialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_CONTENT, content);
        bundle.putString(KEY_SURE_CONTENT, sureContent);
        commonDialog.setArguments(bundle);
        return commonDialog;
    }

    /**
     * 确定 弹窗
     */
    public static CommonDialog getInstance(String content, boolean isoutClick, String sureContent, String title) {
        CommonDialog commonDialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_CONTENT, content);
        bundle.putString(KEY_SURE_CONTENT, sureContent);
        bundle.putBoolean(IS_OUTSIDE_CLICK, isoutClick);
        commonDialog.setArguments(bundle);
        return commonDialog;
    }

    /**
     * 确定 取消 弹窗  // 余额不足 弹窗
     */
    public static CommonDialog getInstance(String title, String content, String cancel, String sure, boolean showTip) {
        CommonDialog commonDialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_CONTENT, content);
        bundle.putString(KEY_CANCEL, cancel);
        bundle.putString(KEY_SURE, sure);
        bundle.putBoolean(KEY_SHOW_TIP, showTip);
        commonDialog.setArguments(bundle);
        return commonDialog;
    }

    /**
     * 确定 取消 弹窗  // 余额不足 弹窗
     */
    public static CommonDialog getInstance(String title, String content, String cancel, String sure, boolean showTip,boolean isButtonFit) {
        CommonDialog commonDialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_CONTENT, content);
        bundle.putString(KEY_CANCEL, cancel);
        bundle.putString(KEY_SURE, sure);
        bundle.putBoolean(KEY_SHOW_TIP, showTip);
        bundle.putBoolean("isButtonFit",isButtonFit);
        commonDialog.setArguments(bundle);
        return commonDialog;
    }

    @Override
    protected View createDialogView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_common, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        LinearLayout llTitle = view.findViewById(R.id.ll_title);
        LinearLayout llSureCancel = view.findViewById(R.id.ll_sure_cancel);
        TextView tvOK = view.findViewById(R.id.tv_OK);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tvOK.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        boolean isOutClick = getArguments().getBoolean(IS_OUTSIDE_CLICK);
        getDialog().setCanceledOnTouchOutside(!isOutClick);
        //参数设置
        title = getArguments().getString("KEY_TITLE");
        content = getArguments().getString("KEY_CONTENT");
        sureContent = getArguments().getString("KEY_SURE_CONTENT");
        boolean isButtonFit = getArguments().getBoolean("isButtonFit");
        if (isButtonFit) tv_cancel.setBackground(tv_sure.getBackground());
        //适配内容过少 手动设置布局宽度
        if (!TextUtils.isEmpty(content)&&content.length() < 6) {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) tvContent.getLayoutParams();
            linearParams.width = getResources().getDisplayMetrics().widthPixels - 500;
            tvContent.setLayoutParams(linearParams);
        }
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
            tvContent.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(sureContent)) {
            tvOK.setText(sureContent);
            tvOK.setVisibility(View.VISIBLE);
        }
        cancel = getArguments().getString("KEY_CANCEL");
        sure = getArguments().getString("KEY_SURE");
        showTip = getArguments().getBoolean("KEY_SHOW_TIP");
        if (!TextUtils.isEmpty(cancel) && !TextUtils.isEmpty(sure)) {
            llSureCancel.setVisibility(View.VISIBLE);
            tv_cancel.setText(cancel);
            tv_sure.setText(sure);

        }
        if (showTip) {
            llTitle.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_OK || id == R.id.tv_sure) {
            if (listener != null) listener.sure();
            dismiss();
        } else if (id == R.id.tv_cancel) {
            if (listener != null) listener.cancel();
            dismiss();
        }
    }

    public CommonDialog addOnDialogListener(OnDialogListener onDialogListener) {
        listener = onDialogListener;
        return this;
    }

    public OnDialogListener listener;

    public interface OnDialogListener {
        void cancel();

        void sure();
    }

}
