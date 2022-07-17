package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_login.mvp.dto.NobilityListDTO;
import com.fengwo.module_login.mvp.dto.NobilityTypeDTO;
import com.fengwo.module_login.mvp.dto.WalletDto;
import com.fengwo.module_login.mvp.ui.iview.INobilityPrivilegeView;

import java.util.List;

public class NobilityPrivilegePresenter extends BaseLoginPresenter<INobilityPrivilegeView> {

    public void getNobilityTypeList() {
        addNet(
                service.getNobilityTypeList("2")
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<NobilityTypeDTO>>() {
                            @Override
                            public void _onNext(List<NobilityTypeDTO> data) {
                                getView().setPrivilegeTypeList(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        }));
    }

    public void getNobilityPrivilege() {
        addNet(service.getNobilityPrivilege().compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<NobilityListDTO>(getView()) {
                    @Override
                    public void _onNext(NobilityListDTO data) {
                        getView().setPrivilegeList(data.nobilityList);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void buyNobility(int id) {
        addNet(service.buyNobility(id).compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<PopoDto>>() {
                    @Override
                    public void _onNext(HttpResult<PopoDto> data) {
                        if (data.isSuccess()) {
                            getView().buyNobilitySuccess(data);
                        } else getView().buyNobilityFail(data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().buyNobilityFail(msg);
                    }
                }));
    }

    public void updateWalletInfo() {
        addNet(service.getWalletInfo()
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<WalletDto>() {
                    @Override
                    public void _onNext(WalletDto data) {
                        Long huazun = data.preBalance + data.balance;
                        getView().updateWalletInfo(huazun);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }
}