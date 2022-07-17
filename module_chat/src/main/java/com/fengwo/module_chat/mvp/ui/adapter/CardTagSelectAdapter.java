package com.fengwo.module_chat.mvp.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_comment.bean.CardTagModel;

public class CardTagSelectAdapter extends BaseQuickAdapter<CardTagModel, BaseViewHolder> {
    public CardTagSelectAdapter() {
        super(R.layout.chat_item_card_tag);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CardTagModel item) {
        helper.setText(R.id.tv_root, item.name);
        helper.itemView.setSelected(item.selected);
    }
}