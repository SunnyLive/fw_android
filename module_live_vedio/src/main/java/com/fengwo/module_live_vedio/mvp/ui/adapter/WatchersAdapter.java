package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;

import java.util.List;

public class WatchersAdapter extends BaseQuickAdapter<WatcherDto, BaseViewHolder> {
    private final int[] resShouhuBg = {R.drawable.live_ic_shouhu1, R.drawable.live_ic_shouhu2, R.drawable.live_ic_shouhu3};

    public WatchersAdapter(@Nullable List<WatcherDto> data) {
        super(R.layout.live_item_watcher, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, WatcherDto item) {
        int position = holder.getAdapterPosition();
        WatcherDto dto = mData.get(position);
        ImageView iv = holder.getView(R.id.iv);
        ImageView ivLevel = holder.getView(R.id.iv_level);
        if (position < resShouhuBg.length && dto.consumNums > 0) {
            iv.setImageResource(resShouhuBg[position]);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
        ImageLoader.loadImg(holder.getView(R.id.iv_header), dto.headImg, R.drawable.ic_default_head);
        ivLevel.setImageResource(ImageLoader.getResId(ImageLoader.getLiveUserLevel(dto.userLevel), R.drawable.class));   //设置用户头像
        holder.addOnClickListener(R.id.iv_header);
    }
}
