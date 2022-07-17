package com.fengwo.module_chat.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/6/19 11:43
 */
public class MuchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MuchAdapter() {
        super(R.layout.adapter_string_much);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String content) {
        baseViewHolder.setText(R.id.tv_content,content);
    }
}
