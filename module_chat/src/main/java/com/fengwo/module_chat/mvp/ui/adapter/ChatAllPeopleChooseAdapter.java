package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_comment.utils.ImageLoader;

public class ChatAllPeopleChooseAdapter extends BaseQuickAdapter<GroupMemberModel, BaseViewHolder> {

    public ChatAllPeopleChooseAdapter() {
        super(R.layout.chat_item_allpeople_choose);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroupMemberModel item) {
        ImageView iv = helper.getView(R.id.iv);
        ImageLoader.loadImg(iv, item.headImg);
    }

    /**
     * 没有就添加 有就删除
     *
     * @param item
     */
    public void checkIfHasDelOrAdd(GroupMemberModel item, RecyclerView recyclerView) {
        for (int i = 0; i < mData.size(); i++) {
            if (item.userId == mData.get(i).userId) {
                remove(i);
                return;
            }
        }
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
        recyclerView.scrollToPosition(mData.size() - 1);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}