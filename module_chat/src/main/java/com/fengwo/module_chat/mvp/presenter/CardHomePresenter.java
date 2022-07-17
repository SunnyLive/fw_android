package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.ui.contract.ICardHomeView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;

public class CardHomePresenter extends BaseChatPresenter<ICardHomeView> {

    public void getHomeBanner(String pageParam, int position, String parentId) {
        addNet(service.getBanner(2, 1, pageParam, 2, parentId, position)
                .compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<AdvertiseBean>>() {
                    @Override
                    public void _onNext(BaseListDto<AdvertiseBean> data) {
                        if (getView() != null) getView().setBanner(data.records);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() != null) getView().toastTip(msg);
                    }
                }));
    }

    public void getCircleInfo(String id) {
        addNet(service.getCircleInfo(id).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<RecommendCircleBean>() {
                    @Override
                    public void _onNext(RecommendCircleBean data) {
                        if (getView() != null) getView().setCircleInfo(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() != null) getView().toastTip(msg);
                    }
                }));
    }

    public void getCircleMember(String circleId) {
        addNet(service.getCircleMemberList(circleId).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<CardMemberModel>() {
                    @Override
                    public void _onNext(CardMemberModel data) {
                        if (getView() != null) getView().setMemberList(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() != null) getView().toastTip(msg);
                    }
                }));
    }
}
