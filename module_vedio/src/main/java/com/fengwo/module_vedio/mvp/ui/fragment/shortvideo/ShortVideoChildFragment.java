package com.fengwo.module_vedio.mvp.ui.fragment.shortvideo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.eventbus.PlayShortVideoEvent;
import com.fengwo.module_vedio.mvp.presenter.ShortVideoPresenter;
import com.fengwo.module_vedio.mvp.ui.activity.shortvideo.ShortVideoActivity;
import com.fengwo.module_vedio.mvp.ui.adapter.ShortVideoAdapter;
import com.fengwo.module_vedio.mvp.ui.adapter.ShortVideoAlbumAdapter;
import com.fengwo.module_vedio.mvp.ui.iview.IShortVedioView;
import com.fengwo.module_vedio.widget.GridItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/25
 */
public class ShortVideoChildFragment extends BaseMvpFragment<IShortVedioView, ShortVideoPresenter> implements IShortVedioView {

    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;


    private View mHeadView;
    private ShortVideoAdapter shortVideoAdapter;
    //    private List<VideoHomeShortModel> mListData = new ArrayList<>();
    private int page;
    private String pageParams = ",10";
    private String movieTitle;
    private VideoHomeShortModel videoHomeShortModel;
    //    private int albumId; //专辑id
    HeaderViewHolder headerViewHolder;

    private int shortVideoId;//短片id

    public static Fragment getInstance(int albumId, VideoHomeShortModel videoHomeShortModel) {
        Fragment fragment = new ShortVideoChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("albumId", albumId);
        if (videoHomeShortModel != null) {
            bundle.putSerializable("model", videoHomeShortModel);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected ShortVideoPresenter initPresenter() {
        return new ShortVideoPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_short_video_child;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setTransparentBlackText(getActivity(), smartrefreshlayout);
//        albumId = getArguments().getInt("albumId", 0);
        videoHomeShortModel = (VideoHomeShortModel) getArguments().getSerializable("model");

        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            onRefresh();
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            smartrefreshlayout.closeHeaderOrFooter();
            page = this.page + 1;
            getShortVideoList();
        });
        initHeadView();
        initBottomRv();
        setData(videoHomeShortModel);
    }

    private void setData(VideoHomeShortModel videoHomeShortModel) {
        if (videoHomeShortModel.albumId >= 0) { //有剧集
            setAlbumListVisibility(true);
            p.getAlbumList("1,100", videoHomeShortModel.albumId, videoHomeShortModel.userId);
        } else {
            setAlbumListVisibility(false);
            setHeadViewData(videoHomeShortModel);
        }
    }

