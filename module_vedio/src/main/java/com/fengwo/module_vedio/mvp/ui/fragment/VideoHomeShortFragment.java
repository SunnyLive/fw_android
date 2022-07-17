package com.fengwo.module_vedio.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.event.ToTopEvent;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.mvp.ui.activity.shortvideo.ShortVideoActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 影视模块短片列表
 *
 * @author Zachary
 * @date 2019/12/24
 */
public class VideoHomeShortFragment extends BaseListFragment<VideoHomeShortModel> {
    private boolean isVisiable;

    @Override
    public Flowable setNetObservable() {
        return new RetrofitUtils().createApi(VedioApiService.class).getShortVideoList(page + PAGE_SIZE, 0);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.video_item_home_short_video;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, VideoHomeShortModel item, int position) {
        ImageView imageView = helper.getView(R.id.pic_iv);
        ImageView ivHeader = helper.getView(R.id.ivHeader);
        ImageLoader.loadImg(imageView, item.cover);
        ImageLoader.loadImg(ivHeader, item.headImg);
        View likeView = helper.setText(R.id.tvAlbumName, "《" + item.movieTitle + "》")
                .setText(R.id.tvUserName, item.userName)
                .setText(R.id.tvLikeNum, String.valueOf(item.likes))
                .setText(R.id.tv_comment_count, String.valueOf(item.comments))
                .getView(R.id.view_like);
        likeView.setSelected(item.isLike);
    }

    @Override
    public String setEmptyContent() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setTransparentBlackText(getContext(), smartRefreshLayout);
        RxBus.get().toObservable(ToTopEvent.class).subscribe(new Consumer<ToTopEvent>() {
            @Override
            public void accept(ToTopEvent toTopEvent) throws Exception {
                if (isVisiable)
                    recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void setData(List<VideoHomeShortModel> datas, int page) {
        super.setData(datas, page);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            // 点赞
            likeShortVideo(position, this.adapter.getData().get(position));
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ShortVideoActivity.startShortVideo(getActivity(), 0, this.adapter.getData().get(position));
        });
    }

    private void likeShortVideo(int listPosition, VideoHomeShortModel model) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("movieId", model.id);
        map.put("type", "0");
        RequestBody body = RequestBody.create(new Gson().toJson(map), MediaType.parse("application/json"));
        new RetrofitUtils().createApi(VedioApiService.class).likeVideo(body)
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (recyclerView == null) return;
                        if (data.isSuccess()) {
                            adapter.getData().get(listPosition).isLike = !model.isLike;
                            int likes = adapter.getData().get(listPosition).likes;
                            if (model.isLike) {
                                adapter.getData().get(listPosition).likes = likes - 1;
                            } else {
                                adapter.getData().get(listPosition).likes = likes + 1;
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (recyclerView == null) return;
                        toastTip(msg);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisiable)
            onRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisiable = isVisibleToUser;
    }
}