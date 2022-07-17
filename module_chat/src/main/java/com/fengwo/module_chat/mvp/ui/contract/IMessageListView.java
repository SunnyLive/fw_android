package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface IMessageListView extends MvpView {
    void setMessageList(List<ChatListItemEntity> data);

    void refreshMessageList();
}
