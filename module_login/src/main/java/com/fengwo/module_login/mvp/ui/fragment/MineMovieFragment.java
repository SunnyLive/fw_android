package com.fengwo.module_login.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListFragment;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MineMovieDto;
import com.fengwo.module_login.mvp.dto.ShortVideoModel;
import com.fengwo.module_login.utils.UserManager;

import io.reactivex.Flowable;

public class MineMovieFragment extends BaseListFragment<MineMovieDto> {
    private final static String UID = "uid";
    private final static String ZAN = "iszan";

    private View addView;

    public static MineMovieFragment newInstance(int uid, boolean iszan) {
        Bundle args = new Bundle();
        args.putInt(UID, uid);
        args.putBoolean(ZAN, iszan);
        MineMovieFragment fragment = new MineMovieFragment();
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
                    ArouteUtils.toBuildSubjectActivity();
                }
            });
        } else {
            addView.setVisibility(View.GONE);
        }

    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getMovieByUser(uid + "", p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_mine_movie;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, MineMovieDto item, int position) {
        ImageView imageView = helper.getView(R.id.iv);
        ImageLoader.loadImg(imageView, item.cover);
        helper.setText(R.id.tv_title, item.name);
        helper.setText(R.id.tv_num, item.num + "个作品");
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.num <= 0) {
                    MineMovieFragment.this.toastTip("该专辑暂无作品");
                    return;
                }
                ArouteUtils.toShortVideoActivity(item.id, uid);
//                ArouteUtils.toShortVideoActivity(item);
//                SmallVedioDetailActivity.startActivity(getActivity(), position, getData());
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
