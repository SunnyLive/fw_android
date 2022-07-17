package com.fengwo.module_chat.mvp.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class TranspondDialog extends BasePopupWindow {

    @BindView(R2.id.rv_header)
    com.fengwo.module_comment.widget.RoundImageView rvHeader;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.iv_content)
    com.fengwo.module_comment.widget.RoundImageView ivContent;

    public TranspondDialog(Context context) {
        super(context);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setPopupGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateContentView() {
        View popupById = createPopupById(R.layout.dialog_transpond);
        ButterKnife.bind(this,popupById);
        return popupById;
    }

    public void setContent(String ImgUrl,String name,String content){
        if(!TextUtils.isEmpty(ImgUrl)) ImageLoader.loadRouteImg(rvHeader,ImgUrl);
        if(!TextUtils.isEmpty(name)) tvName.setText(name);
        if(!TextUtils.isEmpty(content)){
            if(content.startsWith("https://")){
                tvContent.setVisibility(View.GONE);
                ivContent.setVisibility(View.VISIBLE);
                ImageLoader.loadRouteImg(ivContent,content);
            }else{
                tvContent.setVisibility(View.VISIBLE);
                ivContent.setVisibility(View.GONE);
                tvContent.setText(content);
            }
        }
    }

    @OnClick({R2.id.tv_cancel,R2.id.tv_ok})
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.tv_cancel) {
            if (listener!=null) listener.clickCancel();
            dismiss();
        } else if (id == R.id.tv_ok) {
            if (listener!=null) listener.clickOk();
            dismiss();
        }
    }

    public OnClickListener listener;

    public interface OnClickListener{
        void clickCancel();
        void clickOk();
    }
    public void addOnClickListener(OnClickListener onClickListener){
        listener = onClickListener;
    }

}