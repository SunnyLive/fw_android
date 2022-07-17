package com.fengwo.module_chat.mvp.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.MerchantListBoean;

import java.util.List;

public class MerchantTagAdapter extends BaseQuickAdapter<MerchantListBoean.MerchantTagModel, BaseViewHolder> {

    public MerchantTagAdapter(List<MerchantListBoean.MerchantTagModel> merchantTagList) {
        super(R.layout.chat_item_merchant_tag, merchantTagList);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MerchantListBoean.MerchantTagModel item) {
        helper.setText(R.id.tv, item.tagName);
    }
}