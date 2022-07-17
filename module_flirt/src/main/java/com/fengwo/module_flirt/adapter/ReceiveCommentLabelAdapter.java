package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CommentTagDto;

import java.util.Arrays;
import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/10 15:13
 */
public class ReceiveCommentLabelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int[] back = {R.drawable.bg_tag_2, R.drawable.bg_tag_3, R.drawable.bg_tag_4, R.drawable.bg_tag_1};
    private List<String> text = Arrays.asList("#FE3C9C", "#FF6B5B", "#ECAB24", "#54BDFF");

    public ReceiveCommentLabelAdapter() {
        super(R.layout.adapter_receive_comment_label);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String data) {
        baseViewHolder.setText(R.id.tv_card_label, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        LinearLayoutCompat cardLabel = holder.getView(R.id.cv_card_label);

        cardLabel.setBackgroundResource(back[position % back.length]);

        holder.setTextColor(R.id.tv_card_label, Color.parseColor(text.get(position % text.size())) );
    }
}
