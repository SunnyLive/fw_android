package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface IChatCardSameView extends MvpView {
    void setLableList(List<CardTagModel> datas);
    void setCardList(List<CircleListBean> datas);
    void cardLikeSuccess(String id, int positoin);
}
