package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_comment.utils.ImageLoader;

public class ChatGroupResAdapter extends BaseQuickAdapter<EnterGroupModel.GroupInfoModel, BaseViewHolder> {
    public ChatGroupResAdapter() {
        super(R.layout.item_chat_group_res);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, EnterGroupModel.GroupInfoModel item) {
        ImageView imageView = helper.getView(R.id.iv);
        helper.setText(R.id.tv, item.merchantName);
        ImageLoader.loadImg(imageView, item.cover);
    }
}
