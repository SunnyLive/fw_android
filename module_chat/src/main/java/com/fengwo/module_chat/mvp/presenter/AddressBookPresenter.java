package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.ContactBean;
import com.fengwo.module_chat.mvp.ui.contract.IAddressBookView;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.List;

public class AddressBookPresenter extends BaseChatPresenter<IAddressBookView> {

    public void getContactList() {
        addNet(
                service.getContactList().compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<ContactBean>>() {
                            @Override
                            public void _onNext(List<ContactBean> data) {
                                getView().setContactListData(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }
}
