package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_comment.base.MvpView;

import java.util.ArrayList;

public interface ICardSelectView extends MvpView {
    void setCircles(ArrayList<RecommendCircleBean> records);

    void setLoveCircleSuccess();
}
