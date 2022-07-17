package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_chat.mvp.ui.contract.IChatCardView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatCardPresenter extends BaseChatPresenter<IChatCardView> {
    //方向:0 当前页，-1 右滑，1 左滑
    public void getCardList(String cardId, String circleId, int direction, int uid, int tab, int isLike) {
        addNet(
                service.getCardList(cardId, circleId, direction, uid , tab,"1,20", isLike).compose(io_main()).compose(handleResult())
                        .subscribeWith(new LoadingObserver<BaseListDto<ChatCardBean>>() {
                            @Override
                            public void _onNext(BaseListDto<ChatCardBean> data) {
                                ArrayList<ChatCardBean> records = data.records;
                                if (direction == 0) {
                                    getView().setCardList(records);
                                } else if (direction == -1) {
                                    getView().addLeftCardList(records);
                                } else {
                                    getView().addRightCardList(records);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }


    public void getMemberList(int position, String circleId) {
        addNet(
                service.getCircleMemberList(circleId).compose(io_main()).compose(handleResult())
                        .subscribeWith(new LoadingObserver<CardMemberModel>() {
                            @Override
                            public void _onNext(CardMemberModel data) {
                                getView().setMemberList(position, data);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }


    public void attentionUser(String id, int position) {
        addNet(
                service.addAttention(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) getView().attentionSuccess(id, position);
                        else getView().toastTip(data.description);
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

    public void cardTop(String cardId, boolean isTop, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cardId", cardId);
        map.put("state", isTop ? 1 : 0);//1置顶 0未置顶
        addNet(service.cardTop(createRequestBody(map)).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardTopSuccess(cardId, position);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    public void cardPower(String cardId, int powerStatus, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", cardId);
        map.put("powerStatus", powerStatus);// 0 所有人可见  1 仅自己可见  2 仅好友可见
        addNet(service.cardPower(createRequestBody(map)).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardPowerSuccess(cardId, position, powerStatus);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    public void cardDelete(String cardId, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cardId", cardId);
        addNet(service.cardDelete(createRequestBody(map)).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardDeleteSuccess(cardId, position);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }
    public void doPost(String cardId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", cardId);
        service.cardDraftPost(createRequestBody(map)).compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().hideLoadingDialog();
                        if (data.isSuccess()) {
                            getView().postCardSuccess(false);
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        getView().toastTip(msg);
                    }
                });
    }

}