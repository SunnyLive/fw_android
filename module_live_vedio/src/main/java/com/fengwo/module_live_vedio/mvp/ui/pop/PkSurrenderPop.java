package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.EndPkEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

import cc.shinichi.library.tool.ui.ToastUtil;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/20
 */
public class PkSurrenderPop extends BasePopupWindow {

    private Context context;
    private CompositeDisposable disposables;

    public PkSurrenderPop(Context context, String text) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.CENTER);
        disposables = new CompositeDisposable();
        TextView tvTitle = findViewById(R.id.tv_pop_title);
        TextView tvContent = findViewById(R.id.tv_pop_content);
        TextView tvSure = findViewById(R.id.tv_sure);
        tvTitle.setText(text.contains("PK") ? "当前正在PK中，确定发起投降？" : "是否确定关闭本场PK？");
        tvContent.setText(text.contains("PK") ? "发起投降后，将直接结束本场PK。同时\n" +
                "本场PK判断发起投降方为败。" : "关闭后将直接结束本场PK，确定此操作\n" +
                "前最好与对方主播沟通清楚。");
        tvSure.setText(text.contains("PK") ? "确定投降，并结束本场PK" : "确定关闭");
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pkSurrender(text.contains("PK"));
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_pk_surrender);
    }

    private void pkSurrender(boolean isShow) {
        new RetrofitUtils().createApi(LiveApiService.class)
                .pkSurrender()
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            if (isShow) ToastUtils.showShort(context, data.description);
//                            RxBus.get().post(new EndPkEvent());
                        }else {
                            ToastUtils.showShort(context, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });
    }
}
