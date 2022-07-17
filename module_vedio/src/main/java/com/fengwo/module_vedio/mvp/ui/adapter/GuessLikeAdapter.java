package com.fengwo.module_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;

import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto.ShortVedio;

import java.util.List;

public class GuessLikeAdapter extends BaseQuickAdapter<ShortVedio, BaseVH> {

    private int nowPage = 1;
    private int allPage;
    private int maxShow = -1;

    public GuessLikeAdapter(int layoutResId, @Nullable List<ShortVedio> data) {
        super(layoutResId, data);
    }

    public GuessLikeAdapter(int layoutResId, @Nullable List<ShortVedio> data, int maxShow) {
        super(layoutResId, data);
        this.maxShow = maxShow;
        allPage = data.size() % maxShow == 0 ? data.size() / maxShow : data.size() / maxShow + 1;
    }

    @Override
    protected void convert(@NonNull BaseVH helper, ShortVedio item) {
        helper.setText(R.id.title_tv, item.movieTitle);
        helper.addOnClickListener(R.id.root);
        ImageLoader.loadImg(helper.getView(R.id.iv_cover), item.cover);
    }

    @Override
    public int getItemCount() {
        if (nowPage < allPage) {
            return maxShow;
        } else {
            if (mData.size() % maxShow == 0) {
                return maxShow;
            } else {
                return mData.size() % maxShow;
            }
        }
    }

    public void nextPage() {
        nowPage++;
        if (nowPage > allPage) {
            nowPage = 1;
        }
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public ShortVedio getItem(int position) {
        return super.getItem(position + (nowPage - 1) * maxShow);
    }
}
