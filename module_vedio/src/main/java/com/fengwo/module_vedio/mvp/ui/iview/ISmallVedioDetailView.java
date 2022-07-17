package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_vedio.mvp.dto.SmallCommentDto;

import java.util.List;

public interface ISmallVedioDetailView extends IBaseVideoView {

    void setLike(int position);

    void setDelete(HttpResult httpResult);
}
