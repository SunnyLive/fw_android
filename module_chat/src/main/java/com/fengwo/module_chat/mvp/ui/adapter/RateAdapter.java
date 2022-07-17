package com.fengwo.module_chat.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;

import java.util.Arrays;

public class RateAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    private double rate; // 评分

    public RateAdapter(double rate) {
        super(R.layout.chat_item_rate, Arrays.asList(1, 2, 3, 4, 5));
        this.rate = rate;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        ImageView imageView = helper.getView(R.id.image);
        if (rate >= item) {
            imageView.setImageResource(R.drawable.ic_rate_star_all);
        } else if (rate < item && item - rate < 1) {
            imageView.setImageResource(R.drawable.ic_rate_star_half);
        } else {
            imageView.setImageResource(R.drawable.ic_rate_star_empty);
        }
    }
}
