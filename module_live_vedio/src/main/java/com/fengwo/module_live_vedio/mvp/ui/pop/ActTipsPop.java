package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.H5addressBean;
import com.fengwo.module_live_vedio.mvp.ui.activity.BrowserActActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/14
 */
public class ActTipsPop extends BasePopupWindow {

    public ActTipsPop(Context context, H5addressBean data) {
        super(context);
        this.url = data.getAddressLink();
        setPopupGravity(Gravity.CENTER);
        ImageView im_pic = findViewById(R.id.im_pic);
        TextView tvLookDetail = findViewById(R.id.tv_look_detail);
        TextView tvNoTips = findViewById(R.id.tv_no_tip);
        ImageView ivClose = findViewById(R.id.iv_close);
        ImageLoader.loadRouteImg(im_pic,data.getHomePageActivity());
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        CountDownTimer timer = new CountDownTimer(4000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                tvNoTips.setText("今日不再提示("+millisUntilFinished/1000+")");
//            }
//
//            @Override
//            public void onFinish() {
//                dismiss();
//            }
//        };
//        timer.start();
        im_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (!TextUtils.isEmpty(url))
                    BrowserActActivity.start(context,data.getTitle(), url,"1");
            }
        });


    }

    private String url;


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_act_tips);
    }
}
