package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_login.R;

import java.util.List;

public class DuihuanAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int checkPosition = 0;


    public DuihuanAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        View root = holder.getView(R.id.bg);
        if (position == checkPosition) {
            root.setBackgroundResource(R.drawable.login_bg_chongzhi_selected);
        } else {
            root.setBackgroundResource(R.drawable.login_bg_chongzhi_normal);
        }
        root.setOnClickListener(v -> {
            checkPosition = position;
            notifyDataSetChanged();
            ;
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }
}
