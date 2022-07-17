package com.fengwo.module_flirt.adapter;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_flirt.R;

/**
 * @Author BLCS
 * @Time 2020/7/27 16:08
 */
public class ReplayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ReplayAdapter() {
        super(R.layout.adapter_replay);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, String s) {
        viewHolder.setText(R.id.tv_replay,s);
    }
}
