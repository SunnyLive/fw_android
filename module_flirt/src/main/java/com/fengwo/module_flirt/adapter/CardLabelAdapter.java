package com.fengwo.module_flirt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_flirt.R;

/**
 * @Author BLCS
 * @Time 2020/3/30 15:02
 */
public class CardLabelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private final int[] fontColors = {R.color.color_FE3C9C,R.color.color_46AEFF,R.color.color_FEBE49,R.color.color_FE5349};
    //private final int[] bgColors = {R.drawable.ic_bg_label1,R.drawable.ic_bg_label2,R.drawable.ic_bg_label3,R.drawable.ic_bg_label4};
    public CardLabelAdapter() {
        super(R.layout.adapter_card_label);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder mHolder, String table) {
        int fontColor = fontColors[(mHolder.getAdapterPosition() % 4)];
//        int bgColor = bgColors[(mHolder.getAdapterPosition() % 4)];
        int bgColor = R.drawable.bg_cplor_tag;
        CardView cv = mHolder.getView(R.id.cv_card_label);
        cv.setBackgroundResource(bgColor);
        mHolder.setText(R.id.tv_card_label,table);
        mHolder.setTextColor(R.id.tv_card_label, ContextCompat.getColor(mContext,fontColor));
    }
}
