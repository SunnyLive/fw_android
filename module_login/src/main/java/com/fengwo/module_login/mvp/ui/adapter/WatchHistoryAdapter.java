package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.WatchHistoryDto;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;

import java.util.ArrayList;

public class WatchHistoryAdapter extends BaseQuickAdapter<WatchHistoryDto, BaseViewHolder> {

    public WatchHistoryAdapter() {
        super(R.layout.item_watch_history);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WatchHistoryDto item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg());
        ImageView living = helper.getView(R.id.iv_living);
        FrameLayout flLiving = helper.getView(R.id.fl_is_living);
        helper.setText(R.id.tv_name, item.getNickname());
        if (item.isLiving()) {
            flLiving.setVisibility(View.VISIBLE);
            living.setVisibility(View.VISIBLE);
            ImageLoader.loadGif(living, R.drawable.ic_anchor_living);
        } else {
            living.setVisibility(View.GONE);
            flLiving.setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.rl_history);
//        helper.getView(R.id.rl_history).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.getStatus() == 2) {
//                    ArrayList<ZhuboDto> list = new ArrayList<>();
//                    ZhuboDto zhuboDto = new ZhuboDto();
//                    zhuboDto.channelId = item.getChannelId();
//                    list.add(zhuboDto);
//                    LivingRoomActivity.start(mContext, list, 0, true);
//                }  else {
//                    MineDetailActivity.startActivityWithUserId(mContext, item.getChannelId());
//                }
//            }
//        });

    }
}
