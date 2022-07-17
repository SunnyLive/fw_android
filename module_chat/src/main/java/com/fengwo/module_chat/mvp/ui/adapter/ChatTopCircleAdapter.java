package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ScreenUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 社交圈顶部圈list
 * @author chenshanghui
 * @intro
 * @date 2019/9/6
 */
public class ChatTopCircleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int itemWidth;

    public ChatTopCircleAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.chat_item_top_circle, data);
        itemWidth = (int)( (ScreenUtils.getScreenWidth(context)-context.getResources().getDimension(R.dimen.dp_10) *2)) / 5 ;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        ImageView ivIcon = helper.getView(R.id.iv_circle_icon);
        ViewGroup.LayoutParams lp = helper.itemView.getLayoutParams();
        lp.width = itemWidth;
        helper.itemView.setLayoutParams(lp);
    }
}
