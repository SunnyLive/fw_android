package com.fengwo.module_flirt.adapter;

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
import com.fengwo.module_flirt.R;
import com.fengwo.module_live_vedio.mvp.dto.SearchDto;

import java.util.List;

public class SearchResultWenBoAdapter extends BaseQuickAdapter<SearchDto, BaseViewHolder> {
    @Autowired
    UserProviderService userProviderService;
    public SearchResultWenBoAdapter(@Nullable List<SearchDto> data) {
        super(R.layout.flirt_item_search_channel, data);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchDto item) {
        ImageView headerIv = helper.getView(R.id.iv_header);
        ImageLoader.loadRouteImg(headerIv, item.getHeadImg(),8);
        CommentUtils.setSexAndAge(mContext,0,item.getSex(),item.getAge(),helper.getView(com.fengwo.module_live_vedio.R.id.tv_age));
//        ImageView ivSex = helper.getView(R.id.iv_sex);
//        if (item.getSex() == 0) {
//            ivSex.setVisibility(View.GONE);
//        } else {
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
        if (item.getAttention() > 0) {
            btnAttention.setText("已关注");
            btnAttention.setTextColor(mContext.getResources().getColor(R.color.purple_9966ff));
            btnAttention.setBackgroundResource(R.drawable.live_search_bg_btn_attention);
        } else {
            btnAttention.setText("关注");
            btnAttention.setTextColor(mContext.getResources().getColor(R.color.white));
            btnAttention.setBackgroundResource(R.drawable.radius_purple_bg);
        }
        btnAttention.setVisibility(userProviderService.getUserInfo().id==item.getId()?View.INVISIBLE:View.VISIBLE);
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
//        helper.setGone(R.id.iv_level,false);
//        helper.setGone(R.id.iv_host_level,false);
        TextView tvFlirt = helper.getView(R.id.tv_flirt);
        TextView tvLiving = helper.getView(R.id.tv_living);
        if (item.getLiveStatus() == 1){//i撩直播状态
            tvFlirt.setVisibility(View.VISIBLE);
            tvLiving.setVisibility(View.GONE);
        }else if(item.getChannelStatus() == 2){//秀场直播状态
            tvFlirt.setVisibility(View.GONE);
            tvLiving.setVisibility(View.VISIBLE);
        }else {
            tvFlirt.setVisibility(View.GONE);
            tvLiving.setVisibility(View.GONE);
        }
    }
}
