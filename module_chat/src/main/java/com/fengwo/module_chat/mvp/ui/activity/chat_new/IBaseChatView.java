package com.fengwo.module_chat.mvp.ui.activity.chat_new;

import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_comment.base.MvpView;

public interface IBaseChatView extends MvpView {
    void enterGroupSuccess(EnterGroupModel data);

    void enterGroupFail();

    void hasItemInSession(boolean hasItemInSession);


    void sendRandomContent(String title);

    void onAttentionSuccess();
}
