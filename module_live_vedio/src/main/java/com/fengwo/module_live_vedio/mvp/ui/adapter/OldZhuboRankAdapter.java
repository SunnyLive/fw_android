package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.RankZhuboDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OldZhuboRankAdapter extends BaseQuickAdapter<RankZhuboDto, BaseViewHolder> {

    public OldZhuboRankAdapter(@Nullable List<RankZhuboDto> data) {
        super(R.layout.old_live_item_ranktop, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        super.onBindViewHolder(helper, position);
        if (position >= mData.size()) {
            return;
        }
        RankZhuboDto item = mData.get(position);
        ImageView ivRank = helper.getView(R.id.iv_rank);
        TextView tvRank = helper.getView(R.id.tv_rank);
        ImageView im_dw = helper.getView(R.id.im_dw);
        ImageView im_ph_back = helper.getView(R.id.im_ph_back);
        if (position <= 2) {
            ivRank.setVisibility(View.VISIBLE);
            tvRank.setVisibility(View.INVISIBLE);
            switch (position+1) {
                case 1:
                    ivRank.setVisibility(View.VISIBLE);
                    ivRank.setImageResource(R.drawable.pic_hour_one);
                    tvRank.setVisibility(View.INVISIBLE);
                    im_ph_back.setImageResource(R.drawable.pic_xsb_one);
                    im_dw.setImageResource(R.drawable.pic_dw_one);
                    break;
                case 2:
                    ivRank.setVisibility(View.VISIBLE);
                    ivRank.setImageResource(R.drawable.pic_hour_two);
                    tvRank.setVisibility(View.INVISIBLE);
                    im_ph_back.setImageResource(R.drawable.pic_xsb_two);
                    im_dw.setImageResource(R.drawable.pic_dw_two);
                    break;
                case 3:
                    ivRank.setVisibility(View.VISIBLE);
                    ivRank.setImageResource(R.drawable.pic_hour_three);
                    tvRank.setVisibility(View.INVISIBLE);
                    im_ph_back.setImageResource(R.drawable.pic_xsb_three);
                    im_dw.setImageResource(R.drawable.pic_dw_three);
                    break;

            }

//            int res = ImageLoader.getResId("live_ic_tuhao" + (position + 1), R.drawable.class);
//            ivRank.setImageResource(res);
        } else {
            ivRank.setVisibility(View.INVISIBLE);
            tvRank.setVisibility(View.VISIBLE);
            tvRank.setText("" + (position + 1));
            im_ph_back.setImageResource(0);
            im_dw.setImageResource(0);
        }
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.headImg);
        helper.setText(R.id.tv_name, item.nickname);
          helper.setText(R.id.tv_context, DataFormatUtils.formatNumbers(item.score));
        int level = item.userLevel;
        int level2 = item.anchorLevel;
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
        helper.setImageResource(R.id.iv_level1, ImageLoader.getResId("login_ic_type3_v" + level2, R.drawable.class));
        helper.setImageResource(R.id.iv_level2, ImageLoader.getResId("login_ic_v" + level, R.drawable.class));
        TextView tvAttention = helper.getView(R.id.btn_attention);
        if (item.isAttension == 1) {//已关注
            tvAttention.setBackgroundResource(0);
            tvAttention.setTextColor(mContext.getResources().getColor(R.color.color_acacac));
            tvAttention.setText("已关注");
        } else {
            tvAttention.setBackgroundResource(R.drawable.bg_follow);
            tvAttention.setTextColor(mContext.getResources().getColor(R.color.red_cf3d79));
            tvAttention.setText("关注");
        }
        helper.addOnClickListener(R.id.btn_attention, R.id.root);
//        ImageView living = helper.getView(R.id.iv_living);
//        if (item.liveStatus == 2) {
//            living.setVisibility(View.VISIBLE);
//            ImageLoader.loadGif(living, R.drawable.gif_living_purple);
//        } else {
//            living.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RankZhuboDto item) {

    }

}
