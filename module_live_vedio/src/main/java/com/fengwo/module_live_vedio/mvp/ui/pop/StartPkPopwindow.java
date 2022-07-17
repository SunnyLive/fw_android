package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;

import cc.shinichi.library.tool.ui.ToastUtil;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class StartPkPopwindow extends BasePopupWindow {

    private Context context;
    private ImageView ivSinglePk,ivTeamPk;
    private TextView tvSingleBottom,tvTeamBottom;
    private CheckBox checkBox;
    private boolean isSingle = true;//是否单人pk

    public StartPkPopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.context = context;
        ivSinglePk = findViewById(R.id.iv_start_pk_single);
        ivTeamPk = findViewById(R.id.iv_start_pk_team);
        tvSingleBottom = findViewById(R.id.tv_single_bottom);
        tvTeamBottom = findViewById(R.id.tv_team_bottom);
        checkBox = findViewById(R.id.cb_start_pk_pop);
        ivSinglePk.setSelected(true);
        ivSinglePk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSingleBottom.setText("随机匹配");
                tvTeamBottom.setText("邀请主播");
                ivSinglePk.setSelected(true);
                ivTeamPk.setSelected(false);
                isSingle = true;
            }
        });
        ivTeamPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSingleBottom.setText("随机加入团队");
                tvTeamBottom.setText("创建团队");
                ivSinglePk.setSelected(false);
                ivTeamPk.setSelected(true);
                isSingle = false;
            }
        });
        tvSingleBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStartPkListener==null)return;
                if (isSingle){
                    onStartPkListener.onSingleRandom();
                }else {
                    onStartPkListener.onTeamRandom();
                }
                dismiss();
            }
        });
        tvTeamBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStartPkListener==null)return;
                if (isSingle){
                    onStartPkListener.onSingleInvite();
                }else {
                    onStartPkListener.onTeamInvite();
                }
                dismiss();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.cb_start_pk_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnPk();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkReceivePkMsg();
            }
        },500);

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_start_pk_pop);
    }


    public interface OnStartPkListener{
        void onSingleRandom();
        void onSingleInvite();
        void onTeamRandom();
        void onTeamInvite();
    }
    private OnStartPkListener onStartPkListener;

    public void setOnStartPkListener(OnStartPkListener onStartPkListener) {
        this.onStartPkListener = onStartPkListener;
    }

    private void turnPk(){
        int state;
        if (checkBox.isChecked()){
            state = 0;
        }else {
            state = 1;
        }
        new RetrofitUtils().createApi(LiveApiService.class)
                .onOffPk(state)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context,data.description);
//                        checkBox.setChecked(!checkBox.isChecked());
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context,msg);
                    }
                });
    }
    private void checkReceivePkMsg(){
        new RetrofitUtils().createApi(LiveApiService.class)
                .checkReceivePkMsg()
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            String s = data.data.toString();
                            if (s.contains("1")){
                                checkBox.setChecked(false);
                            }else {
                                checkBox.setChecked(true);
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context,msg);
                    }
                });
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

}
