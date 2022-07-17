package com.fengwo.module_vedio.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.event.ToTopEvent;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.eventbus.ScrollYEnvent;
import com.fengwo.module_vedio.mvp.dto.ShortVideoModel;
import com.fengwo.module_vedio.mvp.ui.activity.SmallVedioDetailActivity;
import com.fengwo.module_vedio.widget.GridItemDecoration;
import com.tencent.liteav.demo.play.utils.DensityUtil;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public class VideoHomeRecommendFragment extends BaseListFragment<VideoHomeShortModel> {
    CompositeDisposable compositeDisposable;

    public static VideoHomeRecommendFragment newInstance(int menuId) {
        Bundle args = new Bundle();
        args.putInt("menuId", menuId);
        VideoHomeRecommendFragment fragment = new VideoHomeRecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    int menuId;

    @Override
    public Flowable setNetObservable() {
        menuId = getArguments().getInt("menuId");
        if (menuId == 0) {
            return new RetrofitUtils().createApi(VedioApiService.class).getVideoHomeList(0, page + PAGE_SIZE);
        } else {
            return new RetrofitUtils().createApi(VedioApiService.class).getShortVideoList(menuId, 0, page + PAGE_SIZE);
        }
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_video_home_recommend;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, VideoHomeShortModel item, int position) {
        ImageView imageView = helper.getView(R.id.iv);
        ImageLoader.loadImg(imageView, item.cover);
        helper.setText(R.id.tv_num, item.likes + "");
        if (item.isLike) {
            helper.setImageResource(R.id.iv_like, R.drawable.vedio_icon_smalldetail_like);
        } else {
            helper.setImageResource(R.id.iv_like, R.drawable.vedio_icon_smalldetail_unlike);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmallVedioDetailActivity.startActivity(getActivity(), position, getData(), menuId, "", page);
            }
        });
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
        compositeDisposable = new CompositeDisposable();
        RxBus.get().toObservable(ToTopEvent.class).subscribe(new Consumer<ToTopEvent>() {
            @Override
            public void accept(ToTopEvent toTopEvent) throws Exception {
                if (isVisiable)
                    recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    @Override
    protected void initRv() {
        super.initRv();
        recyclerView.addItemDecoration(new GridItemDecoration(getContext(), DensityUtil.dip2px(getContext(), 12)));
    }

    private boolean isVisiable;

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