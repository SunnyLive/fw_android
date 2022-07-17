package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.dto.SmallCommentDto;

import java.util.List;

public interface IShortVedioDetailView extends IBaseVideoView {

    void setDetailData(ShortVedioListDto.ShortVedio detailData);

    void setComments(List<SmallCommentDto.Comment> records, int page);

    void toggleFavourite();
}
