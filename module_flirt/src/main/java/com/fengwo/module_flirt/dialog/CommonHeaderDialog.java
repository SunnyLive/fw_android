package com.fengwo.module_flirt.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 有头像的弹窗 == 预约成功
 * @Author BLCS
 * @Time 2020/4/1 18:18
 */
public class CommonHeaderDialog extends BaseDialogFragment implements View.OnClickListener {

    public boolean isTransparent;
    public static final String KEY_TRANSPARENT = "KEY_TRANSPARENT";
    public static final String KEY_HEADER = "KEY_HEADER";
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_CONTENT = "KEY_CONTENT";
    public static final String KEY_BTN_CONTENT = "KEY_BTN_CONTENT";
    private TextView tvTime;
    private ImageView ivClose;

    public static CommonHeaderDialog getInstance(boolean isTransparent,String header,String time,String content,String btnContent){
        CommonHeaderDialog commonDialog = new CommonHeaderDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_TRANSPARENT,isTransparent);
        bundle.putString(KEY_HEADER,header);
        bundle.putString(KEY_TIME,time);
        bundle.putString(KEY_CONTENT,content);
        bundle.putString(KEY_BTN_CONTENT,btnContent);
        commonDialog.setArguments(bundle);
        return commonDialog;
    }


    @Override
    protected View createDialogView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_common_header, container, true);
        String headerUrl = getArguments().getString(KEY_HEADER);
        String content = getArguments().getString(KEY_CONTENT);
        String btnContent = getArguments().getString(KEY_BTN_CONTENT);
        String time = getArguments().getString(KEY_TIME);
        CircleImageView cvHeader = view.findViewById(R.id.cv_header);
        ImageLoader.loadCircleImg(cvHeader,headerUrl);
        ImageLoader.loadCircleWithBorder(cvHeader,headerUrl,5f, Color.WHITE);
        TextView tvOK = view.findViewById(R.id.tv_OK);
        tvOK.setText(btnContent);
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(content);
        tvTime = view.findViewById(R.id.tv_time);
        tvTime.setText(time);
        ivClose = view.findViewById(R.id.iv_close);
        tvOK.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        isTransparent = getArguments().getBoolean(KEY_TRANSPARENT);
        if (isTransparent){
            ivClose.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.GONE);
            getDialog().setCanceledOnTouchOutside(false);
            setCancelable(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_OK){
            dismiss();
            if (onSureClickListener!=null) onSureClickListener.sure();
        }else if(id == R.id.iv_close){
            dismiss();
        }
    }


    public CommonHeaderDialog addDialogClick(OnSureClickListener listener){
        onSureClickListener = listener;
        return this;
    }
    public OnSureClickListener onSureClickListener;
    public interface OnSureClickListener{
        void sure();
    }
}
