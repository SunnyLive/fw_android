package com.fengwo.module_chat.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/26
 */
public class SearchHotAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private final int[] backgrounds = new int[]{
            R.drawable.bg_search_item_1,
            R.drawable.bg_search_item_2,
            R.drawable.bg_search_item_3,
            R.drawable.bg_search_item_4
    };

    public SearchHotAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_hot, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        int position = helper.getLayoutPosition();
        helper.setBackgroundRes(R.id.root, backgrounds[position % 4]);
        helper.setText(R.id.tv_tag_name, item);
    }
}
