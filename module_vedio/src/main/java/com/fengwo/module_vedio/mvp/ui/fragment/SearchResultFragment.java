package com.fengwo.module_vedio.mvp.ui.fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseFragment;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.eventbus.SearchEvent;
import com.fengwo.module_vedio.mvp.dto.SearchResultDto;
import com.fengwo.module_vedio.mvp.dto.ShortVedioInfo;
import com.fengwo.module_vedio.mvp.dto.SmallVedioInfo;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.api.RefreshFooter;;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import butterknife.BindView;

public class SearchResultFragment extends BaseMvpFragment implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R2.id.rv)
    RecyclerView rv;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;

    public static final int TYPE_SHORT = 1;
    public static final int TYPE_SMALL = 2;
    private static final String KEY = "result";
    private static final String TYPE = "type";
    private SearchResultDto searchResult;
    private int type;

    private BaseQuickAdapter shortAdapter;
    private BaseQuickAdapter smallAdapter;

    public static SearchResultFragment newInstance(SearchResultDto searchResult, int type) {
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        Bundle b = new Bundle();
        b.putSerializable(KEY, searchResult);
        b.putInt(TYPE, type);
        searchResultFragment.setArguments(b);
        return searchResultFragment;
    }


    @Override
    protected int setContentView() {
        return R.layout.vedio_fragment_searchresult;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        searchResult = (SearchResultDto) getArguments().getSerializable(KEY);
        type = getArguments().getInt(TYPE);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DefaultRefreshHeaderCreator creator = (context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.text_99);//全局设置主题颜色
            return new ClassicsHeader(context);
        };
        smartRefreshLayout.setRefreshHeader(creator.createRefreshHeader(getActivity(), smartRefreshLayout));
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadMoreListener(this);
        if (TYPE_SHORT == type) {
            if (null == shortAdapter) {
                if (searchResult.movieInfoDTOList.size() < 10) {
                    smartRefreshLayout.setEnableLoadMore(false);
                }
                shortAdapter = new BaseQuickAdapter(R.layout.vedio_item_search_result, searchResult.movieInfoDTOList) {
                    @Override
                    protected void convert(@NonNull BaseViewHolder helper, Object item) {
                        if (item instanceof ShortVedioInfo) {
                            ShortVedioInfo s = (ShortVedioInfo) item;
                            helper.setText(R.id.tv_title, s.movieTitle);
                            ImageLoader.loadImg(helper.getView(R.id.iv_cover),s.cover);
                            helper.setText(R.id.tv_duration, TimeUtils.getMovieDuration(s.duration));
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                    }
                };
            }
            rv.setAdapter(shortAdapter);
        } else if (TYPE_SMALL == type) {
            if (null == smallAdapter) {
                if (searchResult.shortVideoInfoDTOList.size() < 10) {
                    smartRefreshLayout.setEnableLoadMore(false);
                }
                smallAdapter = new BaseQuickAdapter(R.layout.vedio_item_search_result, searchResult.shortVideoInfoDTOList) {
                    @Override
                    protected void convert(@NonNull BaseViewHolder helper, Object item) {
                        if (item instanceof SmallVedioInfo) {
                            SmallVedioInfo s = (SmallVedioInfo) item;
                            helper.setText(R.id.tv_title, s.videoTitle);
                            ImageLoader.loadImg(helper.getView(R.id.iv_cover),s.cover);
                            helper.setText(R.id.tv_duration, TimeUtils.getMovieDuration(s.duration));
                        }

                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                    }
                };
            }
            rv.setAdapter(smallAdapter);
        }

    }

    public void reSetData(SearchResultDto searchResult) {
        smartRefreshLayout.finishRefresh();
        if (type == TYPE_SHORT) {
            if (searchResult.movieInfoDTOList.size() < 10) {
                smartRefreshLayout.setEnableLoadMore(false);
            }
            shortAdapter.setNewData(searchResult.movieInfoDTOList);
        } else {
            if (searchResult.shortVideoInfoDTOList.size() < 10) {
                smartRefreshLayout.setEnableLoadMore(false);
            }
            smallAdapter.setNewData(searchResult.shortVideoInfoDTOList);
        }
    }

    public void addData(SearchResultDto searchResult) {
        smartRefreshLayout.finishLoadMore();
        if (type == TYPE_SHORT) {
            if(searchResult.movieInfoDTOList.size()<10){
                smartRefreshLayout.setEnableLoadMore(false);
            }
            shortAdapter.addData(searchResult.movieInfoDTOList);
        } else {
            if(searchResult.shortVideoInfoDTOList.size()<10){
                smartRefreshLayout.setEnableLoadMore(false);
            }
            smallAdapter.addData(searchResult.shortVideoInfoDTOList);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        RxBus.get().post(new SearchEvent(false));

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        RxBus.get().post(new SearchEvent(true));
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
