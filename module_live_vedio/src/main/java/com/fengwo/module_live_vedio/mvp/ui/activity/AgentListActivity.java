package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.BrokerRankDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.AgentListAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AgentListActivity extends BaseMvpActivity {

    private static final String TAG = "AgentListActivity";

    @BindViews({R2.id.civ_list_first, R2.id.civ_list_two, R2.id.civ_list_three})
    CircleImageView[] avators;

    @BindViews({R2.id.tv_list_first, R2.id.tv_list_two, R2.id.tv_list_three})
    TextView[] names;

    @BindViews({R2.id.tv_list_first_flower, R2.id.tv_list_two_flower, R2.id.tv_list_three_flower})
    TextView[] flowers;

    @BindView(R2.id.rv_agent_list)
    RecyclerView rvList;
    @BindView(R2.id.sl_agent_list)
    SmartRefreshLayout slAgentList;
    private LiveApiService service;
    private AgentListAdapter mAdapter;
    private int page = 1;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        showLoadingDialog();
        service = new RetrofitUtils().createApi(LiveApiService.class);
        getBrokenTop3();
        //RecyclerView
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AgentListAdapter();
        rvList.setAdapter(mAdapter);
        slAgentList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getBrokenTop3();
            }
        });
        slAgentList.setEnableLoadMore(false);
        SmartRefreshLayoutUtils.setTransparentBlackText(AgentListActivity.this, slAgentList);

    }

    private void getBrokenTop3() {
        service.getBrokerRank(page + ",50")
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<BrokerRankDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<BrokerRankDto>> data) {
                        try {
                            if (data.isSuccess()) {
                                showThree(data.data.records);
                                if (data.data.records.size()>3){
                                    for (int i = 0; i < 3; i++) {
                                        data.data.records.remove(0);
                                    }
                                    mAdapter.setNewData(data.data.records);
                                }
                            }
                            hideLoadingDialog();
                            slAgentList.finishRefresh();
                        } catch (Exception e) {
                            L.e(TAG, "_onNext: " + e.toString());
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        try {
                            toastTip(msg);
                            hideLoadingDialog();
                            slAgentList.finishRefresh();
                        } catch (Exception e) {
                            L.e(TAG, "_onNext: " + e.toString());
                        }
                    }
                });
    }

    /**
     * show top three
     */
    private void showThree(ArrayList<BrokerRankDto> records) {
        for (int i = 0; i < Math.min(3, records.size()); i++) {
            BrokerRankDto brokerRankDto = records.get(i);
            ImageLoader.loadImg(avators[i], brokerRankDto.getUserHeadImg());
            names[i].setText(brokerRankDto.getUserNickname());
            L.e(TAG, "values: " + brokerRankDto.getTotalDivide());
            String values = DataFormatUtils.formatNumbers(Double.parseDouble(brokerRankDto.getTotalDivide()));
            SpannableString spannableString = new SpannableString(values + getResources().getString(R.string.flower_values));

            flowers[i].setText(spannableString);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agent_list;
    }

    @OnClick(R2.id.iv_agent_list_back)
    public void click(View view) {
        if (view.getId() == R.id.iv_agent_list_back) {
            finish();
        }
    }

}
