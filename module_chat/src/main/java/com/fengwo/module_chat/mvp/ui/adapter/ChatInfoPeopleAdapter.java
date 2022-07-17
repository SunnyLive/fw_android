package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_comment.utils.ImageLoader;

public class ChatInfoPeopleAdapter extends BaseMultiItemQuickAdapter<GroupMemberModel, BaseViewHolder> {

    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REMOVE = 2;

    public ChatInfoPeopleAdapter() {
        super(null);
        addItemType(TYPE_CONTENT, R.layout.chat_item_chat_people);
        addItemType(TYPE_REMOVE, R.layout.chat_item_chat_people);
        addItemType(TYPE_ADD, R.layout.chat_item_chat_people);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroupMemberModel item) {
        ImageView imageView = helper.getView(R.id.iv);
        TextView textView = helper.getView(R.id.tv);
        switch (helper.getItemViewType()) {
            case TYPE_CONTENT:
                ImageLoader.loadImg(imageView, item.headImg);
                helper.setText(R.id.tv, item.nickname);
                break;
            case TYPE_REMOVE:
                imageView.setImageResource(R.drawable.ic_chat_member_remove);
                textView.setText("移除");
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_66));
                break;
            case TYPE_ADD:
                imageView.setImageResource(R.drawable.ic_chat_member_add);
                textView.setText("添加");
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_66));
                break;
        }
    }
}
