package com.fengwo.module_chat.mvp.ui.dialog;

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

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_chat.mvp.ui.adapter.CommentAdapter;
import com.fengwo.module_chat.mvp.ui.event.CommentRefreshEvent;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class CommentDialog extends BasePopupWindow {

    private CompositeDisposable compositeDisposable;

    private TextView tvCommentCount;

    private ChatService service;
    private int page = 1;
    private final String PAGE_SIZE = ",20";
    private CommentAdapter commentAdapter;
    private String totalCount;
    private String id;

    private OnCommentListener listener;

    public CommentDialog(Context context, String id) {
        super(context);
        this.id = id;
        initView();
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setPopupGravity(Gravity.BOTTOM);
        setShowAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pickerview_slide_in_bottom));
        setDismissAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pickerview_slide_out_bottom));
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_comment);
    }

    @SuppressLint("CheckResult")
    private void initView() {
        compositeDisposable = new CompositeDisposable();
        tvCommentCount = findViewById(R.id.tvCommentCount);
        findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
        RecyclerView recyclerView = findViewById(R.id.rvComment);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext());
        commentAdapter.setEnableLoadMore(true);
        commentAdapter.setOnLoadMoreListener(() -> {
            page += 1;
            getData(id);
        }, recyclerView);
        commentAdapter.disableLoadMoreIfNotFullPage();
        commentAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id = view1.getId();
            if (id == R.id.view_like) {
                likeComment(position, -1, commentAdapter.getData().get(position).commentId);
            } else if (id == R.id.viewExpandStatus) {
                CommentModel commentModel = commentAdapter.getData().get(position);
                if (commentModel.isExpand) {
                    if (commentModel.hasMore()) {// 展开并且有更多数据
                        // 加载二级列表
                        int page = new BigDecimal(commentModel.seconds.size()).divide(new BigDecimal(20), BigDecimal.ROUND_UP).intValue() + 1;
                        getSecondCommentList(commentModel.commentId, position, page + PAGE_SIZE);
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
                        getSecondCommentList(commentModel.commentId, position, 1 + PAGE_SIZE);
                    }
                }
            }
        });
        commentAdapter.setOnChildItemClickListener(new CommentAdapter.OnChildItemClickListener() {
            @Override
            public void commentLike(int parentPosition, int position, CommentModel commentModel) {
                likeComment(parentPosition, position, commentModel.commentId);
            }

            @Override
            public void commentClick(int parentPosition, int position, CommentModel commentModel) {
                if (listener != null)
                    listener.comment(parentPosition, position, 2, commentModel, id,
                            commentAdapter.getData().get(parentPosition).commentId);
            }
        });
        commentAdapter.setOnItemClickListener((adapter, view12, position) -> {// 回复一级评论
            if (listener != null) {
                listener.comment(position, -1, 2, commentAdapter.getData().get(position),
                        id, commentAdapter.getData().get(position).commentId);
            }
        });
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(null);
        findViewById(R.id.viewEditTouch).setOnClickListener(v -> {
            if (listener != null) {
                listener.comment(-1, -1, 1, null, id, 0);
            }
        });
        compositeDisposable.add(RxBus.get().toObservable(CommentRefreshEvent.class).subscribe(event -> {
            L.e("======");
            if (!TextUtils.equals(id, event.cardId)) return;
            int parentIndex = event.parentIndex;
            int position = event.position;
            CommentModel model = event.model;
            totalCount = String.valueOf(Integer.parseInt(totalCount) + 1);
            tvCommentCount.setText(String.format("%s条评论", totalCount));
            // 填充数据
            if (parentIndex < 0) {
                commentAdapter.addData(0, model);
                recyclerView.scrollToPosition(0);
            } else if (position < 0) {
                CommentModel commentModel = commentAdapter.getData().get(parentIndex);
                commentModel.replys += 1;
                commentModel.isExpand = true;
                commentModel.seconds.add(0, model);
                commentAdapter.getData().set(parentIndex, commentModel);
                commentAdapter.notifyDataSetChanged();
            } else {
                CommentModel commentModel = commentAdapter.getData().get(parentIndex);
                commentModel.replys += 1;
                commentModel.seconds.add(position + 1, model);
                commentAdapter.getData().set(parentIndex, commentModel);
                commentAdapter.notifyDataSetChanged();
            }
        }));
        page = 1;
        service = new RetrofitUtils().createApi(ChatService.class);
        getData(id);
    }

    private void getData(String id) {
        compositeDisposable.add(service.getCommentList(Integer.parseInt(id), page + PAGE_SIZE).compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<BaseListDto<CommentModel>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<CommentModel>> data) {
                        if (tvCommentCount == null) return;
                        if (data.isSuccess()) {
                            ArrayList<CommentModel> comments = data.data.records;
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
                }));
    }

    private void likeComment(int parentIndex, int index, int id) {
        compositeDisposable.add(service.likeComment(id).compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            if (index < 0) {// 一级列表点赞
                                CommentModel commentModel = commentAdapter.getData().get(parentIndex);
                                int isLike = commentModel.isLike;
                                commentModel.isLike = isLike == 1 ? 0 : 1;
                                if (isLike == 1) {
                                    commentModel.likes -= 1;
                                } else commentModel.likes += 1;
                                commentAdapter.setData(parentIndex, commentModel);
                            } else {// 二级列表点赞
                                CommentModel model = commentAdapter.getData().get(parentIndex);
                                int isLike = model.seconds.get(index).isLike;
                                model.seconds.get(index).isLike = isLike == 1 ? 0 : 1;
                                if (isLike == 1) {
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
                }));
    }

    private void getSecondCommentList(int id, int parentIndex, String pageParams) {
        compositeDisposable.add(service.getSecondCommentList(id, pageParams).compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<BaseListDto<CommentModel>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<CommentModel>> data) {
                        CommentModel model = commentAdapter.getData().get(parentIndex);
                        model.isExpand = true;
                        List<CommentModel> reply = model.seconds;
                        reply.addAll(data.data.records);
                        commentAdapter.setData(parentIndex, model);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                }));
    }

    public void setOnCommentListener(OnCommentListener l) {
        this.listener = l;
    }

    public interface OnCommentListener {
        void comment(int parentIndex, int position, int type, CommentModel commentModel, String cardId, int parentId);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (compositeDisposable ==null ) return;
        compositeDisposable.isDisposed();
        compositeDisposable.clear();
    }
}