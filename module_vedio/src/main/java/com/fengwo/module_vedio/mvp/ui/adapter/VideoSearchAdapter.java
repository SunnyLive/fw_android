package com.fengwo.module_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_vedio.R;


public class VideoSearchAdapter extends BaseQuickAdapter<String, BaseVH> {


    public VideoSearchAdapter() {
        super(R.layout.vedio_item_hotest);
    }


    @Override
    protected void convert(@NonNull BaseVH helper, String item) {
        helper.setVisible(R.id.tv_adapter_hot, helper.getAdapterPosition() < 3 ? true : false);
        helper.setText(R.id.tv_adapter, item);
    }
}
