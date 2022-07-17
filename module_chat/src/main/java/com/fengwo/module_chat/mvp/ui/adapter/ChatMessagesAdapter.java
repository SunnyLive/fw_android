package com.fengwo.module_chat.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.presenter.MessagesPresenter;
import com.fengwo.module_chat.mvp.ui.activity.social.TestChatActivity;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 消息列表
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/11
 */
public class ChatMessagesAdapter extends BaseQuickAdapter<MessagesPresenter.Friend, BaseViewHolder> {

    public ChatMessagesAdapter(@Nullable List<MessagesPresenter.Friend> data) {
        super(R.layout.chat_item_messages, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessagesPresenter.Friend item) {
        TextView tvName = helper.getView(R.id.tv_name);
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        TextView tvLastMsg = helper.getView(R.id.tv_last_msg);
        TextView tvDate = helper.getView(R.id.tv_date);
        TextView tvUnreadNum = helper.getView(R.id.tv_unread_num);
        tvName.setText(item.nike_name);
        String url = "http://" + item.head_img_ip + "/headimgdir/" + item.head_img;
        ImageLoader.loadImg(ivAvatar, url);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChatActivity.start(mContext, 1, item.user_ids);
                TestChatActivity.start(mContext, 1 + "", item.user_ids);
            }
        });
    }
}
