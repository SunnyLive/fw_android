package com.fengwo.module_vedio.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.dto.NewVideoDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/2
 */
public class NewVideoAdapter extends BaseQuickAdapter<NewVideoDto, BaseViewHolder> {
    public NewVideoAdapter(@Nullable List<NewVideoDto> data) {
        super(R.layout.vedio_item_new_video_list,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewVideoDto item) {
        helper.setText(R.id.tv_title,item.getPost_title());
        ImageLoader.loadImg(helper.getView(R.id.iv_thumb),item.getThumb());
        helper.addOnClickListener(R.id.iv_share);
    }
}
