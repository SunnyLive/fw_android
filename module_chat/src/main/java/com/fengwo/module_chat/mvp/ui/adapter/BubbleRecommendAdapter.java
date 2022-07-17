package com.fengwo.module_chat.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/25
 */
public class BubbleRecommendAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BubbleRecommendAdapter(@Nullable List<String> data) {
        super(R.layout.item_recommend_acticle,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }
}
