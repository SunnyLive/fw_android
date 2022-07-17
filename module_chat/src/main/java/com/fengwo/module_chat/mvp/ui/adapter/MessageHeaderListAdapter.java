package com.fengwo.module_chat.mvp.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.enums.MessageHeaderEnum;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;

import java.util.List;

@Deprecated
public class MessageHeaderListAdapter extends BaseQuickAdapter<List<ReceiveSocketBean>, BaseViewHolder> {

    public MessageHeaderListAdapter() {
        super(R.layout.chat_item_message);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, List<ReceiveSocketBean> item) {
        helper.addOnClickListener(R.id.root);

        ImageView ivHeader = helper.getView(R.id.ivHeader);
        ivHeader.setImageResource(MessageHeaderEnum.getIcon(helper.getAdapterPosition()));
        helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(helper.getAdapterPosition()));

        helper.setText(R.id.tvBadge, item.size() > 99 ? "99+" : item.size() + "");//未读消息
        helper.setVisible(R.id.tvBadge, item.size() > 0);

        //todo 是否有系统消息 有则显示最后一条
        if (item.size() > 0) {
            ReceiveSocketBean lastData = item.get(item.size() - 1);
            if (lastData != null) {
                helper.setText(R.id.tvSubtitle, lastData.getContent().getValue());
                if (!TextUtils.isEmpty(lastData.getAction())) {
                    String action = lastData.getAction();
                    L.e("====== " + action);
                    long aLong = Long.parseLong(action);
                    helper.setText(R.id.tvDate, ChatTimeUtils.getChatTime(aLong));
                }
            }
        }

    }
}
