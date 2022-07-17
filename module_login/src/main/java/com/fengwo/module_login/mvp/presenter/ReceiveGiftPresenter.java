package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.ReceiveGiftDto;
import com.fengwo.module_login.mvp.ui.iview.IReceiveGiftView;

public class ReceiveGiftPresenter extends BaseLoginPresenter<IReceiveGiftView> {

    public void getReceiveGiftData(String pageParams, int type) {
        addNet(
                service.getReceiveGift(pageParams, type)
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<ReceiveGiftDto>() {
                            @Override
                            public void _onNext(ReceiveGiftDto data) {
                                getView().receiveGift(data);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        }));
    }
}
