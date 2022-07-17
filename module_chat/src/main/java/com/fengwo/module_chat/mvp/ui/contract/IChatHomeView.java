package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;

import java.util.ArrayList;
import java.util.List;

public interface IChatHomeView extends MvpView {

    void setRecommendCircleList(List<RecommendCircleBean> data);

    void setTopBanner(ArrayList<AdvertiseBean> records);

    void setCardList(BaseListDto<CircleListBean> data);

    void loadListFailed(String msg);

    void cardLikeSuccess(String id, int position);
}
