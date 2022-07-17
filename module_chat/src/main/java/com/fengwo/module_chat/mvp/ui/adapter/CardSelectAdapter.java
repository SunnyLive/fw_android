package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_comment.utils.ImageLoader;

public class CardSelectAdapter extends BaseQuickAdapter<RecommendCircleBean, BaseViewHolder> {

    private final int MAX_COUNT;
    private int selectedCount = 0;

    public CardSelectAdapter(int maxCount) {
        super(R.layout.chat_item_card_select);
        MAX_COUNT = maxCount;
        setOnItemClickListener((adapter, view, position) -> {
            if (mData.get(position).selected) {
                selectedCount--;
                mData.get(position).selected = false;
                notifyDataSetChanged();
            } else if (selectedCount < MAX_COUNT) {
                selectedCount++;
                mData.get(position).selected = true;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecommendCircleBean item) {
        ImageView imageView = helper.setText(R.id.tvTitle, item.name).getView(R.id.civ_card);
        ImageLoader.loadImg(imageView, item.thumb);
        helper.itemView.setSelected(item.selected);
    }

    public String getSelectedCircleIds() {
        if (mData == null || mData.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (RecommendCircleBean model : mData) {
            if (model.selected) builder.append(model.id).append(",");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void addSelectedCount() {
        selectedCount++;
    }

    public void clearSelectedCount() {
        selectedCount = 0;
    }
}