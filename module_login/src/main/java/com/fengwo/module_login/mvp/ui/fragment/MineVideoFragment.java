package com.fengwo.module_login.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

import io.reactivex.Flowable;

public class MineVideoFragment extends BaseListFragment<VideoHomeShortModel> {
    private final static String UID = "uid";
    private final static String ZAN = "iszan";

    public static MineVideoFragment newInstance(int uid, boolean b) {

        Bundle args = new Bundle();
        args.putInt(UID, uid);
        args.putBoolean(ZAN, b);
        MineVideoFragment fragment = new MineVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private int uid;
    private boolean isZan;

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
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        if (isZan)
            return new RetrofitUtils().createApi(LoginApiService.class).getLikeVedioByUser(p, uid + "");
        return new RetrofitUtils().createApi(LoginApiService.class).getVedioByUser(p, uid + "", 1);//排序: 0-点赞量 1-浏览量，2-评论量，3-时间倒序，4-时间升序
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        if (position % 2 == 0) {
            params.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.dp_10);
            params.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.dp_5);
        } else {
            params.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.dp_5);
            params.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.dp_10);
        }
        helper.itemView.setLayoutParams(params);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArouteUtils.toSmallVedio(position, getData());
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
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        SmartRefreshLayoutUtils.setWhiteBlackText(getActivity(), smartRefreshLayout);
    }

}
