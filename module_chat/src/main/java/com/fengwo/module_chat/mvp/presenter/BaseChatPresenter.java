package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.RetrofitUtils;

public abstract class BaseChatPresenter<T extends MvpView> extends BasePresenter<T> {
    protected ChatService service;

    public BaseChatPresenter() {
        service = new RetrofitUtils().createApi(ChatService.class);
    }
}
