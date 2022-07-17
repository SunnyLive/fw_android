package com.fengwo.module_live_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;

public class LiveLabelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int[] bgColors = {R.drawable.bg_live_label1,R.drawable.bg_live_label2,R.drawable.bg_live_label3,R.drawable.bg_live_label4};
    public LiveLabelAdapter() {
        super(R.layout.item_live_label);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder mHolder, String table) {
        int bgColor = bgColors[(mHolder.getAdapterPosition() % 4)];
        mHolder.setBackgroundRes(R.id.tv_live_label,bgColor);
        mHolder.setText(R.id.tv_live_label,table);
    }
}
