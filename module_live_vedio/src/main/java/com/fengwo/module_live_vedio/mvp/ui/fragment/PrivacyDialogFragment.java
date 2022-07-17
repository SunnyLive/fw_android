package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseNiceDialog;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.WebActivity;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.ViewHolder;
import com.fengwo.module_live_vedio.R;

import androidx.annotation.NonNull;


public class PrivacyDialogFragment extends BaseNiceDialog implements View.OnClickListener {


    @Override
    public int intLayoutId() {
        return R.layout.dialogfragment_privacy;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        TextView view = holder.getView(R.id.tv_auto);
        SpannableString ss = new SpannableString("如果您同意《蜂窝互娱用户隐私协议》请点击“同意”开始使用我们的产品和服务，我们会竭尽全力保护您的个人信息安全。");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                BrowserActivity.startRuleActivity(getActivity());
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
//                super.updateDrawState(ds);
            }
        };
        ss.setSpan(clickableSpan,5,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan=new ForegroundColorSpan(getActivity().getResources().getColor(R.color.purple_9B7CF1));
        ss.setSpan(colorSpan,5,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(ss);
        holder.setOnClickListener(R.id.tv_disagree,this);
        holder.setOnClickListener(R.id.tv_agree,this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_agree) {
            dismiss();
            SPUtils1.put(getActivity(), "IsHide", true);
        } else if (id == R.id.tv_disagree) {
            SPUtils1.put(getActivity(), "IsHide", false);
            getActivity().finish();
        }
    }

}
