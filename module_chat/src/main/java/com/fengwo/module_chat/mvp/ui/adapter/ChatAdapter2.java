package com.fengwo.module_chat.mvp.ui.adapter;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengbo.module_chatlib.ui.msg.IMessageView;
import com.fengwo.module_chat.R;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class ChatAdapter2 extends BaseMultiItemQuickAdapter<IMessageView, BaseViewHolder> {

    public ChatAdapter2() {
        super(null);
        addItemType(1, R.layout.chat_item_msg_to);//我发的
        addItemType(2, R.layout.chat_item_msg_from);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, IMessageView item) {
        helper.setText(R.id.tv_time, item.getChatMessage().getAddTime());
        FrameLayout flContent = helper.getView(R.id.fl_content);
        View view = item.obtainMessageView(flContent, helper.getLayoutPosition());
        flContent.removeAllViews();
        flContent.addView(view);
        helper.addOnLongClickListener(R.id.fl_content);
    }
}