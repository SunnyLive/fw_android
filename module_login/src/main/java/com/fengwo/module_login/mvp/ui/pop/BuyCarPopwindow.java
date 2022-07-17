package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/16
 */
public class BuyCarPopwindow extends BasePopupWindow {

    TextView tvName,tvDayNum,tvPrice,tvCancel,tvBuy;
    ImageView ivImg;

    public BuyCarPopwindow(Context context,String name,String imgUrl,int dayNum,int price) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        tvName = findViewById(R.id.tv_name);
        tvDayNum = findViewById(R.id.tv_duration);
        tvPrice = findViewById(R.id.tv_price);
        tvCancel = findViewById(R.id.tv_close);
        tvBuy = findViewById(R.id.tv_buy);
        ivImg = findViewById(R.id.iv_img);
        ImageLoader.loadImg(ivImg,imgUrl);
        tvName.setText(name);
        tvDayNum.setText(dayNum+"天");
        tvPrice.setText(price+"花钻");
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBuyCarListener!=null){
                    onBuyCarListener.onBuyCar();
                }
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_buy_car);
    }

    public interface OnBuyCarListener{
        void onBuyCar();
    }
    private OnBuyCarListener onBuyCarListener;

    public OnBuyCarListener getOnBuyCarListener() {
        return onBuyCarListener;
    }

    public void setOnBuyCarListener(OnBuyCarListener onBuyCarListener) {
        this.onBuyCarListener = onBuyCarListener;
    }
}
