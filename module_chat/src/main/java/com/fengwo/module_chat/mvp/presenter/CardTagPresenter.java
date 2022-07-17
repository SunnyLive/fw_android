package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_chat.mvp.ui.contract.ICardTagView;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.List;

public class CardTagPresenter extends BaseChatPresenter<ICardTagView> {

    public void getTagList(int circleId) {
        addNet(service.getTagList(circleId).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<List<CardTagModel>>() {
                    @Override
                    public void _onNext(List<CardTagModel> data) {
                        getView().setAllTag(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
}
