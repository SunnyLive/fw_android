package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_live_vedio.R;

import cn.jpush.android.api.PushNotificationBuilder;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class SingleMatchPopwindow extends BasePopupWindow {

    private Context context;
    private TextView tvMatchSuccessCd,tvMatchStartCd,tvTitle,tvSingleVs,tv_matching,tv_cancel;
    private ImageView ivClose,ivHeadMe,ivHeadOther;
    private CountDownTimer matchStartCDT;
    private boolean isRandom = true;

    public SingleMatchPopwindow(Context context,String headUrl) {
        super(context);
        setPopupGravity(Gravity.BOTTOM|Gravity.RIGHT);
        this.context = context;
//        tvMatchStartCd = findViewById(R.id.tv_bottom_countdown);
        tvMatchSuccessCd = findViewById(R.id.tv_top_countDown);
        tv_matching = findViewById(R.id.tv_matching);
        tv_cancel = findViewById(R.id.tv_cancel);
        tvTitle = findViewById(R.id.tv_title);
        ivClose = findViewById(R.id.iv_close);
        ivHeadMe = findViewById(R.id.iv_head_me);
//        ivHeadOther = findViewById(R.id.iv_head_other);
//        tvSingleVs = findViewById(R.id.tv_single_vs);
//        startMatchCountDown();
        setOutSideDismiss(false);
        ImageLoader.loadCircleImg(ivHeadMe,headUrl);
        setCancle();
    }

    private void setCancle() {
        ivClose.setOnClickListener(l->{
            cancelPop();
        });
        tv_cancel.setOnClickListener(l ->{
            cancelPop();
        });
    }
    public void showPopupWindow(boolean b){
        isRandom = b;
        showPopupWindow();
        startMatchCountDown(isRandom?120:10);
    }

    private void startMatchCountDown(int second) {
        cancelCountDown();
        matchStartCDT= new CountDownTimer(second*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvMatchSuccessCd!=null)
                tvMatchSuccessCd.setText((int) (millisUntilFinished/1000)+"S");
            }

            @Override
            public void onFinish() {
                try {
                    cancelPop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_single_match);
    }

    private void cancelPop(){
        if (onSingleMatchListener!=null){
            onSingleMatchListener.onCancelMatch(isRandom);
            dismiss();
        }
        cancelCountDown();
    }
    public void cancelCountDown(){
        if (matchStartCDT!=null)matchStartCDT.cancel();
    }

    public interface OnSingleMatchListener {
        void  onCancelMatch(boolean isRandom);//取消匹配
    }
    private OnSingleMatchListener onSingleMatchListener;

    public void setOnSingleMatchListener(OnSingleMatchListener onSingleMatchListener) {
        this.onSingleMatchListener = onSingleMatchListener;
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        cancelCountDown();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        cancelCountDown();
    }
}
