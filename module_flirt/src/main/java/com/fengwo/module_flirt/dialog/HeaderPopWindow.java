package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import razerdp.basepopup.BasePopupWindow;

public class HeaderPopWindow extends BasePopupWindow {

    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.tv_time)
    TextView tvTime;
    @BindView(R2.id.tv_OK)
    TextView tvOK;
    @BindView(R2.id.tv_cancle)
    TextView tvCancle;
    @BindView(R2.id.cv_header)
    CircleImageView cvHeader;
    @BindView(R2.id.iv_close)
    ImageView ivClose;
    @BindView(R2.id.view)
    View view;


    public HeaderPopWindow(Context context, String headerUrl, String title, String content, String nativeStr, String posiStr) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        if (TextUtils.isEmpty(headerUrl)) {
            cvHeader.setVisibility(View.GONE);
        } else
            ImageLoader.loadImg(cvHeader, headerUrl);
        tvContent.setText(title);
        tvTime.setText(content);
        if (!TextUtils.isEmpty(nativeStr)) {
            tvCancle.setText(nativeStr);
            tvCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (null != listener) {
                        listener.cancle();
                    }
                }
            });
            view.setVisibility(View.VISIBLE);
        } else {
            tvCancle.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
        tvOK.setText(posiStr);
        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (null != listener) {
                    listener.sure();
                }
            }
        });
        setBackPressEnable(false);
        setOutSideDismiss(false);
    }

    OnBtnClickListener listener;

    public void setOnBtnClickListener(OnBtnClickListener l) {
        listener = l;
    }

    public interface OnBtnClickListener {
        void sure();

        void cancle();
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_with_header);
        ButterKnife.bind(this, v);
        return v;
    }

}
