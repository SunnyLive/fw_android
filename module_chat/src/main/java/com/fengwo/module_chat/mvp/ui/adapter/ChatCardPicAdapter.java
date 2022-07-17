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

public class ChatCardPicAdapter extends BaseQuickAdapter<ChatCardImgBean, BaseViewHolder> {


    public ChatCardPicAdapter() {
        super(R.layout.item_chat);
    }

    public ChatCardPicAdapter(List<ChatCardImgBean> list) {
        super(R.layout.item_chat, list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatCardImgBean item) {
        ImageView imageView = helper.getView(R.id.root);
        ImageLoader.loadHightImage(imageView, item.imageUrl, R.drawable.bg_full_default);
    }

}