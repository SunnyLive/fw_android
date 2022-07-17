package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.presenter.MessagesPresenter;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface IMessageActivityView extends MvpView {
    public void setMessageList(List<MessagesPresenter.Friend> d);
}
