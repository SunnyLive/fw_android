package com.fengwo.module_chat.mvp.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

/**
 * 搜索结果适配器
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/12
 */
public class ChatSearchResultAdapter extends BaseMultiItemQuickAdapter<CircleListBean, BaseViewHolder> {

    public ChatSearchResultAdapter(@Nullable List<CircleListBean> data) {
        super(data);
        addItemType(0, R.layout.chat_item_home_stagger);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, CircleListBean item) {
        Group group = helper.getView(R.id.groupChat);
        ImageView civHeader = helper.getView(R.id.civChat);
        ImageView ivPoster = helper.getView(R.id.ivChatHeader);
        TextView tvTitle = helper.getView(R.id.tvChat);
        View location = helper.getView(R.id.view_location);
        TextView tvLocation = helper.getView(R.id.tvLocation);

        if (item.isAd == 1) {
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(mContext, 129);
            ImageLoader.loadImg(ivPoster, item.adImage);
            ivPoster.setLayoutParams(lp);
        } else {
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(mContext, 206);
            ivPoster.setLayoutParams(lp);
            ImageLoader.loadImg(ivPoster, item.cover);
        }
        location.setVisibility(TextUtils.isEmpty(item.position) ? View.INVISIBLE : View.VISIBLE);
        tvLocation.setText(item.position);
        tvTitle.setText(item.excerpt);
        tvTitle.setVisibility(TextUtils.isEmpty(item.excerpt) ? View.GONE : View.VISIBLE);
        group.setVisibility(TextUtils.isEmpty(item.nickname) ? View.GONE : View.VISIBLE);
        helper.setText(R.id.tvChatName, item.nickname).setText(R.id.tvChatNum, String.valueOf(item.likes));
        ImageLoader.loadImg(civHeader, item.headImg);
    }
}
