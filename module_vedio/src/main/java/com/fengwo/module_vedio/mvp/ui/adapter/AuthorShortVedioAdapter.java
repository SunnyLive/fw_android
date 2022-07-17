package com.fengwo.module_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;

import java.util.List;

public class AuthorShortVedioAdapter extends BaseQuickAdapter<String, BaseVH> {

    public AuthorShortVedioAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, String item) {

    }
}
