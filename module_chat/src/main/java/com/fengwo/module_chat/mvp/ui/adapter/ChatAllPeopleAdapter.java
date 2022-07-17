package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_comment.utils.ImageLoader;

public class ChatAllPeopleAdapter extends BaseQuickAdapter<GroupMemberModel, BaseViewHolder> {

    public ChatAllPeopleAdapter() {
        super(R.layout.chat_item_chatallpeople);
    }

    public void setCheck(int p) {
        getItem(p).isSelected = !getItem(p).isSelected;
        notifyDataSetChanged();
    }

    public void setCheck(GroupMemberModel item) {
        for (int i = 0; i < getItemCount(); i++) {
            if (item.userId == getItem(i).userId) {
                setCheck(i);
                return;
            }
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroupMemberModel item) {
        ImageView ivCheck = helper.getView(R.id.iv_check);
        if (item.isSelected) {
            ivCheck.setImageResource(R.drawable.ic_chat_checked);
        } else {
            ivCheck.setImageResource(R.drawable.ic_chat_unchecked);
        }
        helper.setText(R.id.tv_name, item.nickname);
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.headImg);
    }
}