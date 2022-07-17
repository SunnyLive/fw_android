package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.PeopleDto;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivingPopPeopleAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

;
;

public class PeoplePopwindow extends BasePopupWindow implements OnLoadMoreListener, OnRefreshListener, View.OnClickListener {


    private final int TYPE_NOW = 4;
    private final int TYPE_WEEK = 2;
    private final int TYPE_MONTH = 3;
    private final int TYPE_CURRENT = 1;

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rv;

    private LivingPopPeopleAdapter adapter;
    private RetrofitUtils retrofitUtils;
    private int channelId;
    private int page = 1;
    private final int PAGESIZE = 20;
    private final String PAGE_SIZE = ",20";
    private int type = TYPE_NOW;
    private View btnNow, btnWeek, btnMonth, btnCurrent;
    private ImageView ivNow, ivWeek, ivMonth, ivCurrent;
    private TextView tvName, tvNum;
    private Context mContext;
    private boolean isLoadMore;

    private TextView tv_zxgz;
    private View empty;

    public PeoplePopwindow(Context context, int channelId, String nickname) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        mContext = context;
        rv = findViewById(R.id.rv);
        smartRefreshLayout = findViewById(R.id.smartrefreshlayout);
        SmartRefreshLayoutUtils.setTransparentBlackText(mContext, smartRefreshLayout);
        btnNow = findViewById(R.id.btn_now);
        btnWeek = findViewById(R.id.btn_week);
        btnMonth = findViewById(R.id.btn_month);
        btnCurrent = findViewById(R.id.btn_current);
        ivNow = findViewById(R.id.iv_now);
        ivWeek = findViewById(R.id.iv_week);
        ivMonth = findViewById(R.id.iv_month);
        tv_zxgz = findViewById(R.id.tv_zxgz);
        ivCurrent = findViewById(R.id.iv_current);
        tvName = findViewById(R.id.tv_name);
        tvNum = findViewById(R.id.tv_num);
        tvName.setText("主播" + nickname);
        empty = View.inflate(context, R.layout.item_base_empty_view, null);
        rv.setLayoutManager(new LinearLayoutManager(context));
        this.channelId = channelId;
        retrofitUtils = new RetrofitUtils();
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadMoreListener(this);
        btnNow.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnMonth.setOnClickListener(this);
        btnCurrent.setOnClickListener(this);
        resetBtnStatus();
        getPeople();
    }

    private void getPeople() {
        if (adapter != null) {
            adapter.removeAllFooterView();
        }
        retrofitUtils.createApi(LiveApiService.class)
                .getReceiveList(page + PAGE_SIZE, type, this.channelId)
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<PeopleDto>>((MvpView) mContext) {
                    @Override
                    public void _onNext(HttpResult<PeopleDto> data) {
                        if (data.isSuccess()) {
                            smartRefreshLayout.closeHeaderOrFooter();
                            // tv_zxgz.setText("在线观众("+data.data.lookNums + ")");
                            tvNum.setText(data.data.lookNums + "");
                            if (CommentUtils.isListEmpty(data.data.records) || data.data.records.size() < PAGESIZE) {
                                smartRefreshLayout.setEnableLoadMore(false);
                            } else {
//                                smartRefreshLayout.setEnableLoadMore(false);
                                smartRefreshLayout.setEnableLoadMore(true);
                            }
                            if (null == adapter) {
                                adapter = new LivingPopPeopleAdapter(data.data.records);
                                adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                    @Override
                                    public void onItemChildClick(BaseQuickAdapter adapter1, View view, int position) {
                                        int id = view.getId();
                                        if (id == R.id.root) {
                                            ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, adapter.getItem(position).getId());
                                        }
                                    }
                                });
                                rv.setAdapter(adapter);
                                adapter.setEmptyView(empty);
                            } else {
                                if (isLoadMore) {
                                    adapter.addData(data.data.records);
                                } else {
                                    adapter.setNewData(data.data.records);
                                }
                            }
                            isLoadMore = false;
                        } else {
                            ToastUtils.showShort(mContext, "网络异常");
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(mContext, "网络异常");
                    }
                });
    }

    private void getOnLine() {

        retrofitUtils.createApi(LiveApiService.class)
                .getRoomWatchers(this.channelId + "")
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<List<WatcherDto>>>((MvpView) mContext) {
                    @Override
                    public void _onNext(HttpResult<List<WatcherDto>> data) {
                        if (data.isSuccess()) {
                            smartRefreshLayout.closeHeaderOrFooter();
                            smartRefreshLayout.setEnableLoadMore(false);
                            tvNum.setText(data.data.size() + "");
                            if (null == adapter) {
                                adapter = new LivingPopPeopleAdapter(getMax50Data(data.data));
                                adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                    @Override
                                    public void onItemChildClick(BaseQuickAdapter adapter1, View view, int position) {
                                        int id = view.getId();
                                        if (id == R.id.root) {
                                            ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, adapter.getItem(position).userId);
                                        }
                                    }
                                });
                                rv.setAdapter(adapter);
                                adapter.setEmptyView(empty);
                            } else {
//                                if (isLoadMore) {
//                                    adapter.addData(data.data);
//                                } else {
                                adapter.setNewData(getMax50Data(data.data));
//                                }
                            }
                            adapter.removeAllFooterView();
                            if (adapter != null && adapter.getData().size() == 50) {
                                View loadMore = LayoutInflater.from(mContext).inflate(R.layout.view_loadmore, null, false);
                                adapter.addFooterView(loadMore);
                            }
                            isLoadMore = false;
                        } else {
                            ToastUtils.showShort(mContext, "网络异常");
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(mContext, "网络异常");
                    }
                });
    }

    private List<WatcherDto> getMax50Data(List<WatcherDto> datas) {
        return datas.size() > 50 ? datas.subList(0, 50) : datas;

    }

    public void setData(List<WatcherDto> data) {
        adapter.setNewData(data);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_people);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = true;
        page++;
        getPeople();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        page = 1;
        if (type == TYPE_CURRENT) {
            getOnLine();
        } else
            getPeople();
    }

    @Override
    public void onClick(View view) {
        page = 1;
        int id = view.getId();
        if (id == R.id.btn_now) {
            type = TYPE_NOW;
            getPeople();
        } else if (id == R.id.btn_week) {
            type = TYPE_WEEK;
            getPeople();
        } else if (id == R.id.btn_month) {
            type = TYPE_MONTH;
            getPeople();
        } else if (id == R.id.btn_current) {
            type = TYPE_CURRENT;
            getOnLine();
        }
        resetBtnStatus();

    }

    private void resetBtnStatus() {
        btnNow.setSelected(type == TYPE_NOW);
        ivNow.setVisibility(type == TYPE_NOW ? View.VISIBLE : View.INVISIBLE);
        btnWeek.setSelected(type == TYPE_WEEK);
        ivWeek.setVisibility(type == TYPE_WEEK ? View.VISIBLE : View.INVISIBLE);
        btnMonth.setSelected(type == TYPE_MONTH);
        ivMonth.setVisibility(type == TYPE_MONTH ? View.VISIBLE : View.INVISIBLE);
        btnCurrent.setSelected(type == TYPE_CURRENT);
        ivCurrent.setVisibility(type == TYPE_CURRENT ? View.VISIBLE : View.INVISIBLE);
    }
}
