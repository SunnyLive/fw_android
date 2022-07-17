package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_comment.base.MvpView;

public interface IChatCardChildView extends MvpView {
    void cardLikeSuccess(String id);
    void attentionSuccess(String id);
    void removeAttentionSuccess(String id);
}
