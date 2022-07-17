package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.BlackDto;
import com.fengwo.module_login.mvp.presenter.BlackListPresenter;
import com.fengwo.module_login.mvp.ui.adapter.BlackListAdapter;
import com.fengwo.module_login.mvp.ui.iview.IBlackListView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BlackListActivity extends BaseMvpActivity<IBlackListView, BlackListPresenter> implements IBlackListView {
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R2.id.empty_view)
    LinearLayout empty_view;
    private BlackListAdapter blackListAdapter;
    private List<BlackDto> datas= new ArrayList<>();
    @Override
    public BlackListPresenter initPresenter() {
        return new BlackListPresenter();
    }
    private int page = 1;
    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("黑名单")
                .setTitleColor(R.color.text_33)
                .build();
        //UI
        blackListAdapter = new BlackListAdapter(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycleview.setAdapter(blackListAdapter);
        blackListAdapter.setDeleteBlack(new BlackListAdapter.DeleteBlackListener() {
            @Override
            public void delete(int id,int pos) {
                p.deleteBlack(id,pos);
            }
        });
        p.getBlackList("1,20");
        //refresh
        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                p.getBlackList(page+",20");
            }
        });
        smartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                p.getBlackList(page+",20");
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_blacklist;
    }

    @Override
    public void getBalckList(BaseListDto<BlackDto> data) {
        if (page==1){
            datas.clear();
            datas.addAll(data.records);
            smartrefreshlayout.finishRefresh();
        }else{
            datas.addAll(data.records);
            smartrefreshlayout.finishLoadMore();
        }
        blackListAdapter.notifyDataSetChanged();
        showEmptyView();
    }

    private void showEmptyView() {
        smartrefreshlayout.setVisibility(datas.size()>0? View.VISIBLE:View.GONE);
        empty_view.setVisibility(datas.size()>0? View.GONE:View.VISIBLE);
    }

    @Override
    public void deleteSuccess(String data,int pos) {
        datas.remove(pos);
        blackListAdapter.notifyItemRemoved(pos);
        showEmptyView();
    }
}
