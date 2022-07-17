package com.fengwo.module_login.mvp.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;

import io.reactivex.Flowable;

public class MineZanCardFragment extends BaseListFragment<CircleListBean> {
    private final static String UID = "uid";
    private final static String ZAN = "iszan";

    public static MineZanCardFragment newInstance(int uid, boolean isZan) {

        Bundle args = new Bundle();
        args.putInt(UID, uid);
        args.putBoolean(ZAN, isZan);
        MineZanCardFragment fragment = new MineZanCardFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private int uid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        uid = getArguments().getInt(UID, -1);
        super.onCreate(savedInstanceState);

    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getLikeCardByUser(p, uid + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
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
        Group group = helper.getView(R.id.groupChat);
        ImageView civHeader = helper.getView(R.id.civChat);
        ImageView ivPoster = helper.getView(R.id.ivChatHeader);
        TextView tvTitle = helper.getView(R.id.tvChat);
        View location = helper.getView(R.id.view_location);
        TextView tvLocation = helper.getView(R.id.tvLocation);

        if (item.isAd == 1) {
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(getActivity(), 129);
            ImageLoader.loadImg(ivPoster, item.adImage);
            ivPoster.setLayoutParams(lp);
        } else {
            ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
            lp.height = DensityUtils.dp2px(getActivity(), 206);
            ivPoster.setLayoutParams(lp);
            ImageLoader.loadImg(ivPoster, item.cover);
        }
        location.setVisibility(TextUtils.isEmpty(item.position) ? View.INVISIBLE : View.VISIBLE);
        tvLocation.setText(item.position);
        tvTitle.setText(item.excerpt);
        tvTitle.setVisibility(TextUtils.isEmpty(item.excerpt) ? View.GONE : View.VISIBLE);
        group.setVisibility(TextUtils.isEmpty(item.nickname) ? View.GONE : View.VISIBLE);
        helper.setText(R.id.tvChatName, item.nickname).setText(R.id.tvChatNum, String.valueOf(item.likes));
        ImageLoader.loadImg(civHeader, item.headImg);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArouteUtils.toCardHome(getData(), item.id + "", "0", 1, uid, 1,0);
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
        SmartRefreshLayoutUtils.setWhiteBlackText(getActivity(), smartRefreshLayout);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
