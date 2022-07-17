package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.RankTuhaoDto;

import java.math.BigDecimal;
import java.util.List;

public class RankTuhaoAdapter extends BaseQuickAdapter<RankTuhaoDto, BaseViewHolder> {


    public RankTuhaoAdapter(int layoutResId, @Nullable List<RankTuhaoDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RankTuhaoDto item) {
        int position = helper.getLayoutPosition();
        helper.setText(R.id.tv_nick_name, item.getNickname());
        helper.setText(R.id.tv_money, "花钻" + DataFormatUtils.formatNumbers(item.getScore()));
        helper.setText(R.id.tv_rank, position + 4 + "");
        ImageLoader.loadCircleImg(helper.getView(R.id.tv_header), item.getHeadImg());
        helper.addOnClickListener(R.id.tv_header);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TextView tvRank = holder.getView(R.id.tv_rank);
        ImageView ivRank = holder.getView(R.id.iv_rank);
        View flRank = holder.getView(R.id.fl_rank);
//        if (position < 3) {
//            flRank.setVisibility(View.VISIBLE);
//            tvRank.setVisibility(View.GONE);
//            ivRank.setImageResource(rankRes[position]);
//        } else {
//            flRank.setVisibility(View.GONE);
//            tvRank.setVisibility(View.VISIBLE);
//            tvRank.setText(position + 1 + "");
//        }
    }
}
