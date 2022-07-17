package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.BankBindStatusDTO;
import com.fengwo.module_login.mvp.dto.BindBankDTO;
import com.fengwo.module_login.mvp.ui.iview.ISafeView;

public class SafePresenter extends BaseLoginPresenter<ISafeView> {

    public void getBankBindStatus() {
        addNet(service.getBankBindStatus().compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<BankBindStatusDTO>>() {
                    @Override
                    public void _onNext(HttpResult<BankBindStatusDTO> data) {
                        getView().setBankStatus(data.data.isBank);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void getBankInfo() {
        addNet(service.getBankInfo().compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<BindBankDTO>>() {
                    @Override
                    public void _onNext(HttpResult<BindBankDTO> data) {
                        getView().setBankInfo(data.data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
