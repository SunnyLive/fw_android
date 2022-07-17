package com.fengwo.module_login.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_login.R;

import java.util.List;

/**
 * @author dqm
 * 今日贡献榜花蜜值
 */
public class MineTodayReceiveAdapter extends BaseQuickAdapter<LiveProfitDto.RecordsBean, BaseViewHolder> {
    public MineTodayReceiveAdapter(@Nullable List<LiveProfitDto.RecordsBean> data) {
        super(R.layout.live_item_guard,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper,LiveProfitDto.RecordsBean item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header),item.getHeadImg());

    }
}
