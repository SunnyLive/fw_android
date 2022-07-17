package com.fengwo.module_flirt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_flirt.bean.CerTagBean;

import androidx.annotation.NonNull;

public class CerTagAdapter extends BaseQuickAdapter<CerTagBean, BaseViewHolder> {
    public CerTagAdapter() {
        super(R.layout.chat_item_card_tag);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CerTagBean item) {
        helper.setText(R.id.tv_root, item.getTagName());
        helper.itemView.setSelected(item.selected);
    }
}