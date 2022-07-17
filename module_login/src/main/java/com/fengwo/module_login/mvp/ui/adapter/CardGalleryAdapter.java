package com.fengwo.module_login.mvp.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.MineCardDto;

public class CardGalleryAdapter  extends BaseQuickAdapter<MineCardDto.CoverBean, BaseViewHolder> {
    /**
     * recyclerView 宽度
     */
    private int rvWidth;

    /**
     *  item的间隙
     */
    private int gap;


    public CardGalleryAdapter(int rvWidth, int gap) {
        super(R.layout.include_circle_imageview);
        this.rvWidth = rvWidth;
        this.gap = gap;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder holder, MineCardDto.CoverBean item) {
        ImageView view = holder.getView(R.id.civ_icon);
        if (getItemCount() == 1) {
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rvWidth - gap));
        } else if (getItemCount() == 2 || getItemCount() == 4) {
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rvWidth / 2 - gap));
        } else {
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rvWidth / 3 - gap));
        }
        String url = TextUtils.isEmpty(item.getVideoImgUrl())?item.getImageUrl():item.getVideoImgUrl();
        ImageLoader.loadRouteImg(view, url, 8);
        holder.getView(R.id.iv_video_play).setVisibility(item.getTypeNew() == 2 ? View.VISIBLE : View.GONE);
    }
}
