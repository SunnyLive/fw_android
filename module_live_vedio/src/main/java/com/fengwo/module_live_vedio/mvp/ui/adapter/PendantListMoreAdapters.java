package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MakeExpansionDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class PendantListMoreAdapters extends BaseQuickAdapter<PendantListDto.TextStickersBean, BaseViewHolder> {


    public PendantListMoreAdapters(int layoutResId, @Nullable List<PendantListDto.TextStickersBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PendantListDto.TextStickersBean item) {
        ImageView im_pic = helper.getView(R.id.im_pic);
        ImageLoader.loadImg(im_pic,item.getStickerUrl());

    }
}
