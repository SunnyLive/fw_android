package com.fengwo.module_flirt.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.InvitationAdapter;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CommentWordDto;
import com.fengwo.module_flirt.bean.IliaoBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import okhttp3.RequestBody;

public class ILiaoShareCodeDialog extends DialogFragment {

    private OnItemClickListener listener;
    private InvitationAdapter invitationAdapter;
    SmartRefreshLayout srCardList;
    private int page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
   //     getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.dialog_share_iliao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
        RecyclerView rcv_view  = view.findViewById(R.id.rcv_view);
        srCardList = view.findViewById(R.id.refreshLayout);
        rcv_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
         invitationAdapter = new InvitationAdapter(getContext());
        rcv_view.setAdapter(invitationAdapter);

        view.setOnClickListener(v -> dismiss());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return false;
            }
        });
        view.findViewById(R.id.llWeiXin).setOnClickListener(v -> {
            if (listener != null) listener.WeiXinShare();
            dismiss();
        });
        view.findViewById(R.id.llWeiXinCircle).setOnClickListener(v -> {
            if (listener != null) listener.WeiXinCircleShare();
            dismiss();
        });
        initMode();
    }

    private void initMode() {

        srCardList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                RequestBody build = new WenboParamsBuilder()
                        .put("page", String.valueOf(page))
                        .build();
                new RetrofitUtils().createWenboApi(FlirtApiService.class)
                        .getYqIliao(build)
                        .compose(RxUtils.applySchedulers2())
                        .subscribe(new LoadingObserver<HttpResult<IliaoBean>>() {
                            @Override
                            public void _onNext(HttpResult<IliaoBean> data) {

                                if (data.isSuccess()) {
                                    invitationAdapter.setNewData(data.data.getRecords());
                                    srCardList.finishRefresh();
                                } else {
                                    ToastUtils.showShort(getContext(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                ToastUtils.showShort(getContext(), msg);
                            }
                        });
            }
        });
        srCardList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                RequestBody build = new WenboParamsBuilder()
                        .put("page", String.valueOf(page))
                        .build();
                new RetrofitUtils().createWenboApi(FlirtApiService.class)
                        .getYqIliao(build)
                        .compose(RxUtils.applySchedulers2())
                        .subscribe(new LoadingObserver<HttpResult<IliaoBean>>() {
                            @Override
                            public void _onNext(HttpResult<IliaoBean> data) {

                                if (data.isSuccess()) {
                                    invitationAdapter.addData(data.data.getRecords());

                                    srCardList.finishLoadMore();
                                } else {
                                    ToastUtils.showShort(getContext(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                ToastUtils.showShort(getContext(), msg);
                            }
                        });
            }
        });
        page = 1;
        RequestBody build = new WenboParamsBuilder()
                .put("page", String.valueOf(page))
                .build();
        new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .getYqIliao(build)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<IliaoBean>>() {
                    @Override
                    public void _onNext(HttpResult<IliaoBean> data) {

                        if (data.isSuccess()) {
                            invitationAdapter.setNewData(data.data.getRecords());

                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.gravity = Gravity.BOTTOM;
        attr.windowAnimations = R.style.picker_view_slide_anim;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
      //  attr.height = DensityUtils.dp2px(getContext(), 160F);
        window.setAttributes(attr);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void WeiXinShare();

        void WeiXinCircleShare();
    }
}
