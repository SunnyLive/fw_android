package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_comment.base.MvpView;

import java.util.ArrayList;

public interface ICardHomeView extends MvpView {
    void setBanner(ArrayList<AdvertiseBean> records);

    void setCircleInfo(RecommendCircleBean data);

    void setMemberList(CardMemberModel data);
}
