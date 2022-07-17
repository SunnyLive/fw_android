package com.fengwo.module_login.mvp.presenter;

import android.app.Service;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.ProfitDto;
import com.fengwo.module_login.mvp.ui.iview.IProfitView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/18
 */
public class ProfitPresenter extends BaseLoginPresenter<IProfitView> {

    public void getProfit(){
        service.getMyProfit()
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<ProfitDto>() {
                    @Override
                    public void _onNext(ProfitDto data) {
                        getView().setProfitData(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void  getIsH5CashOut(){

    }

}