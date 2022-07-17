package com.fengwo.module_comment.pop;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.R;


/**
 * @Author BLCS
 * @Time 2020/7/9 16:13
 */
public class WishTypeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public WishTypeAdapter() {
        super(R.layout.adapter_wish_type);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, String content) {
        viewHolder.setText(R.id.tv_wish,content);
    }
}
