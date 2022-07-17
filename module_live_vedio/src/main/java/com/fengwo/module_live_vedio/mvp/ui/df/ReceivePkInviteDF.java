package com.fengwo.module_live_vedio.mvp.ui.df;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_websocket.bean.InvitePkMsg;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/6
 */
public class ReceivePkInviteDF extends BaseDialogFragment {

    private InvitePkMsg invitePkMsg;
    private boolean isSingle;

    public static DialogFragment getInstance(InvitePkMsg invitePkMsg,boolean isSingle){
        ReceivePkInviteDF receivePkInviteDF = new ReceivePkInviteDF();
        Bundle bundle = new Bundle();
        bundle.putSerializable("invitePkMsg",invitePkMsg);
        bundle.putBoolean("isSingle",isSingle);
        receivePkInviteDF.setArguments(bundle);
        return receivePkInviteDF;
    }

    @Override
    protected void initView() {
        invitePkMsg = (InvitePkMsg) getArguments().getSerializable("invitePkMsg");
        isSingle = getArguments().getBoolean("isSingle");
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> dismiss());
        ImageView ivHeader = findViewById(R.id.iv_header);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvTips = findViewById(R.id.tv_tips);
        TextView tvAccept = findViewById(R.id.tv_accept);
        TextView tvReject = findViewById(R.id.tv_reject);
        ImageLoader.loadImg(ivHeader,invitePkMsg.headImg);
        tvName.setText(invitePkMsg.nickname);
        if(isSingle){tvTips.setText("邀请你进行PK");
        }else {
            tvTips.setText("邀请你组团进行PK");
        }
        tvAccept.setOnClickListener(v ->{
            onResponseListener.onResponse(true);
            dismiss();
        });
        tvReject.setOnClickListener(v -> {
            onResponseListener.onResponse(false);
            dismiss();
        });

        CountDownTimer countDownTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvReject.setText("拒绝（"+millisUntilFinished/1000+"s）");
            }

            @Override
            public void onFinish() {
                onResponseListener.onResponse(false);
                dismiss();
            }
        };
        countDownTimer.start();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.live_receive_pk_invite;
    }

    public interface OnResponseListener{
       void onResponse(boolean accept);
    }
    private OnResponseListener onResponseListener;

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        onResponseListener.onResponse(false);
    }
}
