package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.HoneyValue;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_live_vedio.mvp.dto.ThisGiftDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveProfitAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.ThisGiftAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/4
 */
public class LiveProfitFragment extends Fragment {

    @BindView(R2.id.rv)
    RecyclerView recyclerView;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.tv_total_text)
    TextView tvTotalText;
    @BindView(R2.id.tv_total_value)
    TextView tvTotalValue;

    private int position = 0;
    private int channelId;
    private List<LiveProfitDto.RecordsBean> liveProfitDto = new ArrayList<>();
    private List<ThisGiftDto.RecordsBean> giftDtoList = new ArrayList<>();
    LiveProfitAdapter liveProfitAdapter;
    ThisGiftAdapter thisGiftAdapter;
    private String pageParam = ",4";
    private int page = 1;

    public static Fragment getInstanse(int p, int channelId) {
        LiveProfitFragment fragment = new LiveProfitFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("p", p);
        bundle.putInt("channelId", channelId);
        fragment.setArguments(bundle);
        return fragment;
    }


//    @Override
//    protected BasePresenter initPresenter() {
//        return null;
//    }
//
//    @Override
//    protected int setContentView() {
//        return R.layout.item_live_profit_vp;
//    }
//
//    @Override
//    protected boolean getImmersionBar() {
//        return false;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_live_profit_vp,null);
        ButterKnife.bind(this,view);
        initUI(savedInstanceState);
        return view;
    }

    public void initUI(Bundle savedInstanceState) {
        position = getArguments().getInt("p", 0);
        channelId = getArguments().getInt("channelId", 0);
        RxBus.get().toObservable(HoneyValue.class)
                .subscribe(honeyValue -> {
                    tvTotalValue.setText(honeyValue.honeyValue + "");
                });
        SmartRefreshLayoutUtils.setClassicsColor(getActivity(), smartRefreshLayout, R.color.white, R.color.text_33);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        View empty = View.inflate(getActivity(), R.layout.item_base_empty_view, null);
        if (position == 0) {
            tvTotalText.setText("本场总计：");
            Drawable diamond = getActivity().getResources().getDrawable(R.drawable.live_ic_diamond);
            diamond.setBounds(0, 0, diamond.getMinimumWidth(), diamond.getMinimumHeight());
            tvTotalValue.setCompoundDrawables(null, null, diamond, null);
            liveProfitAdapter = new LiveProfitAdapter(liveProfitDto, position);
            recyclerView.setAdapter(liveProfitAdapter);
            liveProfitAdapter.setEmptyView(empty);
        } else {
            if (giftDtoList != null) {
                tvTotalText.setText("本场总计花蜜值：");
                thisGiftAdapter = new ThisGiftAdapter(giftDtoList);
                recyclerView.setAdapter(thisGiftAdapter);
                thisGiftAdapter.setEmptyView(empty);
            }
        }
        getData();
    }

    private void getData() {
        if (position == 0) {
            getContribute();
        } else {
            getThisGift();
        }
    }

    /**
     * 本场贡献榜
     *
     * @param
     */
    private void getContribute() {
        new RetrofitUtils().createApi(LiveApiService.class).getContribute(page + pageParam, 1, channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<LiveProfitDto>>() {

                    @Override
                    public void _onNext(HttpResult<LiveProfitDto> data) {
                        if (data.isSuccess()) {
                            RxBus.get().post(new HoneyValue(data.data.getThisReceive() + ""));
                            if (page == 1) {
                                liveProfitAdapter.setNewData(data.data.getRecords());
                            } else {
                                liveProfitAdapter.addData(data.data.getRecords());
                            }
                        } else {
                            ToastUtils.showShort(getActivity(),data.description);
                        }
                        if (smartRefreshLayout == null) return;
                        smartRefreshLayout.closeHeaderOrFooter();
                    }

                    @Override
                    public void _onError(String msg) {
                        smartRefreshLayout.closeHeaderOrFooter();
                    }
                });
    }

    /**
     * 本场收到的礼物
     * @param
     */
    private void getThisGift() {
        new RetrofitUtils().createApi(LiveApiService.class).getThisGift(page + pageParam, channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<ThisGiftDto>>() {

                    @Override
                    public void _onNext(HttpResult<ThisGiftDto> data) {
                        if (data.isSuccess()) {
                            if (page == 1) {
                                thisGiftAdapter.setNewData(data.data.getRecords());
                            } else {
                                thisGiftAdapter.addData(data.data.getRecords());
                            }
                        }
                        if (smartRefreshLayout == null) return;
                        smartRefreshLayout.closeHeaderOrFooter();
                    }

                    @Override
                    public void _onError(String msg) {
                        smartRefreshLayout.closeHeaderOrFooter();
                    }
                });
    }

}
