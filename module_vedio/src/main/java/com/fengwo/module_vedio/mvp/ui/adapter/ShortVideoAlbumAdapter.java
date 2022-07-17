package com.fengwo.module_vedio.mvp.ui.adapter;

import android.view.View;

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
public class ShortVideoAlbumAdapter extends BaseQuickAdapter<VideoHomeShortModel, BaseViewHolder> {

    public ShortVideoAlbumAdapter(@Nullable List<VideoHomeShortModel> data) {
        super(R.layout.item_short_video_album, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VideoHomeShortModel item) {
        int position = helper.getLayoutPosition() + 1;
        helper.setText(R.id.tv_section, "第" + position + "集");
        ImageLoader.loadImg(helper.getView(R.id.iv_cover), item.cover);

        ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);

        helper.setVisible(R.id.ll_tag, item.isPlay ? true : false);

    }

    public void resetIsPlay(int p) {
        for (VideoHomeShortModel model : getData()) {
            model.isPlay = false;
        }
        getData().get(p).isPlay = true;
        notifyDataSetChanged();
    }
}
