package com.fengwo.module_chat.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.base.ChatHomeChildRefreshEvent;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.ui.activity.ChatCardActivityNew;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.WebViewActivity;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.widgets.header.HeaderScrollHelper;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.event.BannedEvent;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.widget.CustomerDialog;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class ChatHomeChildFragment extends BaseListFragment<CircleListBean>
        implements HeaderScrollHelper.ScrollableContainer {

    private String tab;

    public static ChatHomeChildFragment newInstance(String id, String tab) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("tab", tab);
        ChatHomeChildFragment fragment = new ChatHomeChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String id;

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    @Override
    protected int setContentView() {
        return R.layout.chat_fragment_child;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView(View v) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            tab = bundle.getString("tab");
        }
        super.initView(v);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            CircleListBean bean = (CircleListBean) adapter.getData().get(position);
            if (bean.isAd == 0) {
                ChatCardActivityNew.start(getContext(), new ArrayList<>(adapter.getData()), ((CircleListBean) adapter.getData().get(position)).id, id, 0, 0, 0,Integer.parseInt(tab));
            } else {
                BrowserActivity.start(getContext(), "", bean.adContentUrl);
            }
        });

        RxBus.get().toObservable(ChatHomeChildRefreshEvent.class).subscribe(refreshEvent -> {
            onRefresh();
        });
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        smartRefreshLayout.setEnableRefresh(false);
    }

    @Override
    public Flowable setNetObservable() {
        return new RetrofitUtils().createApi(ChatService.class).getCirclesList(id, page + PAGE_SIZE, tab);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.chat_item_home_stagger;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, CircleListBean item, int position) {
        ImageView ivPoster = helper.getView(R.id.ivChatHeader);
        Group group = helper.getView(R.id.groupChat);
        ImageView civHeader = helper.getView(R.id.civChat);
        TextView tvTitle = helper.getView(R.id.tvChat);
        tvTitle.setVisibility(TextUtils.isEmpty(item.excerpt) ? View.GONE : View.VISIBLE);
        group.setVisibility(TextUtils.isEmpty(item.nickname) ? View.GONE : View.VISIBLE);
        if (item.isAd == 0) {
            tvTitle.setText(item.excerpt);
            helper.setText(R.id.tvChatName, item.nickname).setText(R.id.tvChatNum, String.valueOf(item.likes));
            ImageLoader.loadImg(civHeader, item.headImg);
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(getActivity(), 206);
            ivPoster.setLayoutParams(lp);
            ImageLoader.loadImg(ivPoster, item.cover);
        } else {
            ImageLoader.loadImg(ivPoster, item.adImage);
        }
    }

    @Override
    public String setEmptyContent() {
        return null;
    }
}