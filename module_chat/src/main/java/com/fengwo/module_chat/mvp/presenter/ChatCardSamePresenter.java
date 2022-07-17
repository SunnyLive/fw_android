package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.ui.contract.IChatCardSameView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.ArrayList;
import java.util.List;

public class ChatCardSamePresenter extends BaseChatPresenter<IChatCardSameView> {

    public void getLableList(String cardId) {
        addNet(
                service.getCardTagList(cardId).compose(io_main()).compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<CardTagModel>>() {
                            @Override
                            public void _onNext(List<CardTagModel> data) {
                                getView().setLableList(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }


    public void getLableCircleIdList(String circleId) {
        addNet(
                service.getCardTagList(circleId).compose(io_main()).compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<CardTagModel>>() {
                            @Override
                            public void _onNext(List<CardTagModel> data) {
                                getView().setLableList(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }


    public void getCardList(String pageParam,String tagIds) {
        addNet(
                service.getCardFromTags(pageParam,tagIds).compose(io_main()).compose(handleResult())
                        .subscribeWith(new LoadingObserver<BaseListDto<CircleListBean>>() {
                            @Override
                            public void _onNext(BaseListDto<CircleListBean> data) {
                                ArrayList<CircleListBean> records = data.records;
                                getView().setCardList(records);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }

    public void cardLike(String id, int position) {
        addNet(service.cardLike(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardLikeSuccess(id, position);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }
}