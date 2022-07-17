package com.fengwo.module_vedio.eventbus;

import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;

public class ShortVideoCommentRefreshEvent {

    public int parentIndex;
    public int position;
    public ShortVideoCommentDto model;
    public String movieId;

    public ShortVideoCommentRefreshEvent(int parentIndex, int position, ShortVideoCommentDto model) {
        this.parentIndex = parentIndex;
        this.position = position;
        this.model = model;
    }
}
