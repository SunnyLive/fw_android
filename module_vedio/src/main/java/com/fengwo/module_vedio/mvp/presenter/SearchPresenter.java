package com.fengwo.module_vedio.mvp.presenter;

import android.util.Log;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_vedio.mvp.dto.SearchResultDto;
import com.fengwo.module_vedio.mvp.dto.VideoHomeCategoryDto;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;
import com.fengwo.module_vedio.mvp.ui.iview.ISearchResultView;


import java.util.ArrayList;
import java.util.List;

import static com.fengwo.module_vedio.mvp.ui.activity.SearchActivity.SP_KEY_SEARCH_HISTORY;

public class SearchPresenter extends BaseVideoPresenter<ISearchResultView> {
    private static final String TAG = "SearchPresenter";
    /**
     * get Guess
     */
    public void getSearchGuessData() {
        service.getVideoSearchHot().compose(io_main()).compose(handleResult())
                .subscribe(new LoadingObserver<List<String>>() {
                    @Override
                    public void _onNext(List<String> data) {
                        getView().showGuessData(data);
                    }
                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });
    }

    /**
     *  TODO search video
     */
    public void search(String searchContent,int page) {
        L.e(TAG, "search: "+page );
        service.getVideoSearch(searchContent,page+",10").compose(io_main()).compose(handleResult())
                .subscribe(new LoadingObserver<VideoSearchDto>() {
                    @Override
                    public void _onNext(VideoSearchDto data) {
                        getView().showSearchResult(data,searchContent);
                    }
                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });
    }
}
