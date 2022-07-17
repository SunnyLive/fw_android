package com.fengwo.module_chat.mvp.ui.event;

import com.fengwo.module_chat.mvp.model.bean.CommentModel;

public class CommentRefreshEvent {

    public int parentIndex;
    public int position;
    public CommentModel model;
    public String cardId;

    public CommentRefreshEvent(int parentIndex, int position, CommentModel model) {
        this.parentIndex = parentIndex;
        this.position = position;
        this.model = model;
    }
}
