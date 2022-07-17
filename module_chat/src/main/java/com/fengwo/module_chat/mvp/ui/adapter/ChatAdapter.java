package com.fengwo.module_chat.mvp.ui.adapter;

import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.uimsg.IMessageView;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<IMessageView, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatAdapter(List<IMessageView> data) {
        super(data);
        addItemType(1, R.layout.chat_item_msg_from);
        addItemType(2, R.layout.chat_item_msg_to);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, IMessageView item) {
        if (item.getItemType() == 1){
            FrameLayout flContent = helper.getView(R.id.fl_content);
            View view = item.obtainMessageView(flContent);
            flContent.removeAllViews();
            flContent.addView(view);
        }else if (item.getItemType() == 2){
            FrameLayout flContent = helper.getView(R.id.fl_content);
            View view = item.obtainMessageView(flContent);
            flContent.removeAllViews();
            flContent.addView(view);
        }

    }
}
