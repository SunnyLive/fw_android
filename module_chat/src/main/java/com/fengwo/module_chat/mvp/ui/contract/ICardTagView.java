package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface ICardTagView extends MvpView {

    void setAllTag(List<CardTagModel> data);
}
