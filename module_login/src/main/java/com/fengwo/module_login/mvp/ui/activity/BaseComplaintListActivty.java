package com.fengwo.module_login.mvp.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_login.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/7
 */
public abstract class BaseComplaintListActivty<T> extends BaseListActivity<T> {

    protected final String PAGE_SIZE = ",100";

    @Override
    public void setData(List <T> datas, int page) {
        smartRefreshLayout.closeHeaderOrFooter();
        smartRefreshLayout.setEnableLoadMore(datas.size() == 10);
        this.page = page;
        datas = transFrom(datas);
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

    protected List<T> transFrom(List<T> datas){
        return datas;
    }
}
