package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_comment.utils.ImageLoader;

public class PostCardCateAdapter extends BaseQuickAdapter<RecommendCircleBean, BaseViewHolder> {

    private int selectedPosition = -1;

    public PostCardCateAdapter() {
        super(R.layout.chat_item_card_select);
        setOnItemClickListener((adapter, view, position) -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecommendCircleBean item) {
        ImageView imageView = helper.setText(R.id.tvTitle, item.name).getView(R.id.civ_card);
        ImageLoader.loadImg(imageView, item.thumb);
        helper.itemView.setSelected(selectedPosition == helper.getAdapterPosition());
    }

    public RecommendCircleBean getSelectedItem() {
        if (mData == null || mData.size() == 0 && selectedPosition > 0) return null;
        return mData.get(selectedPosition);
    }
}