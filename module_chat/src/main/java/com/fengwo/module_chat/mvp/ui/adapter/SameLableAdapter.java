package com.fengwo.module_chat.mvp.ui.adapter;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.R;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.base.BaseVH;

public class SameLableAdapter extends BaseQuickAdapter<CardTagModel, BaseVH> {
    public SameLableAdapter() {
        super(R.layout.adapter_same_lable);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, CardTagModel item) {
        helper.setBackgroundRes(R.id.ll_card_lable,helper.getAdapterPosition()%2==1?R.drawable.card_lable_red_bg:R.drawable.card_lable_bg);
        helper.setText(R.id.tv_card_lable,item.name);
        helper.addOnClickListener(R.id.iv_card_lable_close);
    }
}
