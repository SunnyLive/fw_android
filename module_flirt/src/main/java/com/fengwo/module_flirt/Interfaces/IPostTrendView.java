package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.CardTagModel;

import java.util.List;

public interface IPostTrendView extends MvpView {
    void postCardSuccess(boolean isDraft,CardDetailBean bean);
    void getCardDetail(CardDetailBean bean);
    void setAllTag(List<CardTagModel> cardTagModelList);
}
