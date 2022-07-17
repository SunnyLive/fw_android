package com.fengwo.module_chat.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.widgets.header.HeaderScrollHelper;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;

import butterknife.BindView;

public class EmptyFragment extends BaseMvpFragment
        implements HeaderScrollHelper.ScrollableContainer {
    @BindView(R2.id.root)
    RecyclerView recyclerView;

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    @Override
    protected int setContentView() {
        return R.layout.chat_layout_empty;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseQuickAdapter adapter = new BaseQuickAdapter(R.layout.item_chat) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, Object item) {

            }
        };
        recyclerView.setAdapter(adapter);

        View v = LayoutInflater.from(getActivity()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        adapter.setEmptyView(v);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
