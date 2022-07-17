package com.fengwo.module_flirt.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CoverDto;


/**
 * @Author BLCS
 * @Time 2020/3/27 14:54
 */
public class NearbyImageAdapter extends BaseQuickAdapter<CoverDto, BaseViewHolder> {
    /**
     * recyclerView宽度
     */
    private int rvWidth;

    /**
     * item的间隙
     */
    private int gap;

    public NearbyImageAdapter() {
        super(R.layout.include_circle_imageview);
    }

    public NearbyImageAdapter(int rvWidth, int gap) {
        super(R.layout.include_circle_imageview);
        this.rvWidth = rvWidth;
        this.gap = gap;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CoverDto data) {
        ImageView view = holder.getView(R.id.civ_icon);
        if (getItemCount() == 1) {
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rvWidth - gap));
        } else if (getItemCount() == 2 || getItemCount() == 4) {
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rvWidth / 2 - gap));
        } else {
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rvWidth / 3 - gap));
        }
        String url = TextUtils.isEmpty(data.videoImgUrl)?data.imageUrl:data.videoImgUrl;
        ImageLoader.loadRouteImg(view, url, 8);
        /*  data.getType() 1图片，2视频封面*/
        holder.getView(R.id.iv_video_play).setVisibility(data.typeNew == 2 ? View.VISIBLE : View.GONE);
    }

}
