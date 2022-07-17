package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;

import java.util.List;

public class RankTopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public RankTopAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.iv_header);

    }
}
