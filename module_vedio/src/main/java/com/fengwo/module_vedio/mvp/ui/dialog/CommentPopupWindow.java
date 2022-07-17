package com.fengwo.module_vedio.mvp.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.eventbus.ShortVideoCommentRefreshEvent;
import com.fengwo.module_vedio.eventbus.SmallVedioCommentRefreshEvent;
import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;
import com.fengwo.module_vedio.mvp.ui.adapter.ShortVideoCommentAdapter;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

public class CommentPopupWindow extends BasePopupWindow {

    private TextView tvCommentCount;
    private VedioApiService service;
    private int page = 1;
    private final String PAGE_SIZE = ",20";
    private ShortVideoCommentAdapter commentAdapter;
    private String totalCount;
    private String videoId;
    private OnCommentListener listener;

    public CommentPopupWindow(Context context, String videoId) {
        super(context);
        this.videoId = videoId;
        initView();
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setPopupGravity(Gravity.BOTTOM);
        setShowAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pickerview_slide_in_bottom));
        setDismissAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pickerview_slide_out_bottom));
    }

    @SuppressLint("CheckResult")
    private void initView() {
        tvCommentCount = findViewById(R.id.tvCommentCount);
        findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
        RecyclerView recyclerView = findViewById(R.id.rvComment);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new ShortVideoCommentAdapter(getContext());
        commentAdapter.setEnableLoadMore(true);
        commentAdapter.setOnLoadMoreListener(() -> {
            page += 1;
            getData(videoId);
        }, recyclerView);
        commentAdapter.disableLoadMoreIfNotFullPage();
        commentAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id = view1.getId();
            if (id == R.id.view_like) {
                likeComment(position, -1, commentAdapter.getData().get(position).id);
            } else if (id == R.id.viewExpandStatus) {
                ShortVideoCommentDto commentModel = commentAdapter.getData().get(position);
                if (commentModel.isExpand) {
                    if (commentModel.hasMore()) {// 展开并且有更多数据
                        // 加载二级列表
                        int page = new BigDecimal(commentModel.seconds.size()).divide(new BigDecimal(20), BigDecimal.ROUND_UP).intValue() + 1;
                        getSecondCommentList(commentModel.id, position, page + PAGE_SIZE);
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
                        getSecondCommentList(commentModel.id, position, 1 + PAGE_SIZE);
                    }
                }
            }
        });
        commentAdapter.setOnChildItemClickListener(new ShortVideoCommentAdapter.OnChildItemClickListener() {
            @Override
            public void commentLike(int parentPosition, int position, ShortVideoCommentDto commentModel) {
                likeComment(parentPosition, position, commentModel.id);
            }

            @Override
            public void commentClick(int parentPosition, int position, ShortVideoCommentDto commentModel) {
                if (listener != null)
                    listener.comment(parentPosition, position, 2, commentModel, videoId,
                            commentAdapter.getData().get(parentPosition).id);
            }
        });
        commentAdapter.setOnItemClickListener((adapter, view12, position) -> {// 回复一级评论
            if (listener != null) {
                listener.comment(position, -1, 2, commentAdapter.getData().get(position),
                        videoId, commentAdapter.getData().get(position).id);
            }
        });
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(null);
        findViewById(R.id.viewEditTouch).setOnClickListener(v -> {
            if (listener != null) {
                listener.comment(-1, -1, 1, null, videoId, 0);
            }
        });
        RxBus.get().toObservable(ShortVideoCommentRefreshEvent.class).subscribe(event -> {
            if (!TextUtils.equals(videoId, event.movieId)) return;
            int parentIndex = event.parentIndex;
            int position = event.position;
            ShortVideoCommentDto model = event.model;
            totalCount = String.valueOf(Integer.parseInt(totalCount) + 1);
            SmallVedioCommentRefreshEvent smallVedioCommentRefreshEvent = new SmallVedioCommentRefreshEvent();
            smallVedioCommentRefreshEvent.totalComments = Integer.parseInt(totalCount);
            RxBus.get().post(smallVedioCommentRefreshEvent);
            tvCommentCount.setText(String.format("%s条评论", totalCount));
            page = 1;
            getData(videoId);
        });
        page = 1;
        service = new RetrofitUtils().createApi(VedioApiService.class);
        getData(videoId);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_comment);
    }

    private void likeComment(int parentIndex, int index, int commentId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);
        map.put("videoId", videoId);
        map.put("type", "1");
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        service.likeSmallVideo(requestBody).compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            if (index < 0) {// 一级列表点赞
                                ShortVideoCommentDto commentModel = commentAdapter.getData().get(parentIndex);
                                boolean isLike = commentModel.isLike;
                                commentModel.isLike = !isLike;
                                if (isLike) {
                                    commentModel.likes -= 1;
                                } else commentModel.likes += 1;
                                commentAdapter.setData(parentIndex, commentModel);
                            } else {// 二级列表点赞
                                ShortVideoCommentDto model = commentAdapter.getData().get(parentIndex);
                                boolean isLike = model.seconds.get(index).isLike;
                                model.seconds.get(index).isLike = !isLike;
                                if (isLike) {
                                    model.seconds.get(index).likes -= 1;
                                } else model.seconds.get(index).likes += 1;
                                commentAdapter.setData(parentIndex, model);
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }

    private void getData(String id) {
        service.getSmallVideoCommentList("1", id, page + PAGE_SIZE).compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<ShortVideoCommentDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<ShortVideoCommentDto>> data) {
                        if (tvCommentCount == null) return;
                        if (data.isSuccess()) {
                            ArrayList<ShortVideoCommentDto> comments = data.data.records;
                            totalCount = data.data.total;
                            tvCommentCount.setText(String.format("%s条评论", totalCount));
                            if (page == 1) {
                                commentAdapter.setNewData(comments);
                            } else if (comments == null || comments.size() < 20) {
                                if (comments != null) commentAdapter.addData(comments);
                                commentAdapter.loadMoreEnd();
                            } else {
                                commentAdapter.addData(comments);
                                commentAdapter.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }

    private void getSecondCommentList(int id, int parentIndex, String pageParams) {
        service.getSmallVideoSecondCommentList(String.valueOf(id), "2", videoId, pageParams).compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<ShortVideoCommentDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<ShortVideoCommentDto>> data) {
                        ShortVideoCommentDto model = commentAdapter.getData().get(parentIndex);
                        model.isExpand = true;
                        List<ShortVideoCommentDto> reply = model.seconds;
                        reply.addAll(data.data.records);
                        commentAdapter.setData(parentIndex, model);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }

    public void setOnCommentListener(OnCommentListener l) {
        this.listener = l;
    }

    public interface OnCommentListener {
        void comment(int parentIndex, int position, int type, ShortVideoCommentDto commentModel, String videoId, int parentId);
    }
}
