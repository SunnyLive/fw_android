package com.fengwo.module_vedio.mvp.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;
import com.fengwo.module_vedio.mvp.presenter.SearchPresenter;
import com.fengwo.module_vedio.mvp.ui.activity.shortvideo.ShortVideoActivity;
import com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchAllAdapter;
import com.fengwo.module_vedio.mvp.ui.iview.ISearchResultView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchResultAdapter.SEARCH_CONTENT;
import static com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchResultAdapter.TYPE_MOVIE;
import static com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchResultAdapter.TYPE_SHOW;
import static com.fengwo.module_vedio.mvp.ui.adapter.VideoSearchResultAdapter.TYPE_VIDEO;

public class ShowSearchAllActivity extends BaseMvpActivity<ISearchResultView, SearchPresenter> implements ISearchResultView {

    @BindView(R2.id.sfl_search_all)
    SmartRefreshLayout sflSearchAll;

    @BindView(R2.id.rv_search_all)
    RecyclerView rvSearchAll;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    private VideoSearchAllAdapter mAdapter;
    private String type;
    private String searchContent;

    @Override
    public SearchPresenter initPresenter() {
        return new SearchPresenter();
    }

    private int page = 1;

    @Override
    protected void initView() {
        type = getIntent().getStringExtra(TYPE_SHOW);
        tvTitle.setText(type.equals(TYPE_VIDEO) ? "全部视频" : "全部短片");
        searchContent = getIntent().getStringExtra(SEARCH_CONTENT);
        p.search(searchContent, page);
        sflSearchAll.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                p.search(searchContent, page);
            }
        });
        sflSearchAll.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                p.search(searchContent, ++page);
            }
        });
        initRv(searchContent);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(searchContent)) {
            page = 1;
            p.search(searchContent, page);
        }
    }

    private void initRv(String searchContent) {
        rvSearchAll.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new VideoSearchAllAdapter(type);
        rvSearchAll.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArrayList<VideoHomeShortModel> datas = (ArrayList<VideoHomeShortModel>) adapter.getData();
                if (type.equals(TYPE_VIDEO)) {
                    SmallVedioDetailActivity.startActivity(ShowSearchAllActivity.this, position, new ArrayList<>(datas), -1, searchContent,page);
                } else {
                    ShortVideoActivity.startShortVideo(ShowSearchAllActivity.this, 0, datas.get(position));
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_show_search_all;
    }

    @OnClick(R2.id.iv_back)
    public void onViewClick(View view) {
        if (view.getId() == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void showGuessData(List<String> data) {
        // don't implement
    }

    @Override
    public void showSearchResult(VideoSearchDto data, String content) {
        List<VideoHomeShortModel> records = type.equals(TYPE_MOVIE) ? data.getMovieInfoArr().getRecords() : data.getVideoInfoArr().getRecords();
        if (page > 1) {
            mAdapter.addData(records);
            sflSearchAll.finishLoadMore();
        } else {
            mAdapter.setNewData(records);
            sflSearchAll.finishRefresh();
        }
    }
}