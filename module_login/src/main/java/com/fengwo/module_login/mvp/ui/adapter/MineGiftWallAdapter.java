package com.fengwo.module_login.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.GiftWallDto;

import java.util.List;

/**
 * @author dqn
 */
public class MineGiftWallAdapter extends BaseQuickAdapter<GiftWallDto.PageList.GiftWall, BaseViewHolder> {
    public MineGiftWallAdapter(@Nullable List<GiftWallDto.PageList.GiftWall> data) {
        super(R.layout.item_mine_gift_wall,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GiftWallDto.PageList.GiftWall item) {
        ImageLoader.loadImg(helper.getView(R.id.iv_gift),item.getGiftIcon());

    }
}
