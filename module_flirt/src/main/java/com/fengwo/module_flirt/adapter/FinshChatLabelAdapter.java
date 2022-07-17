package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
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
public class FinshChatLabelAdapter extends BaseQuickAdapter<CommentTagDto, BaseViewHolder> {
    private SparseArray<Integer> clickLabels;
    private List<String> colors = Arrays.asList("#99CCFF", "#99A2FF", "#FF9999", "#DD99FF", "#FFE499");
    private int[] back = {R.drawable.bg_tag_1, R.drawable.bg_tag_2, R.drawable.bg_tag_3, R.drawable.bg_tag_4};
    private List<String> text = Arrays.asList("#54BDFF", "#FE3C9C", "#FF6B5B", "#ECAB24");

    public FinshChatLabelAdapter() {
        super(R.layout.adapter_finish_label);
        clickLabels = new SparseArray<>();
        clickLabels.put(0, 1);//默认选中第一个
    }

    public String getValue() {
        StringBuilder builder = new StringBuilder();
        if (clickLabels.size() == 0) return "";
        for (int i = 0; i < clickLabels.size(); i++) {
            int key = clickLabels.keyAt(i);
            if (clickLabels.get(key, 0) == 1) {
                builder.append(getData().get(key).evaluationName);
                if (i < clickLabels.size() - 1)
                    builder.append(",");
            }
        }
        return builder.toString();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CommentTagDto data) {
        baseViewHolder.setText(R.id.tv_card_label, data.evaluationName);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CardView cardView = holder.getView(R.id.cv_card_label);
//        cardView.setCardBackgroundColor(clickLabels.get(position, 0) > 0
//                ?
//                Color.parseColor(colors.get(position % colors.size())) : mContext.getResources().getColor(R.color.gray_eeeeee));

        cardView.setBackgroundResource(clickLabels.get(position, 0) > 0
                ?
                  R.drawable.bg_tag_03:back[position % back.length]);

        holder.setTextColor(R.id.tv_card_label, clickLabels.get(position, 0) > 0
                ?
                 mContext.getResources().getColor(R.color.text_white_arr):Color.parseColor(text.get(position % text.size())) );
        holder.getView(R.id.cv_card_label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickLabels.get(position, 0) > 0) {
                    clickLabels.put(position, 0);
                    KLog.e("tag","移除"+getData().get(position).evaluationName);
                } else {
                    clickLabels.put(position, 1);
                    KLog.e("tag","选中"+getData().get(position).evaluationName);
                }
                notifyDataSetChanged();
            }
        });
    }
}