    private void initBottomRv() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recycleview.setLayoutManager(layoutManager);
        recycleview.addItemDecoration(new GridItemDecoration(getActivity(), 12));
        shortVideoAdapter = new ShortVideoAdapter(new ArrayList<>());
        recycleview.setAdapter(shortVideoAdapter);
        shortVideoAdapter.addHeaderView(mHeadView);
        shortVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                videoHomeShortModel = shortVideoAdapter.getData().get(position);
                setData(videoHomeShortModel);
            }
        });
    }

    private void onRefresh() {
        smartrefreshlayout.closeHeaderOrFooter();
        page = 1;
        getShortVideoList();
    }

    private void setAlbumListVisibility(boolean visibility) {
        headerViewHolder.llMore.setVisibility(visibility ? View.VISIBLE : View.GONE);
        headerViewHolder.rvAlbum.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.top_short_video_child, null);
        headerViewHolder = new HeaderViewHolder(mHeadView);
        headerViewHolder.rvAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        headerViewHolder.tvHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLike();
            }
        });
        headerViewHolder.tvDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShortVideoActivity) getActivity()).showDes();
            }
        });
    }

    private void addLike() {
        Map map = new HashMap();
        map.put("movieId", shortVideoId);
        map.put("type", 0);
        p.addLike(map);
    }

    private void setHeadViewData(VideoHomeShortModel viewData) {
        shortVideoId = viewData.id;
        movieTitle = viewData.movieTitle;
        headerViewHolder.tvTitle.setText(viewData.movieTitle);
        headerViewHolder.tvPlayNum.setText(viewData.views + "次播放");
        headerViewHolder.tvGift.setText(viewData.gifts + "");
        headerViewHolder.tvHeart.setText(viewData.likes + "");
        headerViewHolder.tvShare.setText(viewData.shares + "");
        if (TextUtils.isEmpty(viewData.albumName)) {
            headerViewHolder.tvTitleProject.setVisibility(View.GONE);
        } else {
            headerViewHolder.tvTitleProject.setVisibility(View.VISIBLE);
            headerViewHolder.tvTitleProject.setText("《" + viewData.albumName + "》专题");
        }

        if (viewData.isLike) {
            Drawable top = getResources().getDrawable(R.drawable.ic_heart_purple);
            headerViewHolder.tvHeart.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.drawable.ic_heart_gray);
            headerViewHolder.tvHeart.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        }
        onRefresh();
        RxBus.get().post(new PlayShortVideoEvent(viewData.url, viewData.id, viewData.movieTitle, viewData.intro, viewData.comments, viewData.userId));
        p.addPlayNum(viewData.id);
    }

    private void getShortVideoList() {
        p.getShortVideoList(page + pageParams, movieTitle);
    }

    @Override
    public void setVideoList(BaseListDto<VideoHomeShortModel> baseListDt) {
//        mListData = baseListDt.records;
        if (page == 1) {
            shortVideoAdapter.setNewData(baseListDt.records);
        } else {
            shortVideoAdapter.addData(baseListDt.records);
        }
    }

    @Override
    public void setAlbumList(BaseListDto<VideoHomeShortModel> baseListDt) {
        ShortVideoAlbumAdapter albumAdapter = new ShortVideoAlbumAdapter(baseListDt.records);
        headerViewHolder.rvAlbum.setAdapter(albumAdapter);
        albumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                resetSelectData(albumAdapter, position);
            }
        });
        for (int i = 0; i < albumAdapter.getData().size(); i++) {
            Log.e("lgl", "for id = " + albumAdapter.getData().get(i).id);
            if (albumAdapter.getData().get(i).id == videoHomeShortModel.id) {
                resetSelectData(albumAdapter, i);
                return;
            }
        }
        resetSelectData(albumAdapter, 0);
    }

    private void resetSelectData(ShortVideoAlbumAdapter albumsAdapter, int position) {
        albumsAdapter.resetIsPlay(position);
        videoHomeShortModel = albumsAdapter.getData().get(position);
        setHeadViewData(videoHomeShortModel);
    }

    @Override
    public void setAddLike(HttpResult httpResult) {
        if (httpResult.isSuccess()) {
            Drawable top;
            if (!videoHomeShortModel.isLike) {
                top = getResources().getDrawable(R.drawable.ic_heart_purple);
                headerViewHolder.tvHeart.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                videoHomeShortModel.likes = videoHomeShortModel.likes + 1;
                videoHomeShortModel.isLike = true;
//            setHeadViewData(videoHomeShortModel);
                headerViewHolder.tvHeart.setText(videoHomeShortModel.likes + "");
            } else {
                top = getResources().getDrawable(R.drawable.ic_heart_gray);
                headerViewHolder.tvHeart.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                videoHomeShortModel.likes = videoHomeShortModel.likes - 1;
                videoHomeShortModel.isLike = false;
//            setHeadViewData(videoHomeShortModel);
                headerViewHolder.tvHeart.setText(videoHomeShortModel.likes + "");
            }
        }
    }

    @Override
    public void addPlayNum(HttpResult httpResult) {

    }


    class HeaderViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_des)
        TextView tvDes;
        @BindView(R2.id.tv_play_num)
        TextView tvPlayNum;
        @BindView(R2.id.ll_title)
        LinearLayout llTitle;
        @BindView(R2.id.tv_gift)
        TextView tvGift;
        @BindView(R2.id.tv_heart)
        TextView tvHeart;
        @BindView(R2.id.tv_share)
        TextView tvShare;
        @BindView(R2.id.tv_danmu)
        TextView tvDanmu;
        @BindView(R2.id.ll_gift)
        LinearLayout llGift;
        @BindView(R2.id.tv_title_project)
        TextView tvTitleProject;
        @BindView(R2.id.tv_more)
        TextView tvMore;
        @BindView(R2.id.ll_more)
        LinearLayout llMore;
        @BindView(R2.id.rv_album)
        RecyclerView rvAlbum;

        public HeaderViewHolder(View headerRootView) {
            ButterKnife.bind(this, headerRootView);
        }
    }

    @Override
    protected boolean getImmersionBar() {
        return false;
    }

    @Override
    public boolean immersionBarEnabled() {
        return false;
    }
}
