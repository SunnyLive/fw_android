package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.ChatCardImgBean;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.List;

public class ChatCardThumbAdapter extends BaseQuickAdapter<ChatCardImgBean, BaseViewHolder> {

    private int selectedPosition = 0;

    public ChatCardThumbAdapter() {
        super(R.layout.item_card_thumb);
    }

    public ChatCardThumbAdapter(List<ChatCardImgBean> list) {
        super(R.layout.item_card_thumb, list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatCardImgBean item) {
        CardView view = helper.getView(R.id.root);
        ImageView imageView = helper.getView(R.id.iv_chat_thumb);
        ImageLoader.loadImg(imageView, item.imageUrl);
        if (selectedPosition == helper.getAdapterPosition()) {
            view.setCardBackgroundColor(mContext.getResources().getColor(R.color.text_white));
        } else
            view.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
}