package com.fengwo.module_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_vedio.R;
import com.tencent.liteav.demo.player.common.utils.TCUtils;

import static com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchResultAdapter.TYPE_MOVIE;

public class VideoSearchAllAdapter extends BaseQuickAdapter<VideoHomeShortModel, BaseVH> {
    private String type;

    public VideoSearchAllAdapter(String type) {
        super(R.layout.include_video_search_result);
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseVH helper, VideoHomeShortModel item) {
        float dimension = mContext.getResources().getDimension(R.dimen.dp_10);
        ImageLoader.loadRouteImg(helper.getView(R.id.iv_adapter_search_result), item.cover, DensityUtils.px2dp(mContext, dimension));
        helper.setText(R.id.tv_adapter_search_result_time, TCUtils.formattedTime(item.duration));
        helper.setText(R.id.tv_adapter_search_result_name, "作者：" + item.userName);
        helper.setText(R.id.tv_adapter_search_result_date, TimeUtils.dealInstan(item.createTime));
        helper.setText(R.id.tv_adapter_search_result_title, type.equals(TYPE_MOVIE) ? item.movieTitle : item.videoTitle);
    }
}
