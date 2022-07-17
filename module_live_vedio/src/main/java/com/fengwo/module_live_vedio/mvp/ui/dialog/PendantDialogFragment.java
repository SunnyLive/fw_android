package com.fengwo.module_live_vedio.mvp.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;

import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantBean;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PendantAdapter;
import com.fengwo.module_live_vedio.mvp.ui.df.BaseDialogFragment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 直播挂件/贴纸
 * @auchor huanghongfu
 */
public class PendantDialogFragment extends BaseDialogFragment implements OnRefreshListener {
    private SmartRefreshLayout refresh_layout;
    private RecyclerView recyclerview;

    private PendantAdapter mAdapter;

    public static PendantDialogFragment newInstance() {

        Bundle args = new Bundle();

        PendantDialogFragment fragment = new PendantDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = getOrientation();
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
//        win.setLayout(getWidth(), getHeight());
        params.dimAmount=0;
        getDialog().setCancelable(cancelable());
        getDialog().setCanceledOnTouchOutside(cancelable());
    }

    @Override
    protected void initView() {
        refresh_layout=findViewById(R.id.refresh_layout);
        recyclerview=findViewById(R.id.recyclerview);

        refresh_layout.setOnRefreshListener(this);
        refresh_layout.autoRefresh();

        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerview.setAdapter(mAdapter=new PendantAdapter(null));

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PendantBean pendantBean = mAdapter.getData().get(position);
            if(pendantBean.getOutType()==0){
                //贴纸类型
                if(mOnPendantClickListener!=null){
                    mOnPendantClickListener.onPendantClick(pendantBean);
                }
            }
        });
    }

    private void config() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .config()
                .compose(RxUtils.applySchedulers())
                .compose(bindToLifecycle())
                .subscribeWith(new LoadingObserver<HttpResult<List<PendantBean>>>() {
                    @Override
                    public void _onNext(HttpResult<List<PendantBean>> data) {
                        if(data.data==null){
                            data.data=new ArrayList<>();
                        }
                        //先找到可输入的贴纸类型
                        ArrayList<PendantBean> finalPendant=new ArrayList<>();
                        ArrayList<PendantBean> textPendant=new ArrayList<>();
                        ArrayList<PendantBean> bitmapPendant=new ArrayList<>();
                        for(PendantBean item:data.data){
                            if(item.getStickerType()==0){
                                textPendant.add(item);
                            }else{
                                bitmapPendant.add(item);
                            }
                        }

                        if(textPendant.size()!=0){
                            PendantBean bean=new PendantBean();
                            bean.setTitle("文字贴纸");
                            bean.setOutType(PendantBean.TITLE_TYPE);
                            textPendant.add(bean);
                        }
                        if(bitmapPendant.size()!=0){
                            PendantBean bean=new PendantBean();
                            bean.setTitle("图片贴纸");
                            bean.setOutType(PendantBean.TITLE_TYPE);
                            textPendant.add(bean);
                        }
                        finalPendant.addAll(textPendant);
                        finalPendant.addAll(bitmapPendant);

                        mAdapter.setNewData(finalPendant);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }
    private OnPendantClickListener mOnPendantClickListener;

    public PendantDialogFragment setOnPendantClickListener(OnPendantClickListener listener){
        mOnPendantClickListener=listener;
        return this;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.moudle_live_video_dialog_pendant_layout;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        config();
    }


    public  interface  OnPendantClickListener{
        void onPendantClick(PendantBean bean);
    }

}
