package com.fengwo.module_vedio.mvp.ui.fragment.shortvideo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.eventbus.ShortVideoCommentRefreshEvent;
import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;
import com.fengwo.module_vedio.mvp.presenter.ShortVideoCommentPresenter;
import com.fengwo.module_vedio.mvp.ui.adapter.ShortVideoCommentAdapter;
import com.fengwo.module_vedio.mvp.ui.iview.IShortVideoCommentView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

;
;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/12/27
 */
public class ShortVideoCommentsFragment extends BaseMvpFragment<IShortVideoCommentView, ShortVideoCommentPresenter>
        implements IShortVideoCommentView {

    @BindView(R2.id.rvComment)
    RecyclerView recyclerView;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.viewEditTouch)
    LinearLayout viewEditTouch;


    private CompositeDisposable compositeDisposable;

    private final String PAGE_SIZE = ",20";
    private int page = 1;

    private int movieId = -1;
    private ShortVideoCommentAdapter commentAdapter;

    public static Fragment getInstance() {
        Fragment fragment = new ShortVideoCommentsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected ShortVideoCommentPresenter initPresenter() {
        return new ShortVideoCommentPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_short_video_comments;
    }

    public void setMovieId(int movieId){
        this.movieId = movieId;
        L.e("okh","setMovieId"+this.movieId);
        if (recyclerView != null) getData();
    }

    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        SmartRefreshLayoutUtils.setTransparentBlackText(getActivity(), refreshLayout);
//        movieId = getArguments().getInt("movieId");
        compositeDisposable = new CompositeDisposable();
//        compositeDisposable.add(RxBus.get().toObservable(PlayShortVideoEvent.class)
//                .compose(bindToLifecycle())
//                .subscribe(event -> {
//                    this.movieId = event.movieId;
//                    L.e("okh"," rxbus receive==movieId ="+this.movieId);
//                    if (recyclerView != null) getData();
//                }));
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getData();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page += 1;
            getData();
        });
        commentAdapter = new ShortVideoCommentAdapter(getContext());
        commentAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id = view1.getId();
            if (id == R.id.view_like) {
                likeComment(position, -1, String.valueOf(commentAdapter.getData().get(position).id), String.valueOf(movieId));
            } else if (id == R.id.viewExpandStatus) {
                ShortVideoCommentDto commentModel = commentAdapter.getData().get(position);
                if (commentModel.isExpand) {
                    if (commentModel.hasMore()) {// 展开并且有更多数据
                        // 加载二级列表
                        int page = new BigDecimal(commentModel.seconds.size()).divide(new BigDecimal(20), BigDecimal.ROUND_UP).intValue() + 1;
                        p.getSecondCommentList(String.valueOf(movieId), page + PAGE_SIZE, commentModel.id, position);
                    } else {//展开但是没有更多数据
                        // 收起
                        commentModel.isExpand = false;
                        commentAdapter.setData(position, commentModel);
                    }
                } else {
                    if (commentModel.seconds.size() > 0) {// 没展开但是有数据
                        // 展开并显示数据
                        commentModel.isExpand = true;
                        commentAdapter.setData(position, commentModel);
                    } else {// 没展开也没有数据
                        // 重新加载数据
                        p.getSecondCommentList(String.valueOf(movieId), 1 + PAGE_SIZE, commentModel.id, position);
                    }
                }
            }
        });
        commentAdapter.setOnChildItemClickListener(new ShortVideoCommentAdapter.OnChildItemClickListener() {
            @Override
            public void commentLike(int parentPosition, int position, ShortVideoCommentDto commentModel) {
                likeComment(parentPosition, position, String.valueOf(commentModel.id), String.valueOf(movieId));
            }

            @Override
            public void commentClick(int parentPosition, int position, ShortVideoCommentDto commentModel) {
                comment(parentPosition, position, 2, commentModel, String.valueOf(movieId),
                        commentAdapter.getData().get(parentPosition).id);
            }
        });
        commentAdapter.setOnItemClickListener((adapter, view12, position) -> {// 回复一级评论
            comment(position, -1, 2, commentAdapter.getData().get(position),
                    String.valueOf(movieId), commentAdapter.getData().get(position).id);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(commentAdapter);

        compositeDisposable.add(RxBus.get().toObservable(ShortVideoCommentRefreshEvent.class).subscribe(event -> {
            if (!TextUtils.equals(String.valueOf(movieId), event.movieId)) return;
            int parentIndex = event.parentIndex;
            int position = event.position;
            ShortVideoCommentDto model = event.model;
            // 填充数据
            if (parentIndex < 0) {
                commentAdapter.addData(0, model);
                recyclerView.scrollToPosition(0);
            } else if (position < 0) {
                ShortVideoCommentDto commentModel = commentAdapter.getData().get(parentIndex);
                commentModel.replys += 1;
                commentModel.isExpand = true;
                commentModel.seconds.add(0, model);
                commentAdapter.getData().set(parentIndex, commentModel);
                commentAdapter.notifyDataSetChanged();
            } else {
                ShortVideoCommentDto commentModel = commentAdapter.getData().get(parentIndex);
                commentModel.replys += 1;
                commentModel.seconds.add(position + 1, model);
                commentAdapter.getData().set(parentIndex, commentModel);
                commentAdapter.notifyDataSetChanged();
            }
        }));
        if (movieId > 0) getData();
    }

    @Override
    protected boolean getImmersionBar() {
        return false;
    }

    @Override
    public boolean immersionBarEnabled() {
        return false;
    }

    @Override
    public void setComment(ArrayList<ShortVideoCommentDto> records) {
        if (commentAdapter == null)return;
        if (page == 1) {
            refreshLayout.finishRefresh();
            commentAdapter.setNewData(records);
        } else if (records == null || records.size() < 20) {
            if (records != null) commentAdapter.addData(records);
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            commentAdapter.addData(records);
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void setSecondComment(int parentIndex, ArrayList<ShortVideoCommentDto> records) {
        ShortVideoCommentDto model = commentAdapter.getData().get(parentIndex);
        model.isExpand = true;
        List<ShortVideoCommentDto> reply = model.seconds;
        reply.addAll(records);
        commentAdapter.setData(parentIndex, model);
    }

    @Override
    public void likeComment(int parentIndex, int secondPosition) {
        if (secondPosition < 0) {// 一级列表点赞
            ShortVideoCommentDto commentModel = commentAdapter.getData().get(parentIndex);
            boolean isLike = commentModel.isLike;
            commentModel.isLike = !isLike;
            if (isLike) {
                commentModel.likes -= 1;
            } else commentModel.likes += 1;
            commentAdapter.setData(parentIndex, commentModel);
        } else {// 二级列表点赞
            ShortVideoCommentDto model = commentAdapter.getData().get(parentIndex);
            boolean isLike = model.seconds.get(secondPosition).isLike;
            model.seconds.get(secondPosition).isLike = !isLike;
            if (isLike) {
                model.seconds.get(secondPosition).likes -= 1;
            } else model.seconds.get(secondPosition).likes += 1;
            commentAdapter.setData(parentIndex, model);
        }
    }

    private void getData() {
        p.getCommentList(String.valueOf(movieId), page + PAGE_SIZE);
    }

    private void likeComment(int parentIndex, int secondPosition, String commentId, String movieId) {
        p.likeComment(parentIndex, secondPosition, commentId, movieId);
    }

    private void comment(int parentIndex, int position, int type, ShortVideoCommentDto commentModel, String movieId, int parentId) {
        KLog.e("lgl"," comment ===movieId ="+movieId);
        ShortVideoCommentInputDialog commentEditDialog = new ShortVideoCommentInputDialog();
        commentEditDialog.setData(parentIndex, position, type, commentModel, movieId, parentId);
        commentEditDialog.show(getChildFragmentManager(), "commentEdit");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable.size() > 0 && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    @OnClick({R2.id.viewEditTouch})
    void onClick(View v){
        int id = v.getId();
        if (id == R.id.viewEditTouch){
            comment(-1, -1, 1, null, String.valueOf(movieId), 0);
        }
    }
}