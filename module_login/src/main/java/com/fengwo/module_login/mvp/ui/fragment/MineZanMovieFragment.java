package com.fengwo.module_login.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MineMovieDto;
import com.fengwo.module_login.utils.UserManager;

import io.reactivex.Flowable;

public class MineZanMovieFragment extends BaseListFragment<VideoHomeShortModel> {
    private final static String UID = "uid";
    private final static String ZAN = "iszan";

    private View addView;

    public static MineZanMovieFragment newInstance(int uid, boolean iszan) {
        Bundle args = new Bundle();
        args.putInt(UID, uid);
        args.putBoolean(ZAN, iszan);
        MineZanMovieFragment fragment = new MineZanMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private int uid;
    private boolean isZan;

    @Override
    public boolean hasEmptyView() {
        return isZan;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        uid = getArguments().getInt(UID, -1);
        isZan = getArguments().getBoolean(ZAN, false);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        addView = v.findViewById(R.id.view_add);
        if (uid == UserManager.getInstance().getUser().id && !isZan) {
            addView.setVisibility(View.VISIBLE);
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            addView.setVisibility(View.GONE);
        }

    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getLikeMovieByUser(uid + "", p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
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
        View likeView = helper.setText(R.id.tvAlbumName, item.movieTitle)
                .setText(R.id.tvUserName, item.userName)
                .setText(R.id.tvLikeNum, String.valueOf(item.likes))
                .setText(R.id.tv_comment_count, String.valueOf(item.comments))
                .getView(R.id.view_like);
        likeView.setSelected(false);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArouteUtils.toShortVideoActivity(item);
            }
        });
    }

    @Override
    public String setEmptyContent() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine_movie;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        SmartRefreshLayoutUtils.setWhiteBlackText(getActivity(), smartRefreshLayout);
    }

    @Override
    protected void initRv() {
        super.initRv();
    }
}
