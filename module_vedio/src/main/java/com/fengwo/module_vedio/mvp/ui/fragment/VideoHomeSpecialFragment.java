package com.fengwo.module_vedio.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.event.ToTopEvent;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.mvp.dto.VideoAlbumListDto;
import com.fengwo.module_vedio.mvp.ui.activity.shortvideo.ShortVideoActivity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 影视模块专辑列表
 *
 * @author Zachary
 * @date 2019/12/24
 */
public class VideoHomeSpecialFragment extends BaseListFragment<VideoAlbumListDto> {
    private boolean isVisiable;

    @Override
    public Flowable setNetObservable() {
        return new RetrofitUtils().createApi(VedioApiService.class).getAlbumList(page + PAGE_SIZE);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.video_item_home_album;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, VideoAlbumListDto item, int position) {
        ImageView imageView = helper.getView(R.id.pic_iv);
        ImageLoader.loadImg(imageView, item.cover);
        helper.setText(R.id.tvAlbumName, "《" + item.name + "》")
                .setText(R.id.tvAlbumSize, String.format("共%d集", item.num));
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
    public void setData(List<VideoAlbumListDto> datas, int page) {
        super.setData(datas, page);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ShortVideoActivity.startShortVideo(getActivity(), this.adapter.getData().get(position).id, null);
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisiable = isVisibleToUser;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisiable)
            onRefresh();
    }
}