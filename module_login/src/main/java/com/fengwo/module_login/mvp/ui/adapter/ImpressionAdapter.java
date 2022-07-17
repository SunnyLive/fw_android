package com.fengwo.module_login.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;

import java.util.List;

public class ImpressionAdapter extends BaseQuickAdapter<UserInfo.TodayUserTime, BaseViewHolder> {
    public ImpressionAdapter(@Nullable List<UserInfo.TodayUserTime> data) {
        super(R.layout.live_item_guard, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo.TodayUserTime item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg());

    }
}
