package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;

import razerdp.basepopup.BasePopupWindow;

/**
 */
public class BuyGiftTipsPopWindow extends BasePopupWindow implements View.OnClickListener {


    public BuyGiftTipsPopWindow(Context context) {
        super(context);
        //设置显示层级
        getPopupWindow().setWindowLayoutType(WindowManager.LayoutParams.FIRST_SUB_WINDOW+10);
        setPopupGravity(Gravity.BOTTOM);
        setBackgroundColor(ContextCompat.getColor(context,R.color.transparent));
        TextView  tvSendGift = findViewById(R.id.tv_send_gift);
        tvSendGift.setOnClickListener(this);
        OnDismissListener mDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(listener!=null)
                    listener.isDismiss();
            }
        };
        setOnDismissListener(mDismissListener);

        findViewById(R.id.view).setOnClickListener(this);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_buy_gift_tips);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_send_gift) {
            if(listener!=null)
                listener.onGiftSend();
        }else if(view.getId() == R.id.view){
            dismiss();
        }
    }

    OnGiftSendListener listener;

    public interface OnGiftSendListener {
        void onGiftSend();

        void isDismiss();
    }

    public void setOnGiftSendListener(OnGiftSendListener l) {
        listener = l;

    }

}
