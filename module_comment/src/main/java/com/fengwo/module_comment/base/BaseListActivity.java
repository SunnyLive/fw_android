package com.fengwo.module_comment.base;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public abstract class BaseListActivity<T> extends BaseMvpActivity<IListView<T>, BaseListPresenter<T>> implements IListView<T> {

    private List<T> datas;
    protected int page = 1;
    protected final String PAGE_SIZE = ",10";
    protected final String DEFAULT_PAGE_SIZE = "10";

    protected SmartRefreshLayout smartRefreshLayout;
    protected RecyclerView recyclerView;
    protected BaseQuickAdapter<T, BaseViewHolder> adapter;

    public boolean isRefresh = true;


    @Override
    protected void initView() {
        smartRefreshLayout = findViewById(R.id.smartrefreshlayout);
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        recyclerView = findViewById(R.id.recycleview);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refresh();
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            isRefresh = false;
            BaseListActivity.this.page = BaseListActivity.this.page + 1;
            p.getListData(page);
//            int p1  = BaseListActivity.this.page + 1;
//            p.getListData(p1);

        });
        p.getListData(page);
    }

    protected void refresh() {
        isRefresh = true;
        BaseListActivity.this.page = 1;
        p.getListData(1);
    }

    @Override
    public BaseListPresenter<T> initPresenter() {
        return new BaseListPresenter<T>(setNetObservable());
    }

    @Override
    public void setData(List<T> datas, int page) {
        smartRefreshLayout.closeHeaderOrFooter();
        smartRefreshLayout.setEnableLoadMore(datas.size() == 10);
        this.page = page;
        if (isRefresh) {
            if (null == adapter) {
                recyclerView.setLayoutManager(setLayoutManager());
                adapter = new BaseQuickAdapter<T, BaseViewHolder>(setItemLayoutRes(), datas) {
                    @Override
                    protected void convert(@NonNull BaseViewHolder helper, T item) {

                    }

                    @Override
                    public void onBindViewHolder(BaseViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        if (position >= getDatas().size()) {
                            return;
                        }
                        bingViewHolder(holder, getDatas().get(position), position);
                    }
                };
                recyclerView.setAdapter(adapter);
                if (getHeadView() != null) {
                    adapter.addHeaderView(getHeadView());
                }
                View v = LayoutInflater.from(this).inflate(R.layout.item_base_empty_view, null, false);
                adapter.setEmptyView(v);
            } else {
                adapter.setNewData(datas);
            }
        } else {
            adapter.addData(datas);
        }

    }
    public void settifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return adapter.getData();
    }

    @Override
    public Flowable httpRequest() {
        return setNetObservable();
    }

    public View getHeadView() {
        return null;
    }

    public abstract Flowable setNetObservable();

    public abstract RecyclerView.LayoutManager setLayoutManager();

    public abstract int setItemLayoutRes();

    public abstract void bingViewHolder(BaseViewHolder helper, T item, int position);
}
