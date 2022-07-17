package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.ui.contract.IChatCardChildView;
import com.fengwo.module_comment.base.BaseHttpData;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

public class ChatCardChildPresenter extends BaseChatPresenter<IChatCardChildView> {

    public void cardLike(String id) {
        addNet(service.cardLike(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardLikeSuccess(id);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    public void attentionUser(String id) {
        addNet(
                service.addAttention(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) getView().attentionSuccess(id);
                        else getView().toastTip(data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void unattentionUser(String id) {
        addNet(
                service.removeAttention(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().removeAttentionSuccess(id);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
