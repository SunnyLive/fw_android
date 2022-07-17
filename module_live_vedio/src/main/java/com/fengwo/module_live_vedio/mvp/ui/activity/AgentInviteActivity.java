package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.dto.AgentInviteDto;
import com.fengwo.module_live_vedio.mvp.presenter.AgentInvitePresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.AgentBtnAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.AgentTopAdapter;
import com.fengwo.module_live_vedio.mvp.ui.iview.IAgentInvite;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;

;
;

public class AgentInviteActivity extends BaseMvpActivity<IAgentInvite, AgentInvitePresenter> implements IAgentInvite {

    @BindView(R2.id.rv_hor)
    RecyclerView rvHor;
    @BindView(R2.id.recycleview)
    RecyclerView rvList;
    @BindView(R2.id.ll_invite_all)
    LinearLayout llInviteAll;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout refresh;
    @BindView(R2.id.tv_invite_all)
    TextView tvInviteAll;
    private AgentTopAdapter headerAdapter;
    private AgentBtnAdapter listAdapter;
    private int page = 1;
    @Override
    protected void initView() {
        setWhiteTitle("邀请人数");
        initRv();
        p.getInvite(page);
    }

    private void initRv() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                p.getInvite(page);
                refresh.finishRefresh();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                p.getInvite(++page);
                refresh.finishLoadMore();
            }
        });
        /*顶部*/
        SmartRefreshLayoutUtils.setWhiteBlackText(this, refresh);
        rvHor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        headerAdapter = new AgentTopAdapter();
        rvHor.setAdapter(headerAdapter);
        /*列表*/
        rvList.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new AgentBtnAdapter();
        rvList.setAdapter(listAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agent_invite_list;
    }

    @Override
    public AgentInvitePresenter initPresenter() {
        return new AgentInvitePresenter();
    }

    @Override
    public void setData(ArrayList<AgentInviteDto> records,String total) {
        if (page==1){
            tvInviteAll.setText("你共邀请" + total + "名一级用户");
            ArrayList<AgentInviteDto> agentInviteDtos = new ArrayList<>();
            agentInviteDtos.addAll(records);
            headerAdapter.setNewData(records.size() > 5 ? records.subList(0, 5) : agentInviteDtos);
            llInviteAll.setVisibility(records.size() > 0 ? View.VISIBLE : View.GONE);
            listAdapter.setNewData(records);
        }else{
            listAdapter.addData(records);
        }
    }
}
