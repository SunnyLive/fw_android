package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.http.Query;

/**
 * 主播心愿单详情
 * @Author BLCS
 * @Time 2020/6/8 17:01
 */
public class AnchorWishDetailPop extends BasePopupWindow implements View.OnClickListener {


    private final int type;
    private String giftName;
    private String gifNum;
    private int giftId;
    private String giftImg;
    private final ImageView ivWishPresent;
    private final TextView presentName;

    /**
     *
     * @param context
     * @param type 1 主播生成心愿  2.主播查看心愿 3.用户查看主播心愿
     */
    public AnchorWishDetailPop(Context context,int type) {
        super(context);
        this.type = type;
        ArouteUtils.inject(this);
        setPopupGravity(Gravity.BOTTOM);
        findViewById(R.id.iv_wish_back).setOnClickListener(this);
        TextView ivWish = findViewById(R.id.iv_creat_wish);
        ivWishPresent = findViewById(R.id.iv_wish_present);
        presentName = findViewById(R.id.tv_wish_present_name);
        ivWish.setOnClickListener(this);
        ConstraintLayout clBg = findViewById(R.id.cl_bg);
        ivWish.setVisibility(type==2?View.INVISIBLE:View.VISIBLE);
        if (type==3){
            ivWish.setBackgroundResource(R.drawable.bg_wish_suresing);
            clBg.setBackgroundResource(R.drawable.ic_wish_bg_clent);
        }else{
            clBg.setBackgroundResource(R.drawable.ic_wish_bg_clent);
            ivWish.setBackgroundResource(R.drawable.bg_wish_suresing);
        }

    }
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_anchor_wish_detail);
    }
    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_creat_wish){
            if (type==1){//生成心愿
            }else{//实现心愿

            }
        }
    }

    public OnWishClickListener onWishClickListener;


    public interface OnWishClickListener{
        void close();
    }
    public void addClickListener(OnWishClickListener listener){
        onWishClickListener = listener;
    }

}
