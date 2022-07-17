package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_live_vedio.R;

import java.util.List;

public class SearchHotestAdapter extends BaseQuickAdapter<String, BaseVH> {

    String[] colors = {"#FFA200", "#5458E1", "#0583E4"};

    public SearchHotestAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, String item) {

    }

    @Override
    public void onBindViewHolder(BaseVH holder, int position) {
        super.onBindViewHolder(holder, position);
        GradientDrawable drawable = (GradientDrawable) holder.getView(R.id.tv_rank).getBackground();
        drawable.setColor(Color.parseColor(colors[position]));
        holder.setText(R.id.tv_rank, "0" + (position + 1));
    }
}
