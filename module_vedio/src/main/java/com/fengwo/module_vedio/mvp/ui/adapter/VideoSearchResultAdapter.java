package com.fengwo.module_vedio.mvp.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;
import com.fengwo.module_vedio.mvp.ui.activity.ShowSearchAllActivity;
import com.fengwo.module_vedio.mvp.ui.activity.SmallVedioDetailActivity;
import com.fengwo.module_vedio.mvp.ui.activity.shortvideo.ShortVideoActivity;
import com.tencent.liteav.demo.player.common.utils.TCUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoSearchResultAdapter extends BaseQuickAdapter<String, BaseVH> {
    private VideoSearchDto data;
    private String content;
    public static final String TYPE_SHOW = "TYPE_SHOW";
    public static final String SEARCH_CONTENT = "SEARCH_CONTENT";
    public static final String TYPE_MOVIE = "TYPE_MOVIE";
    public static final String TYPE_VIDEO = "TYPE_VIDEO";

    public VideoSearchResultAdapter() {
        super(R.layout.adapter_video_search_result);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, String item) {
        int videoSize = data.getVideoInfoArr().getTotal();
        int movieSize = data.getMovieInfoArr().getTotal();
        boolean video = videoSize > 0 && helper.getAdapterPosition() == 0;
        helper.setText(R.id.ll_adapter_video_search_title, video ? mContext.getString(R.string.video_related) : mContext.getString(R.string.video_related_short));
        LinearLayout searchContent = helper.getView(R.id.ll_adapter_video_search);
        searchContent.removeAllViews();
        if (videoSize > 0 && helper.getAdapterPosition() == 0) {
            for (int i = 0; i < (videoSize >= 3 ? 3 : videoSize); i++) {
                List<VideoHomeShortModel> records = data.getVideoInfoArr().getRecords();
                searchContent.addView(initUI(records, records.get(i).videoTitle, i, video));
            }
            // show  all  UI
            helper.setVisible(R.id.tv_adapter_search_result_all, videoSize > 3 ? true : false);
        } else {
            for (int i = 0; i < (movieSize >= 3 ? 3 : movieSize); i++) {
                List<VideoHomeShortModel> records = data.getMovieInfoArr().getRecords();
                searchContent.addView(initUI(records, records.get(i).movieTitle, i, video));
            }
            helper.setVisible(R.id.tv_adapter_search_result_all, movieSize > 3 ? true : false);
        }
        //click search all
        helper.getView(R.id.tv_adapter_search_result_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show search result all
                Intent intent = new Intent(mContext, ShowSearchAllActivity.class);
                intent.putExtra(TYPE_SHOW, videoSize > 0 && helper.getAdapterPosition() == 0 ? TYPE_VIDEO : TYPE_MOVIE);
                intent.putExtra(SEARCH_CONTENT, content);
                mContext.startActivity(intent);
            }
        });
    }

    // add item
    public View initUI(List<VideoHomeShortModel> models, String titles, int pos, boolean isVideo) {
        VideoHomeShortModel model = models.get(pos);
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_video_search_result, null);
        TextView time = view.findViewById(R.id.tv_adapter_search_result_time);//时间
        TextView title = view.findViewById(R.id.tv_adapter_search_result_title);//标题
        TextView name = view.findViewById(R.id.tv_adapter_search_result_name);//name
        TextView date = view.findViewById(R.id.tv_adapter_search_result_date);//date
        float dimension = mContext.getResources().getDimension(R.dimen.dp_5);
        ImageLoader.loadRouteImg(view.findViewById(R.id.iv_adapter_search_result), model.cover, DensityUtils.px2dp(mContext, dimension));
        time.setText(TCUtils.formattedTime(model.duration));
        name.setText("作者：" + model.userName);
        date.setText(TimeUtils.dealInstan(model.createTime));
        title.setText(titles);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideo) {
                    SmallVedioDetailActivity.startActivity(mContext, pos, new ArrayList<VideoHomeShortModel>(models), -1, content, 1);
                } else {
                    ShortVideoActivity.startShortVideo(mContext, 0, model);
                }
            }
        });
        return view;
    }

    /**
     * init data
     */
    public void setCustomData(VideoSearchDto data, String content, View view) {
        this.data = data;
        this.content = content;
        ArrayList<String> objects = new ArrayList<>();
        if (data != null) {
            if (data.getMovieInfoArr().getTotal() > 0 && data.getVideoInfoArr().getTotal() > 0) {
                objects.add("");
                objects.add("");
            } else if (data.getMovieInfoArr().getTotal() > 0 || data.getVideoInfoArr().getTotal() > 0) {
                objects.add("");
            } else {
                if (view != null) setEmptyView(view);
            }
            setNewData(objects);
        } else {
            setNewData(null);
        }
    }

}
