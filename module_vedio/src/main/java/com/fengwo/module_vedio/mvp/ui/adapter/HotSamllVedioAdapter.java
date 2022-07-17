package com.fengwo.module_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.mvp.dto.SmallVedioListDto.Record;

import java.util.List;

public class HotSamllVedioAdapter extends BaseQuickAdapter<Record, BaseVH> {
    public HotSamllVedioAdapter(int layoutResId, @Nullable List<Record> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, Record item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_fengmian), item.cover);
        helper.addOnClickListener(R.id.root);
    }
}
