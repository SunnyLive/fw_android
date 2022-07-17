package com.fengwo.module_chat.widgets.chat;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;

import java.util.List;

public class EmojiAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public EmojiAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        ImageView iv = helper.getView(R.id.iv_emoji);
        iv.setImageResource(item);
        helper.addOnClickListener(R.id.iv_emoji);
    }
}
