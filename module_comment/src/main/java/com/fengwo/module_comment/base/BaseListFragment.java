package com.fengwo.module_comment.base;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public abstract class BaseListFragment<T> extends BaseMvpFragment<IListView<T>, BaseListPresenter<T>> implements IListView<T> {

    private ArrayList<T> datas;
    protected final static String PAGE_SIZE = ",10";
    private int pageSize = 10;

    protected SmartRefreshLayout smartRefreshLayout;
    protected RecyclerView recyclerView;
    protected BaseQuickAdapter<T, BaseViewHolder> adapter;

    protected int page = 1;
    private boolean isRefresh = true;

    @Override
    public void initView(View v) {
        super.initView(v);
        datas = new ArrayList<>();
        smartRefreshLayout = v.findViewById(R.id.smartrefreshlayout);
        recyclerView = v.findViewById(R.id.recycleview);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
//            smartRefreshLayout.closeHeaderOrFooter();
            smartRefreshLayout.finishRefresh();
            isRefresh = true;
            page = 1;
            onRefresh();
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
//            smartRefreshLayout.closeHeaderOrFooter();
            isRefresh = false;
            page = BaseListFragment.this.page + 1;
            p.getListData(page);
        });
        initRv();
        p.getListData(page);
    }

    protected void onRefresh() {
        if (null == p) {
            return;
        }
        p.getListData(1);
    }

    public ArrayList<T> getData() {
        return (ArrayList<T>) adapter.getData();
    }

    protected void initRv() {
        recyclerView.setLayoutManager(setLayoutManager());
        adapter = new BaseQuickAdapter<T, BaseViewHolder>(setItemLayoutRes(), datas) {

            @Override
            protected void convert(@NonNull BaseViewHolder helper, T item) {
                int realPosition = helper.getLayoutPosition() - adapter.getHeaderLayoutCount();
                bingViewHolder(helper, item, realPosition);
            }

        };
        recyclerView.setAdapter(adapter);
        if (hasEmptyView()) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_base_empty_view, null, false);
            if (!TextUtils.isEmpty(setEmptyContent())) {
                TextView tvContent = v.findViewById(R.id.tv_empty);
                tvContent.setText(setEmptyContent());
            }
            adapter.setEmptyView(v);
        }
    }

    public boolean hasEmptyView() {
        return true;
    }

    @Override
    public BaseListPresenter<T> initPresenter() {
        return new BaseListPresenter<>(setNetObservable().compose(bindToLifecycle()));
    }

    @Override
    public void setData(List<T> datas, int page) {
        if (null == datas) {
            return;
        }
//        if (datas.size() < pageSize) {//改首页下拉
//            smartRefreshLayout.finishLoadMoreWithNoMoreData();
//        }
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
        this.page = page;
        if ( page == 1) {
            adapter.setNewData(datas);
        } else {
            adapter.addData(datas);
        }

    }

    @Override
    public Flowable httpRequest() {
        return setNetObservable();
    }

    public abstract Flowable setNetObservable();

    public abstract RecyclerView.LayoutManager setLayoutManager();

    public abstract int setItemLayoutRes();

    public abstract void bingViewHolder(BaseViewHolder helper, T item, int position);

    public abstract String setEmptyContent();
}
