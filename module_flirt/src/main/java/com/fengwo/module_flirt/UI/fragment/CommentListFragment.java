package com.fengwo.module_flirt.UI.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.InteractBean;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;


import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.RoundImageView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.dialog.SelectCommonDialogWindow;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

public class CommentListFragment extends BaseListFragment<InteractBean> {


    private CompositeDisposable compositeDisposable;

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(ChatService.class).getCardCommentList(p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_comment_list;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, InteractBean item, int position) {
        RoundImageView avator = helper.getView(R.id.civ_head);
        ImageLoader.loadRouteImg(avator, item.headImg, 8);

        //cover图
        RoundImageView coverImg = helper.getView(R.id.cover_img);
        ImageLoader.loadRouteImg(coverImg, item.cover, 8);
        //内容
        helper.setText(R.id.tv_msg, item.msg);
        //时间
        helper.setText(R.id.tv_create_time, item.cardLikeTime);
        //内容
        helper.setText(R.id.tv_content, item.content);
        helper.setText(R.id.tv_name, item.nickname);

        //年龄
        TextView tvAge = helper.getView(R.id.tv_age);
        tvAge.setVisibility(View.VISIBLE);
        tvAge.setText(TextUtils.isEmpty(item.age) ? "" : item.age);
        if (TextUtils.isEmpty(item.age))
            tvAge.setCompoundDrawablePadding(0);
        else
            tvAge.setCompoundDrawablePadding(4);

        //性别icon
        if (item.sex == 2) {
            tvAge.setBackgroundResource(R.drawable.shape_corner_girl);
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_girl, 0, 0, 0);
        } else if (item.sex == 1) {
            tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_boy, 0, 0, 0);
        } else {
            tvAge.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            if (!TextUtils.isEmpty(item.age)) {
                tvAge.setBackgroundResource(R.drawable.shape_corner_boy);
            } else
                //无性别 无年龄
                tvAge.setVisibility(View.GONE);
        }

        helper.getView(R.id.civ_head_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MineDetailActivity.startActivityWithUserId(getActivity(), adapter.getData().get(position).userId);
            }
        });

        ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);
    }

    @Override
    public String setEmptyContent() {
        return "还没有收到任何评论呢\n发布状态让更多的人发现你吧";
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle_white;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        compositeDisposable = new CompositeDisposable();
        SmartRefreshLayoutUtils.setTransparentBlackText(getContext(), smartRefreshLayout);
        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            SelectCommonDialogWindow dialog = new SelectCommonDialogWindow(getActivity(), true, "", "删除此条评论", "");
            dialog.addOnClickListener(new SelectCommonDialogWindow.OnSureClickListener() {
                @Override
                public void onFirstclick() {
                    InteractBean item = (InteractBean) adapter.getItem(position);
                    assert item != null;
                    compositeDisposable.add(new RetrofitUtils().createApi(ChatService.class)
                            .deleteCardComment(item.getId() + "")
                            .compose(RxUtils.applySchedulers2())
                            .subscribeWith(new LoadingObserver<HttpResult>() {
                                @Override
                                public void _onNext(HttpResult data) {
                                    if (data.isSuccess()) {
                                        adapter.remove(position);
                                        adapter.notifyItemChanged(position);
                                        dialog.dismiss();
                                    } else {
                                        ToastUtils.showShort(getContext(), data.description);
                                    }
                                }

                                @Override
                                public void _onError(String msg) {
                                    ToastUtils.showShort(getContext(), msg);
                                }
                            }));
                }

                @Override
                public void onSecondclick() {

                }
            });
            dialog.showPopupWindow();
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable == null) return;
        compositeDisposable.isDisposed();
        compositeDisposable.clear();
    }

    @Override
    public void setData(List<InteractBean> datas, int page) {
        super.setData(datas, page);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            onRefresh();
    }
}
