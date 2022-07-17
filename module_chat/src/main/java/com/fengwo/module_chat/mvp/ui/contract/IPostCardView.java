package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.CardTagModel;

import java.util.List;

public interface IPostCardView extends MvpView {
    void postCardSuccess(boolean isDraft);
    void getCardDetail(CardDetailBean bean);
    void setAllTag(List<CardTagModel> cardTagModelList);
}
