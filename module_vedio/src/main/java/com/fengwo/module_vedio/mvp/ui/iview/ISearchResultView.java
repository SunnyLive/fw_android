package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_vedio.mvp.dto.SearchResultDto;
import com.fengwo.module_vedio.mvp.dto.VideoSearchDto;

import java.util.List;

public interface ISearchResultView extends IBaseVideoView {
    public void showGuessData(List<String> data);
    public void showSearchResult(VideoSearchDto data,String content);
}
