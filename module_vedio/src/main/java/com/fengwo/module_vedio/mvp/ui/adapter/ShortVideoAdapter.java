package com.fengwo.module_vedio.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_vedio.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/26
 */
public class ShortVideoAdapter extends BaseQuickAdapter<VideoHomeShortModel, BaseViewHolder> {
    public ShortVideoAdapter(@Nullable List<VideoHomeShortModel> data) {
        super(R.layout.item_short_video_child, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VideoHomeShortModel item) {
        helper.setText(R.id.tv_like, item.likes + "");
        ImageLoader.loadImg(helper.getView(R.id.iv_cover), item.cover);
    }

    @Override
    public int getItemCount() {
        return getData().size() / 2 == 0 ? super.getItemCount() : super.getItemCount() + 1;
    }
}
