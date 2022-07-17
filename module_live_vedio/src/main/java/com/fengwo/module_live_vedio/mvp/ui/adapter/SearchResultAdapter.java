package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.SearchDto;

import java.util.List;

public class SearchResultAdapter extends BaseQuickAdapter<SearchDto, BaseViewHolder> {
    @Autowired
    UserProviderService userProviderService;

    public SearchResultAdapter(@Nullable List<SearchDto> data) {
        super(R.layout.live_item_search_channel, data);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchDto item) {
        ImageView headerIv = helper.getView(R.id.civ_header);
        ImageLoader.loadRouteImg(headerIv, item.getHeadImg(), 8);
        CommentUtils.setSexAndAge(mContext, 0, item.getSex(), item.getAge(), helper.getView(R.id.tv_age));
//            ImageView ivSex = helper.getView(R.id.iv_sex);
//            if (item.getSex() == 0) {
//                ivSex.setVisibility(View.GONE);
//            } else {
//            ivSex.setVisibility(View.VISIBLE);
//            if (item.getSex() == 1) {
//                ivSex.setImageResource(R.drawable.live_ic_boy);
//            } else {
//                ivSex.setImageResource(R.drawable.live_ic_girl);
//            }
//        }

        helper.setText(R.id.tv_name, item.getNickname());
        helper.setText(R.id.tv_des, item.getSignature());
        TextView btnAttention = helper.getView(R.id.btn_attention);
//        if (item.getAttention() > 0) {
//            btnAttention.setText("已关注");
//            btnAttention.setTextColor(mContext.getResources().getColor(R.color.text_66));
//            btnAttention.setBackgroundResource(R.drawable.live_search_bg_btn_attention);
//        } else {
//            btnAttention.setText("关注");
//            btnAttention.setTextColor(Color.WHITE);
//            btnAttention.setBackgroundResource(R.drawable.radius_purple_bg);
//        }
        helper.setTextColor(R.id.btn_attention, item.getAttention() > 0 ? helper.getView(R.id.btn_attention).getResources().getColor(R.color.text_99)
                : Color.WHITE);
        helper.setBackgroundRes(R.id.btn_attention, item.getAttention() > 0 ? R.drawable.bg_attention_status
                : R.drawable.bg_attention_status);
        if (item.getAttention() > 0) {
            helper.setText(R.id.btn_attention, "已关注");
            helper.setBackgroundRes(R.id.btn_attention, R.drawable.bg_attention_status);
            helper.setTextColor(R.id.btn_attention,  helper.getView(R.id.btn_attention).getResources().getColor(R.color.gray_999999));
        }else {
            helper.setText(R.id.btn_attention,  "关注");
            helper.setBackgroundRes(R.id.btn_attention, R.drawable.bg_attention_status_red_all);
            helper.setTextColor(R.id.btn_attention,  helper.getView(R.id.btn_attention).getResources().getColor(R.color.text_white_arr));
        }


        btnAttention.setVisibility(userProviderService.getUserInfo().id == item.getId() ? View.INVISIBLE : View.VISIBLE);
        helper.addOnClickListener(R.id.btn_attention);
//        int hostLevel = item.getHostLevel();
//        if (hostLevel > 50) {
//            hostLevel = 50;
//        }
//        int userLevel = item.getUserLevel();
//        if (userLevel <= 0) {
//            userLevel = 1;
//        }
//        if (userLevel > 47) {
//            userLevel = 47;
//        }
//        helper.setImageResource(R.id.iv_level, ImageLoader.getResId("login_ic_v" + userLevel, R.drawable.class));
//        if (hostLevel > 0)
//            helper.setImageResource(R.id.iv_host_level, ImageLoader.getResId("login_ic_type3_v" + hostLevel, R.drawable.class));
        if(item.getLiveStatus()==1||item.getChannelStatus()==2){
            helper.setGone(R.id.rl_zx_back,true);
        }else {
            helper.setGone(R.id.rl_zx_back,false);
        }
        ImageView sexGif = helper.getView(R.id.gif_sex);
        if(item.getChannelStatus() == 2){
            helper.setBackgroundRes(R.id.rl_zx_back,R.drawable.bg_zx_man);
            ImageLoader.loadGif(sexGif,R.drawable.gif_nan);

        }
        if(item.getLiveStatus() == 1){
            helper.setBackgroundRes(R.id.rl_zx_back,R.drawable.bg_zx_female);
            ImageLoader.loadGif(sexGif,R.drawable.gif_nv);
        }
        //==========
//        TextView tvFlirt = helper.getView(R.id.tv_flirt);
//        TextView tvLiving = helper.getView(R.id.tv_living);
//        if (item.getChannelStatus() == 2) {//秀场直播状态
//            tvLiving.setVisibility(View.VISIBLE);
//            tvFlirt.setVisibility(View.GONE);
//        } else if (item.getLiveStatus() == 1) {//i撩直播状态
//            tvLiving.setVisibility(View.GONE);
//            tvFlirt.setVisibility(View.VISIBLE);
//        } else {
//            tvLiving.setVisibility(View.GONE);
//            tvFlirt.setVisibility(View.GONE);
//        }
    }
}
