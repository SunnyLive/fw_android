package com.fengwo.module_flirt.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.widget.RoundImageView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.PersonNearBy;
import com.fengwo.module_flirt.utlis.CommonUtils;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * @Author BLCS
 * @Time 2020/3/27 14:06
 */
public class NearbyAdapter extends BaseQuickAdapter<CityHost, BaseViewHolder> {


    public NearbyAdapter() {
        super(R.layout.adapter_nearby);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CityHost item) {
        ImageView avator = holder.getView(R.id.civ_head);
        ImageLoader.loadCircleImg(avator, item.getHeadImg());
        holder.setText(R.id.tv_name, item.getNickname());
        TextView tvAge = holder.getView(R.id.tv_age);
        CommentUtils.setSexAndAge(mContext, item.getAnchorId(), item.getSex(), item.getAge(), tvAge);
        holder.setText(R.id.tv_signature, item.getSignature());
        holder.setTextColor(R.id.tv_online, item.getLastTime().equals("在线") ? ContextCompat.getColor(mContext, R.color.purple_9966ff) : ContextCompat.getColor(mContext, R.color.text_66));
        holder.setText(R.id.tv_online, item.getLastTime());
        TextView tvTime = holder.getView(R.id.tv_online);

        tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(tvTime.getText().toString().equals("在线") ? R.drawable.ic_dot : 0, 0, 0, 0);
        holder.setGone(R.id.tv_flirt, item.getLiveStatus() == 2);
        holder.setGone(R.id.tv_living, item.getBstatus() == 1);
//        if (item.getLiveStatus()==2){//秀场直播
//            ImageLoader.loadGif(view, R.drawable.gif_live_head);
//        }
//        if (item.getBstatus()==1){//i撩直播 /*直播状态 0未开播 1开播*/
//            ImageLoader.loadGif(view, R.drawable.gif_liao_head);
//        }
        /* 距离 */
        holder.setText(R.id.tv_distance, item.getDistance());
        /*图片展示*/
        RecyclerView mRv = holder.getView(R.id.rv_icon);
        mRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        NearbyImageAdapter nearbyImageAdapter = new NearbyImageAdapter();
        mRv.setAdapter(nearbyImageAdapter);
        nearbyImageAdapter.setNewData(item.getImgList());

        /*TODO 实名认证*/
//        holder.setGone(R.id.tv_cer_mine,item)
    }

}
