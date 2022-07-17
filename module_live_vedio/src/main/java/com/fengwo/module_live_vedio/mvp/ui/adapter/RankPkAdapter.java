package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.RankSinglePkDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public class RankPkAdapter extends BaseQuickAdapter<RankSinglePkDto, BaseViewHolder> {
    public RankPkAdapter(@Nullable List<RankSinglePkDto> data) {
        super(R.layout.live_item_rankpk, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, RankSinglePkDto item) {
        helper.addOnClickListener(R.id.iv_header);
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg());
        int level = Integer.parseInt(item.getUserLevel());
        int level2 = item.getChannelId();
        if (level2 <= 0) {
            level2 = 1;
        }
        if (level <= 0) {
            level = 1;
        }
        if (level > 46) {
            level = 47;
        }
        if (level2 > 50) {
            level2 = 50;
        }
        helper.setImageResource(R.id.iv_user_level, ImageLoader.getResId("login_ic_v" + level, R.drawable.class));
        helper.setImageResource(R.id.iv_host_level, ImageLoader.getResId("login_ic_type3_v" + level2, R.drawable.class));

        helper.setText(R.id.tv_nick_name, item.getNickname());
        helper.setText(R.id.tv_pk_result, item.getVector() + "胜" + item.getLoss() + "负");

        TextView tvAttention = helper.getView(R.id.tv_attention);
        if (item.getIsAttention() == 1) {//已关注
            tvAttention.setBackgroundResource(R.drawable.shape_gray_corner);
            tvAttention.setTextColor(mContext.getResources().getColor(R.color.purple_9B7CF1));
            tvAttention.setText("已关注");
        } else {
            tvAttention.setBackgroundResource(R.drawable.live_bg_btn_attention);
            tvAttention.setTextColor(mContext.getResources().getColor(R.color.white));
            tvAttention.setText("关注");
        }
        int position = helper.getLayoutPosition();
        ImageView ivPosition = helper.getView(R.id.iv_position);
        TextView tvPosition = helper.getView(R.id.tv_position);
        if (position < 3) {
            ivPosition.setVisibility(View.VISIBLE);
            tvPosition.setVisibility(View.INVISIBLE);
        } else {
            ivPosition.setVisibility(View.INVISIBLE);
            tvPosition.setVisibility(View.VISIBLE);
            tvPosition.setText(position + 1 + "");
        }
        if (position == 0) {
            ivPosition.setImageResource(R.drawable.live_ic_tuhao1);
        } else if (position == 1) {
            ivPosition.setImageResource(R.drawable.live_ic_tuhao2);
        } else if (position == 2) {
            ivPosition.setImageResource(R.drawable.live_ic_tuhao3);
        }
        helper.addOnClickListener(R.id.tv_attention, R.id.iv_header);
    }
}
